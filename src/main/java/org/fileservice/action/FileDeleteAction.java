package org.fileservice.action;

import java.util.List;

import org.apache.struts2.ActionSupport;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.dto.FileDeleteResponseDTO;
import org.fileservice.model.FileMeta;
import org.fileservice.service.FileService;

public class FileDeleteAction extends ActionSupport{
    
    private int fileId;
    private FileDeleteResponseDTO response;
    private FileService fileService;

    public String execute(){


        System.out.println("Entered in FileDeletAction");
      
       



        try {
            
            List<FileMeta> listOfFiles=fileService.deleteFile(fileId);
            response=new FileDeleteResponseDTO(true,"File Successfully deleted from db");
            response.setListOfFiles(listOfFiles);
            return "success";

        }
        catch(UnAuthorizedUserException | UpdateFailedException e){
            response=new FileDeleteResponseDTO(false,e.getMessage());
            return "error";
        }
        catch (Exception e) {
            response=new FileDeleteResponseDTO(false,"something went wrong");
            return "error";
        }
    }
    

    public FileDeleteResponseDTO getResponse(){
        return this.response;
    }
    public void setFileId(int fileId){
        this.fileId=fileId;
    }

    public void setFileService(FileService fileService){
        this.fileService=fileService;
    }
}
