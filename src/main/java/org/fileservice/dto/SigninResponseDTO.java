package org.fileservice.dto;

public class SigninResponseDTO extends ResponseDTO{

    
    private  int userId;

    public SigninResponseDTO() {
    }


    public SigninResponseDTO(boolean status, String message){
        super(status,message);
    }
    
   public void setUserId(int userId){
    this.userId=userId;
   }

   public int getUserId(){
    return this.userId;
   }
    
}
