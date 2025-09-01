package org.fileservice.service;

import java.util.Optional;

import org.apache.struts2.ServletActionContext;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dao.SigninDAO;
import org.fileservice.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



public class SigninService {

    SigninDAO signinDAO=new SigninDAO();
    
    public int signinUser(String email,long number,String password){


        if(email!=null){

            return validateCredentialByEmail(email,password);
        }
        else {
            return validateCredentialByNumber(number,password);
        }
    }

    private int  validateCredentialByEmail(String email, String password){

        Optional<User> optionalUser=signinDAO.findUserByEmail(email);
        if(optionalUser.isEmpty())throw new UserNotFountException("user not exists please signup");
        User user=optionalUser.get();
		if(!password.equals(user.getPassword()))return 0;
        HttpServletResponse res = ServletActionContext.getResponse();
        System.out.println("user id:"+user.getId());
        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);           // localhost only
        cookie.setPath("/");     
        res.addCookie(cookie);
        
        return user.getId();

        
        
        

    }
    private int  validateCredentialByNumber(long number, String password){
        Optional<User> optionalUser=signinDAO.findUserByNumber(number);
        if(optionalUser.isEmpty())throw new UserNotFountException("user not exists please signup");
        User user=optionalUser.get();

        if(!password.equals(user.getPassword()))return 0;
        HttpServletResponse res = ServletActionContext.getResponse();
        System.out.println("user id:"+user.getId());
        Cookie cookie = new Cookie("userId", String.valueOf(user.getId()));
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);        
        cookie.setPath("/");    
        res.addCookie(cookie);	
       
        


        return user.getId();
    }
    
    
}
