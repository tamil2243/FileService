package org.fileservice.action;

import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fileservice.dto.SigninResponseDTO;
import org.fileservice.service.SigninService;

public class SigninAction {
   
    private String email;
    private long  number;
    private String password;
    private SigninService service=new SigninService();
    private SigninResponseDTO response;


    public String execute(){

        System.out.println("Entered in signin action");
        System.out.println("email "+email);
        System.out.println("number "+number);
        System.out.println("password "+password);
        try {
            int  userID=service.signinUser(email, number, password);
            	

            response=new SigninResponseDTO();
          
            if(userID!=0){
                response.setMessage("succesffully signed in");
                response.setUserId(userID);
                response.setStatus(true);
                return "success";
            }
            else{
                response.setMessage("enter correct passowrd");
                return "error";
            }

        } catch (Exception e) {
            response=new SigninResponseDTO();
            response.setStatus(false);
            response.setMessage(e.getMessage());
            return "error";
        }
    }





    //setters
    @StrutsParameter
    public void setEmail(String email){
        this.email=email;
    }
    @StrutsParameter
    public void setNumber(long number){
        this.number=number;
    }
    @StrutsParameter
    public void setPassword(String password){

        this.password=password;
    }

    public SigninResponseDTO getResponse(){
        return this.response;
    }
}
