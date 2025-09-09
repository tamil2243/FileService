package org.fileservice.dto;

import java.util.List;

public class FilePermissionResponseDTO extends ResponseDTO{

    private boolean permission;
    private List<UserDTO> listOfUsers;

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
    

   


    

    public void setListOfUsers(List<UserDTO> listOfUsers){
        this.listOfUsers=listOfUsers;
    }
    
    public List<UserDTO> getListOfUsers(){
        return this.listOfUsers;
    }
}
