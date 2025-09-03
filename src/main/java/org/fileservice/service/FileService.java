package org.fileservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import org.fileservice.Exception.FileNotFoundException;
import org.fileservice.Exception.UnAuthorizedUserException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.dto.DBFIleDownloadResponseDTO;
import org.fileservice.model.FileMeta;
import org.fileservice.model.User;
import org.fileservice.repository.FileRepository;
import org.fileservice.repository.ProfileRepository;

public class FileService {
    


    private CookieService cookieService;
    private FileRepository fileRepository;
    private ProfileRepository profileRepository;


    public void deleteFile(int fileId){
        int userId=cookieService.getUserId();

        
            
        if(!fileRepository.hasDeletePermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to delete file");
        }

        

        int affectedRowsForPermission=fileRepository.deletePermission(fileId);
        if(affectedRowsForPermission==0)throw new UpdateFailedException("failed to delete a file");
        int affectedRowsForFile=fileRepository.deleteFile(fileId);
        if(affectedRowsForFile==0)throw new UpdateFailedException("failed to delete a file");

        

        
    }
    public void uploadFile(File file,String fileName,String contentType)throws java.io.FileNotFoundException, UpdateFailedException, Exception{
        int userId=cookieService.getUserId();


        
        FileInputStream fis = new FileInputStream(file);
        fileRepository.uploadFile(fileName, contentType, fis, file,userId);
        
        
        
        
       
    }
    public DBFIleDownloadResponseDTO downloadFile(int fileId){

        int userId=cookieService.getUserId();


        
            
        if(!fileRepository.hasDownloadPermission(userId, fileId)){
            throw new UnAuthorizedUserException("user haven't access to download file");
        }

        Optional<DBFIleDownloadResponseDTO> optional=fileRepository.getFileForDownload(fileId);
        if(optional.isEmpty()){
            throw new FileNotFoundException("File not found for this fileId :"+fileId);
        }
        DBFIleDownloadResponseDTO dpResponse=optional.get();
        return dpResponse;
     
    }
    public void setPermission(int fileId,String email, boolean download, boolean delete){
        
        int userId=cookieService.getUserId();
        if(!fileRepository.hasDeletePermission(userId, fileId)){
            throw new UnAuthorizedUserException("you haven't access to modify  permissions");
            
        }

        Optional<User> optional=profileRepository.findUserByEmail(email);
        if(optional.isEmpty()){
            throw new UserNotFountException("user not found");
        }
        User user=optional.get();

        boolean isUserPermissionExists=fileRepository.isUserPermissionExists(user.getId(), fileId);
        int affectedRows=0;
        if(isUserPermissionExists){

            affectedRows=fileRepository.updatePermission(user.getId(), fileId, download, delete);
        }
        else{
            affectedRows=fileRepository.addPermission(user.getId(), fileId, download, delete);
        }
        if(affectedRows==0){
            throw new UpdateFailedException("failed to setPermission");
        }
            

        
    }
    public List<FileMeta> getAllFiles(){
        int userId=cookieService.getUserId();

        
        return fileRepository.listAllFiles();
   
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
