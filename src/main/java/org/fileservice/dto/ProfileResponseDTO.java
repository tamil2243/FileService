package org.fileservice.dto;

public class ProfileResponseDTO extends ResponseDTO{

    
    private UserDTO user;

    public ProfileResponseDTO() {
    }


    public ProfileResponseDTO(boolean status, String message){
        super(status,message);
    }

    

    public void setUser(UserDTO user){
        this.user=user;
    }
    public UserDTO getUser(){
        return this.user;
    }
    
}
