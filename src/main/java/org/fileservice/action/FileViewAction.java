package org.fileservice.action;

import java.util.List;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.fileservice.dao.FileDAO;
import org.fileservice.dto.FileViewResponseDTO;
import org.fileservice.model.FileMeta;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FileViewAction extends ActionSupport{


    private int userId;
    private FileViewResponseDTO response;
    private FileDAO fileDAO=new FileDAO();



    @Override
    public String execute(){
        System.out.println("Entered in FileViewAction");
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
            response=new FileViewResponseDTO(false,"please signin");
            return "error";
        }

        try {
            List<FileMeta> listOfFiles=fileDAO.getFileDetails();
            response=new FileViewResponseDTO(true,"successfully file details fetched");
            response.setListOfFiles(listOfFiles);
            return "success";

        } catch (Exception e) {
            response=new FileViewResponseDTO(false,e.getMessage());
            return "error";
        }


        
    }

    public FileViewResponseDTO getResponse(){
        return this.response;
    }
    
}
