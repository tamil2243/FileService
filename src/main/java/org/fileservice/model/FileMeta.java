package org.fileservice.model;

import java.util.List;

public class FileMeta {
    private int fileId;
    private String fileName;
    private String fileType;
    private String filePath;
    private double fileSize;
    private String description;
    private List<String> userList;



    public void setFileId(int fileId){
        this.fileId=fileId;

    }
    public void setFileName(String fileName){
        this.fileName=fileName;
    }
    public void setFileType(String fileType){
        this.fileType=fileType;
    }
    public void setFilePath(String filePath){
        this.filePath=filePath;
    }
    public void setFileSize(double fileSize){
        this.fileSize=fileSize;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setUserList(List<String> useList){
        this.userList=useList;
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
    public String getFilePath(){
        return this.filePath;
    }
    public double getFileSize(){
        return this.fileSize;
    }
    public String getDescription(){
        return this.description;
    }
    public List<String> getUserList(){
        return this.userList;
    }

    
     
    
}
