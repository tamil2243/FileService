package org.fileservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class SignupDAO {
    
    
    public void registerUser(String name,String email,String password,long number) throws Exception{

        try(Connection con=DBConnection.getConnection();
        PreparedStatement ps=con.prepareStatement("insert into user_details(name,email,password,number) values(?,?,?,?)");){


            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setLong(4, number);

            ps.executeUpdate();


        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
