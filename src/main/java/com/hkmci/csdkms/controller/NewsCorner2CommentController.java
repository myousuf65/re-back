package com.hkmci.csdkms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.common.Common;
import com.hkmci.csdkms.common.EmailUtil;
import com.hkmci.csdkms.entity.NewsCorner2;
import com.hkmci.csdkms.entity.Log;
import com.hkmci.csdkms.entity.ScoreLog;
import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.model.NewsCorner2CommentModel;
import com.hkmci.csdkms.model.NewsCorner2LikeModel;
import com.hkmci.csdkms.model.InstitutionsModel;
import com.hkmci.csdkms.model.SectionModel;
import com.hkmci.csdkms.repository.NewsCorner2AssistantRepository;
import com.hkmci.csdkms.service.NewsCorner2CommentService;
import com.hkmci.csdkms.service.NewsCorner2Service;
import com.hkmci.csdkms.service.LogService;
import com.hkmci.csdkms.service.UserService;
import com.vdurmont.emoji.EmojiParser;

@CrossOrigin
@RestController
@RequestMapping("/newscorner2/comment")
public class NewsCorner2CommentController {
	@Autowired
	@Resource
	private NewsCorner2CommentService newscorner2CommentService;
	
	@Autowired
	@Resource
	private NewsCorner2Service newscorner2Service;
	
	@Autowired
	@Resource
	private Common common;
	
	@Autowired
	@Resource
	private LogService logger;
	
	@Autowired
	@Resource
	private UserService userService;
	
	private final NewsCorner2AssistantRepository newscorner2AssistantRepository;

	public NewsCorner2CommentController(NewsCorner2CommentService theNewsCorner2CommentServer,NewsCorner2AssistantRepository theNewsCorner2AssistantRepository ) {
		newscorner2CommentService = theNewsCorner2CommentServer;
		newscorner2AssistantRepository = theNewsCorner2AssistantRepository;
	}
	
	@RequestMapping("/all")
	public List<NewsCorner2CommentModel> findAll(){
		return newscorner2CommentService.findAll();
	}
	
	@RequestMapping("/getComments/{postId}")
	public ResponseEntity<JsonResult> getComments(@PathVariable Long postId,
			@RequestParam(name="page") Integer page, @RequestParam(name = "user") Long userId, HttpSession session){
		
		Integer limitStart = (page - 1) * 5;
		//Integer limitEnd = page * 5;
		Integer limitEnd =  5;
		
		////System.out.println("start = "+ limitStart +" end = " +limitEnd);
		List<Long> limitCommentIds = newscorner2CommentService.getPageComments(postId,limitStart,limitEnd);
		
		Map<String,Object> return_data = newscorner2CommentService.getComments(postId,page,userId,limitCommentIds);
		//System.out.println("how many return = "+ return_data.size());
		@SuppressWarnings("unchecked")
		List<NewsCorner2CommentModel> return_list = (List<NewsCorner2CommentModel>) return_data.get("list");
		//System.out.println("how many return = "+ return_list.size());
		return JsonResult.twoList("Here is Comments data",return_list,Integer.parseInt(return_data.get("total").toString()),Integer.parseInt(return_data.get("total_for_page").toString()),session);
	}

	@RequestMapping("/getDetail/{PostId}")
	public List<NewsCorner2CommentModel> findPost(@PathVariable Long PostId){
		List<NewsCorner2CommentModel> theNewsCorner2CommentModel = newscorner2CommentService.findByPost(PostId);
		return theNewsCorner2CommentModel;
	}
	
	@RequestMapping("/create")
	public ResponseEntity<JsonResult> createComment(@RequestBody JsonNode jsonNode, HttpSession session) {
		NewsCorner2CommentModel new_comment = new NewsCorner2CommentModel();
		//Optional<User> createdBy = userService.findById(jsonNode.get("createdBy").asLong());
		
		Long createdBy = jsonNode.get("createdBy").asLong();
		Integer checkUser = common.checkUserSession(createdBy, session);
		if(checkUser.equals(0)) {
			return JsonResult.errorMsg("Request User Status Error");
		}else {
			User user = (User) session.getAttribute("user_session"); 
			Optional<User> created_user = userService.findById(jsonNode.get("createdBy").asLong());
			Long is_reply2cmnt = jsonNode.get("rootCmntId").asLong() == -1 ? 
					1 :
					jsonNode.get("rootCmntId").asLong();
			
			//Check invalid level one comments
			if(newscorner2CommentService.checkIsReply2cmnt(is_reply2cmnt,jsonNode.get("postId").asLong())) {
				//System.out.println(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
				new_comment.setShowAsAlias(jsonNode.get("showAsAlias").asLong());
				new_comment.setAlias(jsonNode.get("alias").asText());
				new_comment.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
				new_comment.setCreatedAt(new Date());
				new_comment.setIsDeleted(0);
				new_comment.setCreatedBy(created_user.get());
				new_comment.setPostId(jsonNode.get("postId").asLong());
				new_comment.setIsRely2cmnt(is_reply2cmnt);
				
				NewsCorner2CommentModel return_data = newscorner2CommentService.save(new_comment);
				
				//Update User Score
				Integer channel = (Integer) session.getAttribute("channel");
				Long postId = jsonNode.get("postId").asLong();
				
			
				//System.out.println("Total Score = "+created_user.get().getScore()+" Today Score = "+ created_user.get().getTodayScore());
				if(27<created_user.get().getTodayScore() && created_user.get().getTodayScore()<29) {
					user.setScore(created_user.get().getScore()+2);
						
					} else if (28<created_user.get().getTodayScore() && created_user.get().getTodayScore()<30) {
						user.setScore(created_user.get().getScore()+1);
						
					} else {
						user.setScore(created_user.get().getScore()+3);
					}
				
				
				new Thread(() ->{
					Storelog(session,user,return_data,channel,postId);
				}).start();
				
				return_data.setSubComments(new ArrayList<NewsCorner2CommentModel>());
				return_data.setLikes(0L);
				return_data.setContent(jsonNode.get("content").asText());
				return_data.setLiked(0);
				
				Optional<NewsCorner2> newscorner2 = newscorner2Service.findById(jsonNode.get("postId").asLong());
				
				//For Send Notice Email
				@SuppressWarnings("unchecked")
				List<User> user_session_list = (List<User>) session.getAttribute("user_list");
				String showedName = jsonNode.get("showAsAlias").asText().equals("0") ? user.getFullname() : jsonNode.get("alias").asText();
				NewsCorner2 comment_of_post = newscorner2Service.findById(jsonNode.get("postId").asLong()).get();
				//User sendEmailToCreatedBy = userService.findById(comment_of_post.getCreatedBy()).get();
				User sendEmailToOriginal = comment_of_post.getOriginalCreator();
				List<Long> sendEmailToAssistantId = newscorner2AssistantRepository
												  .findAssistantIdByUserIdAndIsDeleted(sendEmailToOriginal.getId(), 0);
				List<User> sendToList = new ArrayList<>();
				if(sendEmailToAssistantId == null || sendEmailToAssistantId.size() == 0) {
					sendToList.add(sendEmailToOriginal);
				}else {
					sendToList.add(sendEmailToOriginal);
					List<User> sendEmaiList = sendEmailToAssistantId.stream()
						  .map((u) -> {
							  User temp_user = user_session_list
									  		  .stream()
									  		  .filter(n -> n.getId().equals(u))
									  		  .collect(Collectors.toList()).get(0);
							//System.out.println("Temp_User:" + temp_user.getStaffNo() + "--" + temp_user.getEmail() + "--" + temp_user.getNotesAccount());
							
							temp_user.setEmail((user_session_list.stream()
									 .filter((n) -> n.getId().equals(temp_user.getId()))
									 .map(User::getEmail)
									 .collect(Collectors.toList())
									 .get(0)));
							//System.out.println("Temp_User222:" + temp_user.getStaffNo() + "--" + temp_user.getEmail());
							  return temp_user;
						  })
						  .collect(Collectors.toList());
					sendToList.addAll(sendEmaiList);
				}
//Send email by Notes Client
//				common.sendNotice(sendToList,newscorner2.get().getPostTitle(), showedName, return_data.getContent());
				EmailUtil.sendEmail(sendToList, newscorner2.get().getPostTitle(), showedName, return_data.getContent());
				
				return JsonResult.ok(return_data, session);
			}else {
				return JsonResult.errorMsg("Invalid Comments Request.");
			}
		}
	}
	
	
	
	 
		public void Storelog(HttpSession session, User user,NewsCorner2CommentModel return_data,Integer channel,Long postId ) {
			
	
			//Update User Score -----------------------------------------------------------------------------------
			//Optional<NewsCorner2> target_newscorner2 = newscorner2Service.findById(pkId);
			Log log = logger.commentNewsCorner2(user, return_data.getId(), "", "Success",channel,postId);

			
			Integer todayScore = user.getTodayScore();
			Integer addScore = 30-todayScore;
	
			if(3>addScore) {
			ScoreLog scoreLog = new ScoreLog(log.getId(),user,addScore);
			logger.saveScoreLog(scoreLog);
			//System.out.println("----small than 2 ----");
			user.setTodayScore(todayScore + addScore );
			session.setAttribute("user_session", user);
			} else {
				ScoreLog scoreLog = new ScoreLog(log.getId(),user,3);
				logger.saveScoreLog(scoreLog);
				//System.out.println("---bigger than 2 -----");			
				user.setTodayScore(todayScore +3);
				session.setAttribute("user_session", user);
				
			}
			
			userService.addUser(user);
			
			//Exist----------------------------------------------------------------------------------------------

		}
	
	
	
	@RequestMapping("/update")
	public ResponseEntity<JsonResult> updateComment(@RequestBody JsonNode jsonNode, HttpSession session) {
		//NewsCorner2CommentModel new_comment = new NewsCorner2CommentModel();
		Optional<NewsCorner2CommentModel> old_comment = newscorner2CommentService.findById(jsonNode.get("id").asLong());
		if(old_comment.isPresent()) {
			NewsCorner2CommentModel new_comment = old_comment.get();
			new_comment.setShowAsAlias(jsonNode.get("showAsAlias").asLong());
			new_comment.setAlias(jsonNode.get("alias").asText());
			new_comment.setContent(EmojiParser.parseToHtmlDecimal(jsonNode.get("content").asText()));
			new_comment.setModifiedBy(jsonNode.get("modifiedBy").asLong());
			new_comment.setModifiedAt(new Date());
			NewsCorner2CommentModel return_data = newscorner2CommentService.save(new_comment);
			return_data.setContent(jsonNode.get("content").asText());
			return JsonResult.ok(return_data,session);
		}else {
			return JsonResult.errorMsg("Invalid Comments Request.");
		}
	}
	
	@RequestMapping("/delete")
	public ResponseEntity<JsonResult> deletecComment(@RequestBody JsonNode jsonNode,HttpSession session) {
		Long commentId = jsonNode.get("commentId").asLong();
		Optional<NewsCorner2CommentModel> deleteComment = newscorner2CommentService.findById(commentId);
		Optional<User> createdBy = userService.checkDeletePermissionById(jsonNode.get("createdBy").asLong(),commentId);
		
		if(createdBy.isPresent()) {
			if(deleteComment.isPresent()) {
				newscorner2CommentService.delete(deleteComment.get(),jsonNode.get("createdBy").asLong());
				return JsonResult.ok(session);
			}else {
				return JsonResult.errorMsg("Invalid Comments Request.");
			}
		}else {
			return JsonResult.errorMsg("Invalid User Request.");
		}
	}
	
//	@RequestMapping("/create")
//	public NewsCorner2CommentModel addPost(@RequestBody NewsCorner2CommentModel TheModel) {
//		TheModel.setId(0);
//		newscorner2CommentService.save(TheModel);
//		return TheModel;
//	}
	

}

