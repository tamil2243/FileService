package org.fileservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import org.fileservice.Exception.FileNotFoundException;
import org.fileservice.Exception.MaximumFileLimitExceeded;
import org.fileservice.Exception.MaximumFileSizeExceeded;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.DBFileDownloadResponseDTO;
import org.fileservice.model.FileMeta;
import org.fileservice.model.User;
import org.fileservice.repository.FileRepository;
import org.fileservice.repository.ProfileRepository;

public class FileService {
    

    private static final String UPLOAD_DIR = "/home/tamil-inc5570/fileStorage/";
    private CookieService cookieService;
    private FileRepository fileRepository;
    private ProfileRepository profileRepository;


    public List<FileMeta> deleteFile(int fileId){
        int userId=cookieService.getUserId();

        
            
        if(!fileRepository.hasDeletePermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to delete file");
        }
        Optional<FileMeta> optional=fileRepository.getFileMetaDetails(fileId);
        if(optional.isEmpty()){
            throw new FileNotFoundException("File not found for this fileId :"+fileId);
        }
        FileMeta fileMeta=optional.get();
        File file = new File(fileMeta.getFilePath());
        int affectedRowsForFile=fileRepository.deleteFile(fileId);
        if(affectedRowsForFile==0)throw new UpdateFailedException("failed to delete a file");
        // 2. Delete file from storage
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new UpdateFailedException("Could not delete file from storage: " + fileMeta.getFilePath());
            }
        }

        return fileRepository.getAllFiles(userId);


       

        

        
    }
    public void uploadFile(File file,String fileName,String contentType, String description)throws UpdateFailedException,UnAuthorizedUserException, MaximumFileLimitExceeded,MaximumFileSizeExceeded,Exception{
        int userId=cookieService.getUserId();
        System.out.println("entered in file service");
        File targetFile = new File(UPLOAD_DIR + userId+"_"+fileName);
        long sizeInBytes = file.length();
        double sizeInMB = (double) sizeInBytes / 1024;
        // Copy uploaded file to target location
       
        
        
     
        
        try {
            Files.copy(file.toPath(), targetFile.toPath());
            String path= targetFile.getAbsolutePath();
            fileRepository.uploadFile(fileName, contentType, sizeInMB, path,userId,description);
        }catch(UpdateFailedException | MaximumFileLimitExceeded |MaximumFileSizeExceeded e){
           if (targetFile.exists()) {
                targetFile.delete();
            
            }
            throw e; 
        }
        catch (Exception e) {
           if (targetFile.exists()) {
                targetFile.delete();
            
            }
            throw e;
        }
  
    }


    public DBFileDownloadResponseDTO downloadFile(int fileId){

        int userId=cookieService.getUserId();

        if(!fileRepository.hasDownloadPermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to download file");
        }

        Optional<FileMeta> optional=fileRepository.getFileMetaDetails(fileId);
        if(optional.isEmpty()){
            throw new FileNotFoundException("File not found for this fileId :"+fileId);
        }
        FileMeta fileMeta=optional.get();

         File file = new File(fileMeta.getFilePath());  

            

            try {
                InputStream inputStream = new FileInputStream(file);

                DBFileDownloadResponseDTO dto = new DBFileDownloadResponseDTO();
                dto.setFileInputStream(inputStream);
                dto.setFileName(fileMeta.getFileName());
                dto.setContentType(fileMeta.getFileType()); 
                return dto;
            } catch (java.io.FileNotFoundException e) {
                throw new FileNotFoundException("Stored file is missing at path: " +fileMeta.getFilePath());
            }
        // return dto;
     
    }
    public DBFileDownloadResponseDTO viewFile(int fileId){

        int userId=cookieService.getUserId();

        if(!fileRepository.hasViewPermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to view file");
        }

        Optional<FileMeta> optional=fileRepository.getFileMetaDetails(fileId);
        if(optional.isEmpty()){
            throw new FileNotFoundException("File not found for this fileId :"+fileId);
        }
        FileMeta fileMeta=optional.get();

         File file = new File(fileMeta.getFilePath());  

            

            try {
                InputStream inputStream = new FileInputStream(file);

                DBFileDownloadResponseDTO dto = new DBFileDownloadResponseDTO();
                dto.setFileInputStream(inputStream);
                dto.setFileName(fileMeta.getFileName());
                dto.setContentType(fileMeta.getFileType()); 
                return dto;
            } catch (java.io.FileNotFoundException e) {
                throw new FileNotFoundException("Stored file is missing at path: " +fileMeta.getFilePath());
            }
        // return dto;
     
    }
    

    public void setPermission(int fileId,String email, boolean download, boolean delete)throws UnAuthorizedUserException,UserNotFountException,UpdateFailedException, Exception{
        
        int userId=cookieService.getUserId();
        if(!fileRepository.hasSharePermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to modify  permissions");
            
        }

        Optional<User> optional=profileRepository.findUserByEmail(email);
        if(optional.isEmpty()){
            throw new UserNotFountException("user not found");
        }
        User user=optional.get();
        int permision=0;
        // getPermission val Download
        if(download){
            permision|=fileRepository.getPermissionValue("Download");
        }
        if(delete){
            permision|=fileRepository.getPermissionValue("Delete");
        }
        
        boolean isUserPermissionExists=fileRepository.isUserPermissionExists(user.getId(), fileId);
        int affectedRows;
        if(isUserPermissionExists){
            System.out.println("Goes into update permission method");
            affectedRows=fileRepository.updatePermission(user.getId(), fileId, permision);
        }
        else{
            System.out.println("Goes into add permission method");
            affectedRows=fileRepository.addPermission(user.getId(), fileId, permision);
        }
        if(affectedRows==0){
            throw new UpdateFailedException("failed to setPermission");
        }
            

        
    }
    public List<FileMeta> getAllFiles(){

        int userId=cookieService.getUserId();

        List<FileMeta> listOFileMetas= fileRepository.getAllFiles(userId);
        for(FileMeta fileMeta:listOFileMetas){

            fileMeta.setUserList(fileRepository.getUserDetailsUsingFileId(fileMeta.getFileId()));
        }
        return listOFileMetas;
   
    }

    
    public void setCookieService(CookieService cookieService){
        this.cookieService=cookieService;
    }
    public void setProfileRepository(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }
    public void setFileRepository(FileRepository fileRepository){
        this.fileRepository=fileRepository;
    }
    
}
