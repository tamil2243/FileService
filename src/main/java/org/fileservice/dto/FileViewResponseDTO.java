package org.fileservice.dto;

public class FileViewResponseDTO extends ResponseDTO{

    public FileViewResponseDTO() {
    }


    public FileViewResponseDTO(boolean status, String message){
        super(status,message);
    }
    
}
