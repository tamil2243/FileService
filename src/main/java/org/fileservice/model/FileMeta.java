package org.fileservice.model;

public class FileMeta {
    private int fileId;
    private String fileName;
    private String fileType;
   



    public void setFileId(int fileId){
        this.fileId=fileId;

    }
    public void setFileName(String fileName){
        this.fileName=fileName;
    }
    public void setFileType(String fileType){
        this.fileType=fileType;
    }
    


    public int getFileId(){
        return this.fileId;
    }
    public String getFileName(){
        return this.fileName;
    }
    public String getFileType(){
        return this.fileType;
    }
    
     
    
}
