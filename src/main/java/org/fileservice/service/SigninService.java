package org.fileservice.service;

import java.util.Optional;

import org.apache.struts2.ServletActionContext;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.model.User;
import org.fileservice.repository.ProfileRepository;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



public class SigninService {

   
    private ProfileRepository profileRepository;
    
    public int signinUser(String email,String number,String password){


        if(email!=null){

            return validateCredentialByEmail(email,password);
        }
        else {
            return validateCredentialByNumber(number,password);
        }
    }

    private int  validateCredentialByEmail(String email, String password){

        Optional<User> optionalUser=profileRepository.findUserByEmail(email);
        if(optionalUser.isEmpty())throw new UserNotFountException("user not exists please signup");
        User user=optionalUser.get();
        
		if(!BCrypt.checkpw(password, user.getPassword()))return 0;
        setCookie(user.getId());
        
        return user.getId();

        
        
        

    }
    private int  validateCredentialByNumber(String number, String password){
        Optional<User> optionalUser=profileRepository.findUserByNumber(number);
        if(optionalUser.isEmpty())throw new UserNotFountException("user not exists please signup");
        User user=optionalUser.get();

        if(!BCrypt.checkpw(password, user.getPassword()))return 0;
        	
       setCookie(user.getId());
        


        return user.getId();
    }
    private void setCookie(int userId){
        HttpServletResponse res = ServletActionContext.getResponse();
        System.out.println("user id:"+userId);
        Cookie cookie = new Cookie("userId", String.valueOf(userId));
        cookie.setMaxAge(60 * 60);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);        
        cookie.setPath("/");    
        res.addCookie(cookie);
    }

    public void setProfileRepository(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }
    
    
    
}
