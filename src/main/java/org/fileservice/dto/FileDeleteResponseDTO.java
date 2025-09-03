package org.fileservice.dto;

public class FileDeleteResponseDTO extends ResponseDTO{

    public FileDeleteResponseDTO(){}

    public FileDeleteResponseDTO(boolean status, String message){
        super(status,message);
    }
    
}
