package com.bobo;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
 
public class SendEmail extends HttpServlet{
    
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException{
	  
	  // 获取系统的属性
	  Properties props = System.getProperties();
      props.put("mail.smtp.starttls.enable", true); 
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.user", "username");
      props.put("mail.smtp.password", "password");
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", true);
      
      String from = "boofcourse@gmail.com";
      String to = "zhangbo198912@hotmail.com";
 
      // 获取默认的 Session 对象
      Session session = Session.getDefaultInstance(props);
      
      response.setContentType("text/html;charset=UTF-8");
      PrintWriter out = response.getWriter();

      try{
         // 创建一个默认的 MimeMessage 对象
         MimeMessage message = new MimeMessage(session);
         System.out.println("Port: "+session.getProperty("mail.smtp.port"));
         
         // 设置 From: header field of the header.
         message.setFrom(new InternetAddress(from));
         
         // 设置 To: header field of the header.
         message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(to));
         // 设置 Subject和实际消息
         message.setSubject("This is the Subject Line!");
         message.setText("This is an actual message from: "+from);
         
         // 发送消息
         Transport transport = session.getTransport("smtp");
         transport.connect("smtp.gmail.com", "boofcourse@gmail.com", "yourpassword");//put actual password
         System.out.println("Transport: "+transport.toString());
         transport.sendMessage(message, message.getAllRecipients());
         
         String title = "Send Email";
         String res = "Send successfully...";
         String docType = "<!DOCTYPE html> \n";
         out.println(docType +
         "<html>\n" +
         "<head><title>" + title + "</title></head>\n" +
         "<body bgcolor=\"#f0f0f0\">\n" +
         "<h1 align=\"center\">" + title + "</h1>\n" +
         "<p align=\"center\">" + res + "</p>\n" +
         "</body></html>");
         
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
   }
} 