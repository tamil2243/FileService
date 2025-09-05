package org.fileservice.action;
import java.io.InputStream;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.FileNotFoundException;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.dto.DBFileDownloadResponseDTO;
import org.fileservice.dto.FileDownloadResponseDTO;
import org.fileservice.service.FileService;


public class FileDownloadAction extends ActionSupport{
    private InputStream fileInputStream; 
    private String fileName;             
    private String contentType;            
    private int fileId;                   
    private FileService fileService;
    private FileDownloadResponseDTO response;

    @Override
    public String execute() {
        System.out.println("Entered in Download");
        System.out.println("file id:"+fileId);
        

        try {
            
            
            DBFileDownloadResponseDTO dpResponse=fileService.downloadFile(fileId);
            fileInputStream=dpResponse.getFileInputStream();
            fileName=dpResponse.getFileName();
            contentType=dpResponse.getContentType();

            response=new FileDownloadResponseDTO(true,"file retrived");
            return "success";




        } catch (UnAuthorizedUserException  | FileNotFoundException e) {

            response=new FileDownloadResponseDTO(false,e.getMessage());
            return "error";
        }catch (Exception e) {
            
            response=new FileDownloadResponseDTO(false,"Something went wrong");
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

    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
}
