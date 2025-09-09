package org.fileservice.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import org.apache.struts2.ActionSupport;
import org.apache.struts2.action.UploadedFilesAware;
import org.apache.struts2.dispatcher.multipart.UploadedFile;
import org.apache.struts2.interceptor.parameter.StrutsParameter;
import org.fileservice.Exception.MaximumFileLimitExceeded;
import org.fileservice.Exception.MaximumFileSizeExceeded;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.dto.FileUploadResponseDTO;
import org.fileservice.service.FileService;


public class FileUploadAction extends ActionSupport implements UploadedFilesAware{
    private List<UploadedFile> uploadedFiles;
    private FileService fileService;
    private FileUploadResponseDTO response;
    private String description;

   
    @Override
    public void withUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles=uploadedFiles;
       
    }

    @Override
    public String execute() throws Exception {
        if (uploadedFiles == null || uploadedFiles.isEmpty()) {
            System.out.println("No file uploaded!");
            response=new FileUploadResponseDTO(false,"File Not Found");
            return "error";
        }

     
       
        System.out.println("description :"+description);

       
       try {

            for(UploadedFile uploadedFile:uploadedFiles){

                System.out.println("name"+uploadedFile.getOriginalName());

                File file = (File) uploadedFile.getContent();
                fileService.uploadFile(file, uploadedFile.getOriginalName(), uploadedFile.getContentType(),description);
                
                
            }
            response=new FileUploadResponseDTO(true,"file successfully uploaded");
            return "success";
       } 
       catch (UnAuthorizedUserException | UpdateFailedException | FileNotFoundException |MaximumFileLimitExceeded | MaximumFileSizeExceeded e) {
            response=new FileUploadResponseDTO(false,e.getMessage());
            return "error";
       }
       catch (FileAlreadyExistsException e) {
            response=new FileUploadResponseDTO(false,"File Alredy exists");
            return "error";
       }
       catch (Exception e) {
            response=new FileUploadResponseDTO(false,"Something went wrong");
            e.printStackTrace();
            return "error";
       }
        
        
    }
    

    public FileUploadResponseDTO getResponse(){

        return this.response;
    }
    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }

    @StrutsParameter
    public void setDescription(String description){
        this.description=description;
    }
    
}
