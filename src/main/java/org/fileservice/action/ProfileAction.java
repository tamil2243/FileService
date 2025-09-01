package org.fileservice.action;

import java.util.Optional;

import org.apache.struts2.ServletActionContext;
import org.fileservice.dao.ProfileDAO;
import org.fileservice.dto.ProfileResponseDTO;
import org.fileservice.dto.UserDTO;
import org.fileservice.model.User;

import jakarta.servlet.http.HttpServletRequest;

public class ProfileAction {


    private int id;
	private String email;
	private String name;
    private long number;
    private ProfileDAO profileDAO=new ProfileDAO();
    private ProfileResponseDTO response;
	public String executeGetPut(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String method = request.getMethod();
        System.out.println("Request method: " + method);
		
		switch (method) {
			case "GET":
				return doGet();
			case "PUT":
				return doPut();
			default:{
				return "success";    
			}
		}
		


	}
	public String executeGetPost(){

		HttpServletRequest request = ServletActionContext.getRequest();
		String method = request.getMethod();
        System.out.println("Request method: " + method);
		System.out.println("idVal :"+id);
		
		switch (method) {
			case "GET":
				return doGetAll();
			case "POST":
				return doPost();
			default:{
				return "success";    
			}
		}

		


	}
	

	public  String doGetAll(){
		
		System.out.println("entered in getAll");
		
	

		return "success";
	}
	private String doGet(){
		System.out.println("Entered in get");
		HttpServletRequest request = ServletActionContext.getRequest();
		String url = request.getRequestURL().toString();
		id=Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
		System.out.println("id :"+ id);
		Optional<User> optional=profileDAO.findUserById(id);

        if(optional.isEmpty()){
            response=new ProfileResponseDTO();
            response.setStatus(false);
            response.setMessage("user not exist");
            return "error";
        }
        response=new ProfileResponseDTO();
        response.setStatus(true);
        response.setMessage("user exist");
        response.setUser(new UserDTO(optional.get()));
		return "success";
		
	}
	private String doPost(){
		System.out.println("entered in post");
		
		return "success";
		
	}
	public String doPut(){
		System.out.println("entered in put");
		if(name!=null){	

			try {
                profileDAO.changeUserName(id, name);
                response=new ProfileResponseDTO(true,"successfully updated");
                return "success";
            } catch (Exception e) {
                response=new ProfileResponseDTO(false,e.getMessage());
                return "error";
            }
		}
		if(number!=0){

			try {
                profileDAO.changeUserNumber(id, number);
                response=new ProfileResponseDTO(true,"successfully updated");
                return "success";
                
            } catch (Exception e) {
                response=new ProfileResponseDTO(false,e.getMessage());
                return "error";
            }
		}


        response=new ProfileResponseDTO(false,"can't update using empty value");
        return "error";
		

		
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
    
    public ProfileResponseDTO getResponse(){
        return this.response;
    }
    
}
