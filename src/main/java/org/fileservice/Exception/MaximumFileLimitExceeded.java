package org.fileservice.Exception;

public class MaximumFileLimitExceeded extends RuntimeException{

    public MaximumFileLimitExceeded(String message){
        super(message);
    }
    
}
