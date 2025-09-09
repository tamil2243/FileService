package org.fileservice.action;

import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fileservice.Exception.PasswordMismachException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.SigninResponseDTO;
import org.fileservice.service.SigninService;

public class SigninAction {
   
    private String email;
    private String  number;
    private String password;
    private SigninService signinService;
    private SigninResponseDTO response;


    public String execute(){

     
        try {
            int  userID=signinService.signinUser(email, number, password);
            response=new SigninResponseDTO(true,"succesffully signed in");  
            response.setUserId(userID);
            return "success";

        }catch(UserNotFountException | NullPointerException | PasswordMismachException e){
            response=new SigninResponseDTO(false,e.getMessage());
            return "error";
        } 
        catch (Exception e) {
            response=new SigninResponseDTO(false,"Something went wrong durning signin");
            return "error";
        }
    }





    //setters
    @StrutsParameter
    public void setEmail(String email){
        this.email=email;
    }
    @StrutsParameter
    public void setNumber(String number){
        this.number=number;
    }
    @StrutsParameter
    public void setPassword(String password){

        this.password=password;
    }

    public SigninResponseDTO getResponse(){
        return this.response;
    }

    public void setSigninService(SigninService signinService){
        this.signinService=signinService;
    }
}
