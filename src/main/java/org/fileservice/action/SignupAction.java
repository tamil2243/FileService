package org.fileservice.action;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fileservice.Exception.EmailAlredyExistsException;
import org.fileservice.Exception.MobileNumberAlredyExistsException;
import org.fileservice.dto.SignupResponseDTO;
import org.fileservice.service.SignUpService;


public class SignupAction extends ActionSupport{



    private String name;
    private String email;
    private String  number;
    private String password;
    private SignupResponseDTO response;
    private SignUpService signUpService;

    @Override
    public String execute(){
       
        try {

            signUpService.registerUser(name, number, email, password);
            response=new SignupResponseDTO(true,"successfully registered");
            
            return "success";
        } catch(MobileNumberAlredyExistsException | EmailAlredyExistsException  | NullPointerException e){
            response=new SignupResponseDTO(false,e.getMessage());
            return "error";
        }
        catch (Exception e) {
            response=new SignupResponseDTO(false,"somthing went wrong");
           
            return "error";
        }
        
    }

    

    // setters
    @StrutsParameter
    public void setName(String name){
        this.name=name;
    }
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

    public void setSignUpService(SignUpService signUpService){
        this.signUpService=signUpService;
    }


    // getters
    

    public SignupResponseDTO getResponse() {
        return response;
    }

   
    
    
}
