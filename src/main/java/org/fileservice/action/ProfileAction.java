package org.fileservice.action;

import org.apache.struts2.ServletActionContext;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.ProfileResponseDTO;
import org.fileservice.dto.UserDTO;
import org.fileservice.model.User;
import org.fileservice.service.ProfileService;

import jakarta.servlet.http.HttpServletRequest;

public class ProfileAction {


    private int id;
	private String email;
	private String name;
    private String number;
    private ProfileService profileService;
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
	
	

	
	private String doGet(){
		System.out.println("Entered in get");
		
        try {
            User user=profileService.getDetails();
            response=new ProfileResponseDTO(true,"User exist details feched");
            response.setUser(new UserDTO(user));
            return "success";
        } 
        catch(UnAuthorizedUserException | UserNotFountException e){
            response=new ProfileResponseDTO(false,e.getMessage());
            return "error";
        }
        catch (Exception e) {
            response=new ProfileResponseDTO(false,"Something went wrong during getDetails");
            return "error";
        }
        
		
	}
	
	public String doPut(){
		System.out.println("entered in put");
        System.out.println("name :"+name);
        System.out.println("number :"+number);
        System.out.println("email :"+email);

        try {
            profileService.updateDetails(number, name);
            response=new ProfileResponseDTO(true,"successfully updated");
            return "success";
        }catch(UnAuthorizedUserException | UpdateFailedException | NullPointerException e){
            response=new ProfileResponseDTO(false,e.getMessage());
            return "error";
        } 
        catch (Exception e) {
            response=new ProfileResponseDTO(false,"Something went wrong during updation");
            return "error";
        }
		
		

		
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
    
    public ProfileResponseDTO getResponse(){
        return this.response;
    }



    public void setProfileService(ProfileService profileService){
        this.profileService=profileService;
    }
    
}
