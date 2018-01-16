package com.bobo.count;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class SiteHitCounter implements Filter{
    
  private int hitCount; 
               
  public void  init(FilterConfig config) 
                    throws ServletException{
     hitCount = 0;
  }

  public void  doFilter(ServletRequest request, 
              ServletResponse response,
              FilterChain chain) 
              throws java.io.IOException, ServletException {

      // 把计数器的值增加 1
      hitCount++;
      System.out.println("网站访问统计："+ hitCount );

      // 把请求传回到过滤器链
      chain.doFilter(request,response);
  }
  public void destroy() { 
      // 这一步是可选的，但是如果需要，您可以把 hitCount 的值写入到数据库
  } 
} 
