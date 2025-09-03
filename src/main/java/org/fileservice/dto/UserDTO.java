package org.fileservice.dto;

import org.fileservice.model.User;

public class UserDTO {

    private int id;
    private String name;
    private String email;
    private String  number;

    public UserDTO(){}

    public UserDTO(User user){

        this.id=user.getId();
        this.name=user.getName();
        this.number=user.getNumber();
        this.email=user.getEmail();
    }
   
    public void setId(int id){
        this.id=id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setNumber(String number){
        this.number=number;
    }
    

    public int getId(){
        return this.id;
    }
    public String getNumber(){
        return this.number;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    
    
}
