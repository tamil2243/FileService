package org.fileservice.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import org.fileservice.model.User;

public class ProfileRepository {
    


    


    public Optional<User> findUserById(int id){
        User user=null;

        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select * from user_details where id=?");){
            
            ps.setInt(1, id);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getString("number"));
                user.setPassword(rs.getString("password"));
                


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.ofNullable(user);
        }
    }
    public Optional<User> findUserByEmail(String email){
        User user=null;

        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select * from user_details where email=?");){
            
            ps.setString(1, email);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getString("number"));
                user.setPassword(rs.getString("password"));
                


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            return Optional.ofNullable(user);
        }
    }
    public Optional<User> findUserByNumber(String number){

        User user=null;
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("select id,name,email,number,password from user_details where number=?");){
            
            ps.setString(1, number);
            ResultSet rs=ps.executeQuery();

            if(rs.next()){
                
                user=new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setNumber(rs.getString("number"));
                user.setPassword(rs.getString("password"));


            }
            return Optional.ofNullable(user);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.ofNullable(user);
        }
    }
    public int changeUserName(int id, String name){
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set name=? where id=?");){
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate();
            
        } catch (Exception e) {
            
            e.printStackTrace();
            return 0;
            
        }
    }
    public int updatePhoneNumber(int id, String number){
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set number=? where id=?");){
            
            ps.setString(1, number);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
            
        }
    }
    public int  updatePassword(int id, String password){
        try (Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("update user_details set password=? where id=?");){

            ps.setString(1, password);
            ps.setInt(2, id);
            return ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        return 0;
    }
    public void registerUser(String name,String email,String password,String number){

        try(Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("insert into user_details(name,email,password,number) values(?,?,?,?)");){


            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, number);

            ps.executeUpdate();


        }catch(Exception e){
            e.printStackTrace();
            
        }

    }
}
