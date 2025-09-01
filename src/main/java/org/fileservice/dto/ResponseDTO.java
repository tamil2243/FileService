package org.fileservice.dto;

public abstract  class ResponseDTO {

    private boolean status;
    private String message;

     public ResponseDTO() {
    }


    public ResponseDTO(boolean status, String message){
        this.status=status;
        this.message=message;
    }
    
    public void setStatus(boolean status){
        this.status=status;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public boolean getStatus(){
        return this.status;
    }

    public String getMessage(){
        return this.message;
    }
    
}
