package org.fileservice.service;

import java.util.Optional;

import org.fileservice.Exception.EmailAlredyExistsException;
import org.fileservice.Exception.MobileNumberAlredyExistsException;
import org.fileservice.model.User;
import org.fileservice.repository.ProfileRepository;
import org.mindrot.jbcrypt.BCrypt;

public class SignUpService {
    private  ProfileRepository profileRepository;


    public void registerUser(String name, String number, String email, String password){



        
        if(name==null || name.isEmpty())throw new NullPointerException("name field can't be empty");
        if(password==null || password.isEmpty())throw new NullPointerException("password field can't be empty");
        if(email==null || email.isEmpty())throw new NullPointerException("email field can't be empty");
        if(number==null)throw new NullPointerException("number field can't be empty");


        Optional<User> optionalUser=profileRepository.findUserByNumber(number);
        if(optionalUser.isPresent())throw new MobileNumberAlredyExistsException("Mobile number Alredy exists");
        if(profileRepository.findUserByEmail(email).isPresent())throw new EmailAlredyExistsException("Email ID alredy exists");

        String hashedPassword=BCrypt.hashpw(password, BCrypt.gensalt());
            
        profileRepository.registerUser(name, email, hashedPassword, number);
           
    }
    public void setProfileRepository(ProfileRepository profileRepository){
        this.profileRepository=profileRepository;
    }
    
}
