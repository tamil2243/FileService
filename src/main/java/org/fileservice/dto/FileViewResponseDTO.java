package org.fileservice.dto;

import java.util.List;

import org.fileservice.model.FileMeta;



public class FileViewResponseDTO extends ResponseDTO{
    private List<FileMeta> listOfFiles;

    
    public FileViewResponseDTO(){
        
    }

    public FileViewResponseDTO(boolean status,String message){

        super(status, message);
    }

    public void setListOfFiles(List<FileMeta> listOfFiles){
        this.listOfFiles=listOfFiles;
    }
    

    public List<FileMeta> getListOfFiles(){
        return this.listOfFiles;
    }
    
}
