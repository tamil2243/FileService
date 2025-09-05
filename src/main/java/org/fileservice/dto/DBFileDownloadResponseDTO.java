package org.fileservice.dto;
import java.io.InputStream;

public class DBFileDownloadResponseDTO {
    private InputStream fileInputStream; 
    private String fileName;             
    private String contentType; 



    public InputStream getFileInputStream() { return fileInputStream; }
    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }


    public void setFileInputStream(InputStream fileInputStream){
        this.fileInputStream=fileInputStream;
    }
    public void setFileName(String fileName){
        this.fileName=fileName;
    }
    public void setContentType(String contentType){
        this.contentType=contentType;
    }
}
