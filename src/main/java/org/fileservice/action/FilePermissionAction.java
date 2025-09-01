package org.fileservice.action;


import java.util.Optional;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.fileservice.dao.FileDAO;
import org.fileservice.dao.ProfileDAO;
import org.fileservice.dto.FilePermissionResponseDTO;
import org.fileservice.model.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FilePermissionAction extends ActionSupport{
    private int userId;
    private int fileId;
    private String email;
    private boolean download;
    private boolean delete;
    private FilePermissionResponseDTO response;
    private FileDAO fileDAO=new FileDAO();
    private ProfileDAO profileDAO=new ProfileDAO();
    
    public String canDownload(){
        System.out.println("Entered in canDownload");
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();

		if(cookies!=null) {
				for(Cookie c : cookies) {
					if("userId".equals(c.getName())) {
							System.out.println("Found userId: " + c.getValue());
							userId= Integer.parseInt(c.getValue());
					}
				}
		}
        if(userId==0){
            response=new FilePermissionResponseDTO(false,"please signin");
            return "error";
        }

        try {
            boolean can_download=fileDAO.hasDownloadPermission(userId,fileId);
            response=new FilePermissionResponseDTO(true,"successfully file permission fetched");
            response.setPermission(can_download);        
            return "success";

        } catch (Exception e) {
            response=new FilePermissionResponseDTO(false,e.getMessage());
            return "error";
        }


        
    }
    public String canDelete(){
        System.out.println("Entered in canDelete");
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();

		if(cookies!=null) {
				for(Cookie c : cookies) {
					if("userId".equals(c.getName())) {
							System.out.println("Found userId: " + c.getValue());
							userId= Integer.parseInt(c.getValue());
					}
				}
		}
        if(userId==0){
            response=new FilePermissionResponseDTO(false,"please signin");
            return "error";
        }

        try {
            boolean can_delete=fileDAO.hasDeletePermission(userId,fileId);
            response=new FilePermissionResponseDTO(true,"successfully file permission fetched");
            response.setPermission(can_delete);
            return "success";

        } catch (Exception e) {
            response=new FilePermissionResponseDTO(false,e.getMessage());
            return "error";
        }


        
    }
    public String setPermission(){
        System.out.println("Entered in setPermission");
        System.out.println("email "+email+" fileId "+fileId);
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();

		if(cookies!=null) {
				for(Cookie c : cookies) {
					if("userId".equals(c.getName())) {
							System.out.println("Found userId: " + c.getValue());
							userId= Integer.parseInt(c.getValue());
					}
				}
		}
        if(userId==0){
            response=new FilePermissionResponseDTO(false,"please signin");
            return "error";
        }

        try {
            Optional<User> optional=profileDAO.findUserByEmail(email);
            if(optional.isEmpty()){
                response=new FilePermissionResponseDTO(false,"user not found");
                return "error";
            }
            User user=optional.get();

            boolean userAndFileAlredyMapped=fileDAO.alredyUserPresent(user.getId(), fileId);
            if(userAndFileAlredyMapped){

                fileDAO.modifyPermission(user.getId(), fileId, download, delete);
            }
            else{
                fileDAO.setPermission(user.getId(), fileId, download, delete);
            }
            response=new FilePermissionResponseDTO(true,"permission has been changed");
            return "success";

        } catch (Exception e) {
            response=new FilePermissionResponseDTO(false,e.getMessage());
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
    
}
