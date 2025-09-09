package org.fileservice.service;

import java.util.Optional;

import org.fileservice.Exception.PasswordMismachException;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.Exception.UserNotFountException;
import org.fileservice.model.User;
import org.fileservice.repository.ProfileRepository;
import org.mindrot.jbcrypt.BCrypt;

public class ProfileService {
    

    private ProfileRepository profileRepository;
    private CookieService cookieService;

    public void updateDetails(String number,String name){


        int id=cookieService.getUserId();
		if(name!=null){	

		
            int affectedRows=profileRepository.changeUserName(id, name);
            if(affectedRows==0)throw new UpdateFailedException("failed to update name");
                
            
		}
		if(number!=null){

			
            int affectedRows=profileRepository.updatePhoneNumber(id, number);
            if(affectedRows==0)throw new UpdateFailedException("failed to update number");
              
		}


        throw new NullPointerException("can't update using empty value");
        
		


    }
    public User getDetails(){
        int userId=cookieService.getUserId();
        
        
        Optional<User> optional=profileRepository.findUserById(userId);
        if(optional.isEmpty()){
            throw new UserNotFountException("user not found");
        }
        return optional.get();
        
    }
    public void setProfileRepository(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }
    public void setCookieService(CookieService cookieService){
        this.cookieService=cookieService;
    }

    public void changePassword(String currentPassword, String newPassword){
        

            int userId=cookieService.getUserId();

            // first check current password is currect

            Optional<User> optional=profileRepository.findUserById(userId);
            if(optional.isEmpty())throw new UserNotFountException("user not found");
            User user=optional.get();
            if(!BCrypt.checkpw(currentPassword, user.getPassword())){
                throw new PasswordMismachException("current password incorrect");
            }
            String hashedPassword=BCrypt.hashpw(newPassword, BCrypt.gensalt());
            int numberOfAffectedRows=profileRepository.updatePassword(userId,hashedPassword);

            if(numberOfAffectedRows==0)throw new UpdateFailedException("Failed to update password for userId: "+ userId);
            
            
        
    }
}
