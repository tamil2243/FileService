package org.fileservice.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.UploadedFilesAware;
import org.apache.struts2.dispatcher.multipart.UploadedFile;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.dto.FileUploadResponseDTO;
import org.fileservice.service.FileService;


public class FileUploadAction extends ActionSupport implements UploadedFilesAware{
    private UploadedFile uploadedFile;
    private String fileName;
    private String contentType;
    private String originalName;
    private FileService fileService;
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
            response=new FileUploadResponseDTO(false,"File Not Found");
            return "error";
        }

        // Print details
        System.out.println("Original Name: " + originalName);
        System.out.println("Server Temp Name: " + fileName);
        System.out.println("Content Type: " + contentType);
        System.out.println("Size: " + uploadedFile.length());

       
       try {
            File file = (File) uploadedFile.getContent();
            fileService.uploadFile(file, originalName, contentType);
            response=new FileUploadResponseDTO(true,"file successfully uploaded");
            return "success";
       } 
       catch (UnAuthorizedUserException | UpdateFailedException | FileNotFoundException e) {
            response=new FileUploadResponseDTO(false,e.getMessage());
            return "error";
       }
       catch (Exception e) {
            response=new FileUploadResponseDTO(false,"Something went wrong");
            return "error";
       }

        
    }
    


   
    public String getOriginalName() { return originalName; }
    public String getContentType() { return contentType; }
    public String getFileName() { return fileName; }


    public FileUploadResponseDTO getResponse(){

        return this.response;
    }
    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
}
