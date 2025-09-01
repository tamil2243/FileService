package org.fileservice.dto;

public class FileUploadResponseDTO extends ResponseDTO{

    public FileUploadResponseDTO(){}

    public FileUploadResponseDTO(boolean status, String message){

        super(status,message);
    }
    
}
