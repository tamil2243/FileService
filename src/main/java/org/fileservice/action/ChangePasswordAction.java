package org.fileservice.action;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.PasswordMismachException;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.ChangePasswordResponseDTO;
import org.fileservice.service.ChangePasswordService;

public class ChangePasswordAction extends ActionSupport{



    private String currentPassword;
    private String newPassword;
    private ChangePasswordResponseDTO response;
    private ChangePasswordService changePasswordService;

    public String execute(){

        System.out.println("Entered in ChangePasswordAction");
        
       
        try {
            

            changePasswordService.changePassword(currentPassword, newPassword);
            response=new ChangePasswordResponseDTO(true,"password changed");
            return "success";

        } catch(UserNotFountException | UnAuthorizedUserException | PasswordMismachException  | UpdateFailedException e){
            response=new ChangePasswordResponseDTO(false,e.getMessage());
            return "error";
        }
        catch (Exception e) {
            response=new ChangePasswordResponseDTO(false,"Something went wrong");
            return "error";
        }
    }

    public  void setCurrentPassword(String currentPassword){
        this.currentPassword=currentPassword;
    }
    public  void setNewPassword(String newPassword){
        this.newPassword=newPassword;
    }
    public ChangePasswordResponseDTO getResponse(){
        return this.response;
    }

    public void setChangePasswordService(ChangePasswordService changePasswordService){
        this.changePasswordService=changePasswordService;
    }
    
}
