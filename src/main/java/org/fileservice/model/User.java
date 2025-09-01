package org.fileservice.model;


public class User{
    

    private int id;
    private String name;
    private String email;
    private long  number;
    private String password;

  

    public String toString(){
        return "id : "+this.id+" name : "+this.name+" email : "+this.email+" number : "+this.number;
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
    public void setNumber(long number){
        this.number=number;
    }
    public void setPassword(String password){

        this.password=password;
    }

    public int getId(){
        return this.id;
    }
    public long getNumber(){
        return this.number;
    }
    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }
    public String getPassword(){
        return this.password;
    }
}
