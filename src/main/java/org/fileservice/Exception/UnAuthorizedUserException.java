package org.fileservice.Exception;

public class UnAuthorizedUserException extends RuntimeException{


    public UnAuthorizedUserException(String message){
        super(message);
    }
    
}
