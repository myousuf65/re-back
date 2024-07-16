package com.hkmci.csdkms.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.hkmci.csdkms.entity.User;
import com.hkmci.csdkms.storage.StorageService;
public class EmailUtil {
	
		@Autowired
		@Resource
		private static StorageService storageService;
	
   
   
 
	   @Value("${app.smtpAddress}")
	    private static String smtpAddress;


	
		public static void sendEmail(List<User> sendToList,String postTitle, String fullname, String content) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		    
		    try
		    {
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	String toEmail = sendToList.get(0).getEmail();
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 0; i < sendToList.size(); i++) {
				    System.out.println("Mail_CC: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i > 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    msg.addRecipient(Message.RecipientType.CC,address);
						}
					}
				}	
	            Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress);
	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
				msg.addRecipient(Message.RecipientType.CC, tempAddress2);
	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress3);
	           

			    msg.setRecipients(Message.RecipientType.TO, toEmail);

				String theSubject = "Comment From KMS Mini Blogs: " + fullname;
				String theContent = fullname + " leaves a comment for blog: " + postTitle + "\n" + content;
//				String filename ="";
//				
//				  Path resourcePath = storageService.getResourceLocation();
//				   

				msg.setSubject(theSubject, "UTF-8");
				
//				Path file = Paths.get( resourcePath.toString() + "/pdf/7000"+ "\\6001_e9c58a7b96f2a31.pdf" );
				
				msg.setSubject(theSubject, "UTF-8");
				msg.setText(theContent, "UTF-8");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
			   
//			    BodyPart messageBodyPart = new MimeBodyPart();
//			    Multipart multipart = new MimeMultipart();
//			    messageBodyPart.setText(theContent);
//			    multipart.addBodyPart(messageBodyPart);
//			    messageBodyPart = new MimeBodyPart();
//		         String filename1 = "/home/manisha/file.txt";
//		         DataSource source = new FileDataSource(filename1);
//		         messageBodyPart.setDataHandler(new DataHandler(source));
//		         messageBodyPart.setFileName(filename1);
//		         multipart.addBodyPart(messageBodyPart);
//		        System.out.println("Sending");
//
//			    msg.setContent(multipart);
//			    
//			    
//			    
//			    
			    
			    System.out.println("Message is ready -- Miniblog Comment. ");
		    	Transport.send(msg);  
		
			    System.out.println("Success: EMail Sent to: " + toEmail);
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}	
		
		
		
		
		
		public static void sendEmailToShareMiniBlog(List<User> sendToList,String resourceTitle, String fullname,Long channel, Long Id) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		    
		    try
		    {
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		 
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 0; i < sendToList.size(); i++) {
				    System.out.println("Mail_T0: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i >= 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress);
		    	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress2);
		    	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress3);
		                    msg.addRecipient(Message.RecipientType.TO,address);
						}
					}
				}	
				String theSubject =  "KMS : "+fullname +" shares a  Mini-Blog "+resourceTitle+" with you. ";
				String URL ="";
				String URL2 ="";
//				if(channel ==1) {
					URL = "https://dsp.csd.hksarg/kms/#/miniblog/details/?id="+Id;
			
//				} else {
					URL2 = "https://kms.csd.gov.hk/#/miniblog/details/?id=" +Id;
//				}
				
				
				String theContent = ""; 
//				if (channel == 2) {
					 theContent = fullname + " shares Mini-Blog "+ resourceTitle+" with you.<br> <br> " +
						
						"請於內聯網網址 <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> 登入知識管理系統，再按 <a href ="+ URL+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the intranet at <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> then click "+"<a href="+URL+">"+"here </a>to view it.<br>  <br>" +
						"請於互聯網網址 <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a>  登入知識管理系統，再按<a href ="+ URL2+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the internet at <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a>  then click "+"<a href="+URL2+">"+"here </a>to view it.";
//				}
//				if (channel < 2) {
//					 theContent = fullname + " shares "+ 
//
//						resourceTitle+" with you.<br> " +
//						"請於內聯網登入知識管理系統，再按 <a href ="+ URL+">此 </a> 瀏覽。 <br>"+
//						"Please log in KMS via the intranet then click "+"<a href="+URL+">"+"here </a>to view it.<br> " ;
//					}
				
				msg.setSubject(theSubject, "UTF-8");
				
				msg.setText(theContent, "UTF-8", "html");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
		
			    System.out.println("Message is ready" + theContent);
		    	Transport.send(msg);  
		
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}	
		
		

		public static void sendEmailToShareResource(List<User> sendToList,String resourceTitle, String fullname,Integer channel, Long Id) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		    
		    try
		    {
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		 
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 0; i < sendToList.size(); i++) {
				    System.out.println("Mail_T0: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i >= 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress);
		    	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress2);
		    	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress3);
		                    msg.addRecipient(Message.RecipientType.TO,address);
						}
					}
				}	
				String theSubject =  "KMS :"+ fullname +" shares "+resourceTitle+" with you. ";
				String URL ="";
				String URL2 ="";
				String theContent ="";
			
					URL = "https://dsp.csd.hksarg/kms/#/resourcedetails/?id="+Id;
			
				if (channel == 2){
					URL2 = "https://kms.csd.gov.hk/#/resourcedetails/?id=" +Id;
				}
				
				if (channel == 2) {
					 theContent = fullname + " shares "+ 
						resourceTitle+" with you.<br> <br> " +
						"請於內聯網網址 <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> 登入知識管理系統，再按 <a href ="+ URL+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the intranet  at <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> then click "+"<a href="+URL+">"+"here </a>to view it.<br>  <br>" +
						"請於互聯網網址 <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a> 登入知識管理系統，再按<a href ="+ URL2+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the internet at <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a>  then click "+"<a href="+URL2+">"+"here </a>to view it.";
				}
				if (channel < 2) {
					 theContent = fullname + " shares "+ 

						resourceTitle+" with you.<br> <br> " +
						"請於內聯網網址 <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> 登入知識管理系統，再按 <a href ="+ URL+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the intranet <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a>  then click "+"<a href="+URL+">"+"here </a>to view it.<br> <br>" ;
					}
				
				
				msg.setSubject(theSubject, "UTF-8");
				
				msg.setText(theContent, "UTF-8", "html");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
			    
//			    // Create the message part
//		         BodyPart messageBodyPart = new MimeBodyPart();
//
//		         // Now set the actual message
//		         messageBodyPart.setText(theContent);
//
//			    
//			    
//			    
//			    // Create a multipar message
//		         Multipart multipart = new MimeMultipart();
//
//		         multipart.addBodyPart(messageBodyPart);
//		  
//			    
//			    // Part two is attachment
//		         messageBodyPart = new MimeBodyPart();
//		         String filename = "D:\\csdkms\\backend\\test.pdf";
//		         DataSource source = new FileDataSource(filename);
//		         messageBodyPart.setDataHandler(new DataHandler(source));
//		         messageBodyPart.setFileName(filename);
//		         multipart.addBodyPart(messageBodyPart);
//		         msg.setContent(multipart);

			    
			    
		
			    System.out.println("Message is ready" + theContent);
		    	Transport.send(msg);  
		
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}	
		
		
		
		public static void sendEmailToShareCocktail(List<User> sendToList,String resourceTitle, String fullname,Long channel, Long Id) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		    
		    try
		    {
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		 
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 0; i < sendToList.size(); i++) {
				    System.out.println("Mail_T0: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i >= 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress);
		    	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress2);
		    	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
		    				msg.addRecipient(Message.RecipientType.BCC, tempAddress3);
		                    msg.addRecipient(Message.RecipientType.TO,address);
						}
					}
				}	
				String theSubject =  "KMS : "+fullname +" shares a  Mini-Blog "+resourceTitle+" with you. ";
				String URL ="";
				String URL2 ="";
//				if(channel ==1) {
					URL = "https://dsp.csd.hksarg/kms/#/miniblog/details/?id="+Id;
			
//				} else {
					URL2 = "https://kms.csd.gov.hk/#/miniblog/details/?id=" +Id;
//				}
				
				
				String theContent = ""; 
				
					 theContent = fullname + " shares cocktial "+ resourceTitle+" with you.<br> <br> " +
						
						"請於內聯網網址 <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> 登入知識管理系統，再按 <a href ="+ URL+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the intranet at <a href = https://dsp.csd.hksarg/> dsp.csd.hksarg </a> then click "+"<a href="+URL+">"+"here </a>to view it.<br>  <br>" +
						"請於互聯網網址 <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a>  登入知識管理系統，再按<a href ="+ URL2+">此 </a> 瀏覽。 <br> <br>"+
						"Please log in KMS via the internet at <a href = https://kms.csd.gov.hk/> kms.csd.gov.hk </a>  then click "+"<a href="+URL2+">"+"here </a>to view it.";

				
				msg.setSubject(theSubject, "UTF-8");
				
				msg.setText(theContent, "UTF-8", "html");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
		
			    System.out.println("Message is ready" + theContent);
		    	Transport.send(msg);  
		
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}	
		
		
		
		
		
		
		
public static void sendEmailCreateForumPost(List<User> sendToList,String postTitle, String fullname) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		   
		    try
		    {
		    	System.out.println("how many user will receive email = "+sendToList.size());
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	String toEmail = sendToList.get(0).getEmail();
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 1; i < sendToList.size(); i++) {
				    System.out.println("Mail_CC: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i > 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    msg.addRecipient(Message.RecipientType.CC,address);
						}
					}
				}	
	            Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress);
	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
				msg.addRecipient(Message.RecipientType.CC, tempAddress2);
	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress3);
	           

			    msg.setRecipients(Message.RecipientType.TO, toEmail);

				String theSubject =  fullname+" Creates Forum Post ";
				String theContent = fullname + " creates forum post : " + postTitle + "\n" ;

				msg.setSubject(theSubject, "UTF-8");
				msg.setText(theContent, "UTF-8");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
		
			    System.out.println("Message is ready");
		    	Transport.send(msg);  
		
			    System.out.println("Success: EMail Sent to: " + toEmail);
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}		
	

public static void sendEmailNoticAdmin(String fullname, String content, String title, String staffEmail) {
	
    System.out.println("SimpleEmail Start: "+smtpAddress);
	
    String smtpHostServer = "10.11.3.81";
//    String sentFrom = "MB System Administrator/CSD/HKSARG";
//    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
    String sentFrom = "administrator_mb_system@csd.hksarg";
    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//    toEmail = "chan_daniel_cy@csd.gov.hk";
    
    Properties props = System.getProperties();

    props.put("mail.smtp.host", smtpHostServer);

    Session session = Session.getInstance(props, null);
   
    try
    {
    MimeMessage msg = new MimeMessage(session);
	      //set message headers
	    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	    msg.addHeader("format", "flowed");
	    msg.addHeader("Content-Transfer-Encoding", "8bit");

	    msg.setFrom(sentFrom);
//	    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
//

        Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
		msg.addRecipient(Message.RecipientType.CC, tempAddress);
        Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
		msg.addRecipient(Message.RecipientType.CC, tempAddress2);
        Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
		msg.addRecipient(Message.RecipientType.CC, tempAddress3);
       

	    msg.setRecipients(Message.RecipientType.TO, staffEmail);

		String theSubject =  "Profile Updated: "+fullname +" has changed his/her icon.";
		String theContent = fullname + " has changed his/her icon, please go to Users Management to approve it.";
		
		
	

		msg.setSubject(title, "UTF-8");
		msg.setText(content , "UTF-8");
//	    msg.setText(content, "UTF-8");
	    msg.setSentDate(new Date());

	    System.out.println("Message is ready");
    	Transport.send(msg);  

//	    System.out.println("Success: EMail Sent to: " + toEmail);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
	 
}
		
public static void sendEmailForumComment(List<User> sendToList,String postTitle, String fullname, String content) {
			
		    System.out.println("SimpleEmail Start: "+smtpAddress);
			
		    String smtpHostServer = "10.11.3.81";
//		    String sentFrom = "MB System Administrator/CSD/HKSARG";
//		    String sentReplyTo = "MB System Administrator/CSD/HKSARG";
		    String sentFrom = "administrator_mb_system@csd.hksarg";
		    String sentReplyTo = "administrator_mb_system@csd.hksarg";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
		    
		    Properties props = System.getProperties();

		    props.put("mail.smtp.host", smtpHostServer);

		    Session session = Session.getInstance(props, null);
		   
		    try
		    {
		    	System.out.println("how many user will receive email = "+sendToList.size());
//		    	String toEmail = sendToList.get(0).getNotesAccount();
		    	String toEmail = sendToList.get(0).getEmail();
		    	MimeMessage msg = new MimeMessage(session);
			      //set message headers
			    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			    msg.addHeader("format", "flowed");
			    msg.addHeader("Content-Transfer-Encoding", "8bit");
		
			    msg.setFrom(sentFrom);
//			    msg.setReplyTo(InternetAddress.parse(sentReplyTo, false));
	//
				for(Integer i = 1; i < sendToList.size(); i++) {
				    System.out.println("Mail_CC: "+sendToList.get(i).getStaffNo() + "--" + sendToList.get(i).getEmail() + "--" + sendToList.get(i).getUsername()+"--" + sendToList.get(i).getNotesAccount());
					if( i > 0 ) {
						if (sendToList.get(i).getEmail() !=null) {
		                    Address address = new InternetAddress(sendToList.get(i).getEmail());
		                    msg.addRecipient(Message.RecipientType.CC,address);
						}
					}
				}	
	            Address tempAddress = new InternetAddress("yu_sw@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress);
	            Address tempAddress2 = new InternetAddress("administrator_mb_system@csd.hksarg");
				msg.addRecipient(Message.RecipientType.CC, tempAddress2);
	            Address tempAddress3 = new InternetAddress("chan_daniel_cy@csd.gov.hk");
				msg.addRecipient(Message.RecipientType.CC, tempAddress3);
	           

			    msg.setRecipients(Message.RecipientType.TO, toEmail);

				String theSubject = "Comment From KMS Forum Post: " + fullname;
				String theContent = fullname + " leaves a comment for post: " + postTitle + "\n" + content;
				
				
			

				msg.setSubject(theSubject, "UTF-8");
				msg.setText(theContent, "UTF-8");
//			    msg.setText(content, "UTF-8");
			    msg.setSentDate(new Date());
		
			    System.out.println("Message is ready");
		    	Transport.send(msg);  
		
			    System.out.println("Success: EMail Sent to: " + toEmail);
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		}	
		
		
//		public static void sendEmail(String toEmail, String subject, String body){
//			
//		    System.out.println("SimpleEmail Start");
//			
//		    String smtpHostServer = "10.11.3.81";
//		    toEmail = "chan_daniel_cy@csd.gov.hk";
//		    
//		    Properties props = System.getProperties();
	//
//		    props.put("mail.smtp.host", smtpHostServer);
	//
//		    Session session = Session.getInstance(props, null);
//		    
//		    try
//		    {
//		      MimeMessage msg = new MimeMessage(session);
//		      //set message headers
//		      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//		      msg.addHeader("format", "flowed");
//		      msg.addHeader("Content-Transfer-Encoding", "8bit");
	//
//		      msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));
	//
//		      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
	//
//		      msg.setSubject(subject, "UTF-8");
	//
//		      msg.setText(body, "UTF-8");
	//
//		      msg.setSentDate(new Date());
	//
//		      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
//		      System.out.println("Message is ready");
//	    	  Transport.send(msg);  
	//
//		      System.out.println("EMail Sent Successfully!!");
//		    }
//		    catch (Exception e) {
//		      e.printStackTrace();
//		    }
//		}
	}
