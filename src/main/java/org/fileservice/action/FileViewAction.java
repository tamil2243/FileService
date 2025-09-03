package org.fileservice.action;

import java.util.List;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.dto.FileViewResponseDTO;
import org.fileservice.model.FileMeta;
import org.fileservice.service.FileService;



public class FileViewAction extends ActionSupport{


    private FileViewResponseDTO response;
    private FileService fileService;



    @Override
    public String execute(){
        System.out.println("Entered in FileViewAction");
        

        try {
            List<FileMeta> listOfFiles=fileService.getAllFiles();
            response=new FileViewResponseDTO(true,"successfully file details fetched");
            response.setListOfFiles(listOfFiles);
            return "success";

        } 
        catch (UnAuthorizedUserException e) {
            response=new FileViewResponseDTO(false,e.getMessage());
            return "error";
        }catch (Exception e) {
            response=new FileViewResponseDTO(false,"SomeThing went wrong");
            return "error";
        }


        
    }

    public FileViewResponseDTO getResponse(){
        return this.response;
    }
    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
    
}
