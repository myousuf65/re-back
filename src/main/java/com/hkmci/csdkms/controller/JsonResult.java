package com.hkmci.csdkms.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkmci.csdkms.entity.User;

/**
 *
 * @Title: JsonResult.java
 * @Package com.hkmci.controller
 * @Description: Return data structure
 *              200：Success
 *              500：Error,show message at message
 *              501：bean error return in map format
 *              555：Exception
 * Copyright: Copyright (c) 2016
 * Company:Nathan.Lee.Salvatore
 *
 * @author Holfer ZHANG
 * @date 2019-4-22 8:33:36
 * @version V1.0
 */
public class JsonResult {

    // jackson object
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // return status
    private Integer status;

    // return message
    private String msg;

    // return data
    private Object data;
    
   	private Object data2;
    // return with fileList
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object fileList;
    // resource list
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object list;

    private String ok;  // unused
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object pathObject;  // For Path
    
    private Integer total; //For total out of data
    
    private Integer score ; //For user score
    
    private Long loginTimes ; //For user loginTimes
    

    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static ResponseEntity<JsonResult> timeOut(String msg){
    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonResult(401, msg,null));
    	
    }
    
   
    
    public static ResponseEntity<JsonResult> ok2(Object data,Long id, HttpSession session) {
    	JsonResult new_JsonResult = new JsonResult(200,data,id,session);
    	return new ResponseEntity<>(new_JsonResult,HttpStatus.OK);
        //return new JsonResult(data);
    }
    
    public static ResponseEntity<JsonResult> ok(Object data, HttpSession session) {
    	return new ResponseEntity<>(new JsonResult(data,session),HttpStatus.OK);
        //return new JsonResult(data);
    }
    
    public static ResponseEntity<JsonResult> ok(Object data, String msg, HttpSession session) {
    	JsonResult new_JsonResult = new JsonResult(200, msg, data,session);
    	return new ResponseEntity<>(new_JsonResult,HttpStatus.OK);
        //return new_JsonResult;
    }
    public static ResponseEntity<JsonResult> ok(Object data, Object data2, HttpSession session) {
    	JsonResult new_JsonResult = new JsonResult(200,data,data2,session);
    	return new ResponseEntity<>(new_JsonResult,HttpStatus.OK);
        //return new_JsonResult;
    }

    public static ResponseEntity<JsonResult> ok(HttpSession session) {
    	return new ResponseEntity<>(new JsonResult(session),HttpStatus.OK);
        //return new JsonResult(null);
    }
    
    public static ResponseEntity<JsonResult> fileList(String msg,Object data,Object fileList, HttpSession session) {
    	return new ResponseEntity<>(new JsonResult(200,msg,data,fileList,session),HttpStatus.OK);
        //return new JsonResult(null);
    }
    
    public static ResponseEntity<JsonResult> list(String msg,Object data, Integer length,HttpSession session) {
    	
    	HashMap<String,Object> data_with_list = new HashMap<>();
    	data_with_list.put("list", data);
    	data_with_list.put("total", length);
    	return new ResponseEntity<>(new JsonResult(200 ,msg,data_with_list,session),HttpStatus.OK);
        //return new JsonResult(null);
    }
    
public static ResponseEntity<JsonResult> listTotal(String msg,Object data, Integer length, HttpSession session) {
    	return new ResponseEntity<>(new JsonResult(200,msg,data,length,session),HttpStatus.OK);
        //return new JsonResult(null);
    }
    
    public static ResponseEntity<JsonResult> path(String msg,Object data, Object pathArray, boolean is_path, HttpSession session) {
  
    	return new ResponseEntity<>(new JsonResult(200,msg,data,pathArray,is_path,session),HttpStatus.OK);
        //return new JsonResult(null);
    }
    
  
    
    public static ResponseEntity<JsonResult> twoList(String msg,Object data, Integer length, Integer total, HttpSession session) {
    	
    	HashMap<String,Object> data_with_list = new HashMap<>();
    	data_with_list.put("list", data);
    	data_with_list.put("total", length);
    	data_with_list.put("total_for_page", total);
    	return new ResponseEntity<>(new JsonResult(200,msg,data_with_list, session),HttpStatus.OK);
        //return new JsonResult(null);
    }

    public static ResponseEntity<JsonResult> relatedList(Object data,Object list, HttpSession session) {
    	
    	HashMap<String,Object> data_with_list = new HashMap<>();
    	data_with_list.put("data", data);
    	data_with_list.put("list", list);
    	return new ResponseEntity<>(new JsonResult(200,"",data_with_list, session),HttpStatus.OK);
        //return new JsonResult(null);
    }

    public static ResponseEntity<JsonResult> errorMsg(String msg) {
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResult(500, msg,null));
    }

    public static ResponseEntity<JsonResult> errorMsg22(String msg) {
    	return ResponseEntity.status(HttpStatus.OK).body(new JsonResult(500, msg,null));
    }
    
    

    public static ResponseEntity<JsonResult> errorMsgNodata(String msg) {
    	return ResponseEntity.status(HttpStatus.OK).body(new JsonResult(505, msg,null));
    }
    
    
    
    public static JsonResult errorMap(Object data) {
        return new JsonResult(501, "error", data);
    }

    public static  JsonResult errorException(String msg) {
        return new JsonResult(555, msg, null);
    }

    public JsonResult() {

    }
    
    public JsonResult(HttpSession session) {
    	User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }

//    public static JsonResult build(Integer status, String msg) {
//        return new JsonResult(status, msg, null);
//    }

    public JsonResult(Integer status, String msg, Object data, HttpSession session) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }
    
    
    
    public JsonResult(Integer status, Object data,Object data2, HttpSession session) {
        this.status = status;
        this.data = data;
        this.data2 = data2;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }
    
    
    public JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public JsonResult(Integer status, String msg, Object data, Integer length, HttpSession session) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.total = length;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }
    
    public JsonResult(Integer status, String msg, Object data, Object pathObject, boolean is_path, HttpSession session) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.pathObject = pathObject;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }
    
    public JsonResult(Integer status, String msg, Object data,Object fileList, HttpSession session) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.fileList = fileList;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }

    public JsonResult(Object data, HttpSession session) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
        User user = (User) session.getAttribute("user_session");
        this.score = user.getScore();
        this.loginTimes = user.getLoginTries();
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer length) {
        this.total = length;
    }
    
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Long loginTimes) {
		this.loginTimes = loginTimes;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public Object getData2() {
		return data2;
	}

	public void setData2(Object data2) {
		this.data2 = data2;
	}


    
    public Object getFileList() {
        return fileList;
    }

    public void setFileList(Object list) {
        this.list = list;
    }
    public Object getList() {
        return fileList;
    }

    public void setList(Object list) {
        this.list = list;
    }
    
    
    

    public Object getPathObject() {
		return pathObject;
	}

	public void setPathObject(Object pathObject) {
		this.pathObject = pathObject;
	}

	/**
     *
     * @Description: turn json result into JsonResult object
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author Holfer ZHANG
     * @date 2019-4-22 8:34:58
     */
    public static JsonResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, JsonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @Description:  No object
     * @param json
     * @return
     *
     * @author Holfer ZHANG
     * @date 2019-4-22 8:35:21
     */
    public static JsonResult format(String json) {
        try {
            return MAPPER.readValue(json, JsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @Description: Object is collection from list
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author Holfer ZHANG
     * @date 2019-4-22 8:35:31
     */
    public static JsonResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
    

	public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}
