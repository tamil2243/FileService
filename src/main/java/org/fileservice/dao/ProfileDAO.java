package org.fileservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.fileservice.model.User;

public class ProfileDAO {
    


    User user;


    public Optional<User> findUserById(int id){


        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select * from user_details where id=?");){
            
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getLong("number"));
                


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            return Optional.ofNullable(user);
        }
    }
    public Optional<User> findUserByEmail(String email){


        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select * from user_details where email=?");){
            
            ps.setString(1, email);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getLong("number"));
                


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            return Optional.ofNullable(user);
        }
    }
    
    public void changeUserName(int id, String name) throws Exception{
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set name=? where id=?");){
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void changeUserNumber(int id, long number)throws Exception{
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set number=? where id=?");){
            
            ps.setLong(1, number);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void changeUserPassword(int id, String password)throws Exception{
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set password=? where id=?");){

            ps.setString(1, password);
            ps.setInt(2, id);
            ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
}
