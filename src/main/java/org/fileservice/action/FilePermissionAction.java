package org.fileservice.action;


import java.util.List;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.FilePermissionResponseDTO;
import org.fileservice.dto.UserDTO;
import org.fileservice.service.FileService;

public class FilePermissionAction extends ActionSupport{
   
    private int fileId;
    private String email;
    private boolean download;
    private boolean delete;
    private List<UserDTO> listOfUsers;
    private FilePermissionResponseDTO response;
    private FileService fileService;;
    
    
   
    public String setPermission(){
        System.out.println("Entered in setPermission");
      
        

        try {
            
            fileService.setPermission(fileId, email, download, delete);
            
            response=new FilePermissionResponseDTO(true,"permission has been changed");
            return "success";

        } catch (UnAuthorizedUserException | UpdateFailedException | UserNotFountException e) {
            response=new FilePermissionResponseDTO(false,e.getMessage());
            return "error";
        }
        catch(Exception e){
            response=new FilePermissionResponseDTO(false,"Somthing Went wrong");
            return "error";
        }


        
    }

    public String getNonViewAccessUsers(){


        try {
            
            List<UserDTO> listOfUsers=fileService.getNonViewAccessUsers(fileId);
            response=new FilePermissionResponseDTO(true,"success");
            response.setListOfUsers(listOfUsers);
            return "success";
        } catch (Exception e) {
            response=new FilePermissionResponseDTO(false,"something went wrong");
            return "error";
        }


        
    } 
    public String setViewAccessListOfUsers(){

        if(listOfUsers==null)System.out.println("list user is null");
        else System.out.println(listOfUsers.size());
        try {
            fileService.setViewAccessListOfUsers(fileId,listOfUsers);
            
            response=new FilePermissionResponseDTO(true,"successfully permission changed list of users");
            response.setListOfUsers(fileService.getNonViewAccessUsers(fileId));
            
            return "success";
        } 
        catch (UnAuthorizedUserException | UpdateFailedException | UserNotFountException e) {
            response=new FilePermissionResponseDTO(false,e.getMessage());
            return "error";
        }catch (Exception e) {
            response=new FilePermissionResponseDTO(false,"something went wrong");
            return "error";
        }
    }

    public FilePermissionResponseDTO getResponse(){
        return this.response;
    }
    public void setFileId(int fileId){
        this.fileId=fileId;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setDownload(boolean download){
        this.download=download;
    }
    public void setDelete(boolean delete){
        this.delete=delete;
    }
    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
    public void setListOfUsers(List<UserDTO> listOfUsers){
       
        this.listOfUsers = listOfUsers;
    }

    
}
