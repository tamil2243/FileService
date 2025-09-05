package org.fileservice.dto;

import java.util.List;

import org.fileservice.model.FileMeta;

public class FileDeleteResponseDTO extends ResponseDTO{
    private List<FileMeta> listOfFiles;
    public FileDeleteResponseDTO(){}

    public FileDeleteResponseDTO(boolean status, String message){
        super(status,message);
    }

     public void setListOfFiles(List<FileMeta> listOfFiles){
        this.listOfFiles=listOfFiles;
    }
    

    public List<FileMeta> getListOfFiles(){
        return this.listOfFiles;
    }
    

    
}
