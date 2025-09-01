package org.fileservice.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.action.UploadedFilesAware;
import org.apache.struts2.dispatcher.multipart.UploadedFile;
import org.fileservice.dao.FileDAO;
import org.fileservice.dto.FileUploadResponseDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FileUploadAction extends ActionSupport implements UploadedFilesAware{
    private UploadedFile uploadedFile;
    int userId;   
    private String fileName;
    private String contentType;
    private String originalName;
    private final FileDAO fileDAO=new FileDAO();
    private FileUploadResponseDTO response;

    @Override
    public void withUploadedFiles(List<UploadedFile> uploadedFiles) {
        if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
            this.uploadedFile = uploadedFiles.get(0);
            this.fileName = uploadedFile.getName();
            this.contentType = uploadedFile.getContentType();
            this.originalName = uploadedFile.getOriginalName();
        }
    }

    @Override
    public String execute() throws Exception {
        if (uploadedFile == null) {
            System.out.println("No file uploaded!");
            return "error";
        }

        // Print details
        System.out.println("Original Name: " + originalName);
        System.out.println("Server Temp Name: " + fileName);
        System.out.println("Content Type: " + contentType);
        System.out.println("Size: " + uploadedFile.length());

        // getting userId from cookie

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
            response=new FileUploadResponseDTO(false,"please signin");
            return "error";
        }
        File file = (File) uploadedFile.getContent();
        FileInputStream fis = new FileInputStream(file);
        // byte[] fileData = Files.readAllBytes(file.toPath());

        // --- Store into DB (BLOB) ---
       try {
           fileDAO.uploadFile(originalName, contentType, fis, file,userId);
           response=new FileUploadResponseDTO(true,"file successfully uploaded");
           return "success";
       } catch (Exception e) {
            response=new FileUploadResponseDTO(false,e.getMessage());
            return "error";
       }

        
    }
    


   
    public String getOriginalName() { return originalName; }
    public String getContentType() { return contentType; }
    public String getFileName() { return fileName; }


    public void setUserId(int userId){
        this.userId=userId;
    }
}
