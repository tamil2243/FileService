package org.fileservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.fileservice.model.User;



public class SigninDAO {
    
    User user;


    public Optional<User> findUserByEmail(String email){


        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select id,name,email,number,password from user_details where email=?");){
            
            ps.setString(1, email);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getLong("number"));
                user.setPassword(rs.getString("password"));


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            return Optional.ofNullable(user);
        }
    }
    public Optional<User> findUserByNumber(long number){


        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select id,name,email,number,password from user_details where number=?");){
            
            ps.setLong(1, number);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getLong("number"));
                user.setPassword(rs.getString("password"));


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            return Optional.ofNullable(user);
        }
    }
}
