package org.fileservice.service;

import org.apache.struts2.ServletActionContext;
import org.fileservice.Exception.UnAuthorizedUserException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieService {

    private int userId;

    public int getUserId(){
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();

		if(cookies!=null) {
				for(Cookie c : cookies) {
					if("userId".equals(c.getName())) {
							System.out.println("Found userId: " + c.getValue());
							userId= Integer.parseInt(c.getValue());
					}
				}
		}
        if(userId==0){

            throw new UnAuthorizedUserException("User need to Signin");
        }
        return userId;
    }
    
}
