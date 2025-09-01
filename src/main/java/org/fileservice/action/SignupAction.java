package org.fileservice.action;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fileservice.dao.SignupDAO;
import org.fileservice.dto.SignupResponseDTO;

public class SignupAction extends ActionSupport{



    private String name;
    private String email;
    private long  number;
    private String password;
    private SignupResponseDTO response;
    private SignupDAO signupDAO;

    @Override
    public String execute(){
        System.out.println("entered in signup action");
        System.out.println("name "+name);
        System.out.println("email "+email);
        System.out.println("number "+number);
        System.out.println("password "+password);
        try {
            
            signupDAO=new SignupDAO();
            signupDAO.registerUser(name, email, password, number);
            response=new SignupResponseDTO(true,"Successfully registered");
            return "success";
        } catch (Exception e) {
            response=new SignupResponseDTO(false,e.getMessage());
           
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
    public void setNumber(long number){
        this.number=number;
    }
    @StrutsParameter
    public void setPassword(String password){

        this.password=password;
    }


    // getters
    

    public SignupResponseDTO getResponse() {
        return response;
    }

   
    
    
}
