package org.fileservice.action;

import java.io.InputStream;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.FileNotFoundException;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.dto.DBFileDownloadResponseDTO;
import org.fileservice.dto.FileViewResponseDTO;
import org.fileservice.service.FileService;

public class FileViewAction extends ActionSupport{
    private InputStream fileInputStream; 
    private String fileName;             
    private String contentType;            
    private int fileId;                   
    private FileService fileService;
    private FileViewResponseDTO response;



    @Override
    public String execute() {
        System.out.println("Entered in Download");
        System.out.println("file id:"+fileId);
        

        try {
            
            
            DBFileDownloadResponseDTO dpResponse=fileService.viewFile(fileId);
            fileInputStream=dpResponse.getFileInputStream();
            fileName=dpResponse.getFileName();
            contentType=dpResponse.getContentType();

            response=new FileViewResponseDTO(true,"file retrived");
            return "success";




        } catch (UnAuthorizedUserException  | FileNotFoundException e) {

            response=new FileViewResponseDTO(false,e.getMessage());
            return "error";
        }catch (Exception e) {
            
            response=new FileViewResponseDTO(false,"Something went wrong");
            return "error";
        }

        
        
    }

    // --- Getters for Struts stream result ---
    public InputStream getFileInputStream() { return fileInputStream; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }

    public FileViewResponseDTO getResponse(){
        return response;
    }

  
    public void setFileId(int fileId) { this.fileId = fileId; }

    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
}
