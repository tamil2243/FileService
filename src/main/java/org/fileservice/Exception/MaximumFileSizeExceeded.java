package org.fileservice.Exception;

public class MaximumFileSizeExceeded extends RuntimeException{

    public MaximumFileSizeExceeded(String message){
        super(message);
    }
    
}
