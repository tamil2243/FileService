package org.fileservice.action;
import java.io.InputStream;
import java.util.Optional;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.fileservice.dao.FileDAO;
import org.fileservice.dto.DBFIleDownloadResponseDTO;
import org.fileservice.dto.FileDownloadResponseDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class FileDownloadAction extends ActionSupport{
    private InputStream fileInputStream; 
    private String fileName;             
    private String contentType;          
    private int userId;  
    private int fileId;                   
    private final FileDAO fileDAO=new FileDAO();
    private FileDownloadResponseDTO response;

    @Override
    public String execute() {
        System.out.println("Entered in Download");
        System.out.println("file id:"+fileId);
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
            response=new FileDownloadResponseDTO(false,"please signin");
            return "error";
        }

        try {
            Optional<DBFIleDownloadResponseDTO> optional=fileDAO.fileDownload(fileId);
            if(optional.isEmpty()){
                response=new FileDownloadResponseDTO(false,"file not found");
                return "error";
            }
            DBFIleDownloadResponseDTO dpResponse=optional.get();
            fileInputStream=dpResponse.getFileInputStream();
            fileName=dpResponse.getFileName();
            contentType=dpResponse.getContentType();

            response=new FileDownloadResponseDTO(true,"file retrived");
            return "success";




        } catch (Exception e) {

            response=new FileDownloadResponseDTO(false,e.getMessage());
            return "error";
        }
        
    }

    // --- Getters for Struts stream result ---
    public InputStream getFileInputStream() { return fileInputStream; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }

    public FileDownloadResponseDTO getResponse(){
        return response;
    }

  
    public void setFileId(int fileId) { this.fileId = fileId; }
}
