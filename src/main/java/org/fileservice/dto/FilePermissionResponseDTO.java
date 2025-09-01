package org.fileservice.dto;

public class FilePermissionResponseDTO extends ResponseDTO{

    private boolean permission;
    

    public FilePermissionResponseDTO(){}
    public FilePermissionResponseDTO(boolean status,String message){
        super(status,message);
    }

    public void setPermission(boolean permission){
        this.permission=permission;
    }
    

    public boolean getPermission(){
        return this.permission;
    }
    
}
