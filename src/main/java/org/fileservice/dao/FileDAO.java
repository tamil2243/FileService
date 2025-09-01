package org.fileservice.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fileservice.dto.DBFIleDownloadResponseDTO;
import org.fileservice.model.FileMeta;



public class FileDAO {



    public void uploadFile(String originalName, String contentType, FileInputStream fis, File file, int userId) throws Exception{

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "INSERT INTO files (file_name, file_type, file_data,uploader_id) VALUES (?, ?, ?,?)")) {

            ps.setString(1, originalName);
            ps.setString(2, contentType);
            ps.setBinaryStream(3, fis, (int) file.length()); 
            ps.setInt(4, userId);
            // ps.setBytes(3, fileData);

            ps.executeUpdate();
            System.out.println("File saved into DB successfully!");
            int file_id=getFileId(originalName, userId);
            System.out.println("file id retrived"+file_id);
            setPermission(userId, file_id, true, true);
            System.out.println("file permission are setted");
            
        }
        catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public Optional<DBFIleDownloadResponseDTO> fileDownload(int fileId) throws Exception{
        DBFIleDownloadResponseDTO response=null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT file_name, file_type, file_data FROM files WHERE id=?")) {
            System.out.println("from dow id:"+fileId);
            
            ps.setInt(1, fileId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                response=new DBFIleDownloadResponseDTO();
                response.setFileName(rs.getString("file_name"));
                response.setContentType(rs.getString("file_type"));
                response.setFileInputStream(rs.getBinaryStream("file_data"));
                
            } 

            return Optional.ofNullable(response);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } 


    }
    public int getFileId(String fileName, int userId){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT id FROM files WHERE file_name=? and uploader_id=?")) {
            System.out.println("from dow name:"+fileName);
            
            ps.setString(1, fileName);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                
               return rs.getInt("id");
                
            } 

            
        } catch(Exception e){
            
        }
        return 0;
        
    }

    public void setPermission(int userId,int fileId,boolean download, boolean delete) throws Exception{

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "insert into file_permissions(user_id,file_id,can_download,can_delete) values(?,?,?,?)")) {

                    ps.setInt(1, userId);
                    ps.setInt(2, fileId);
                    ps.setBoolean(3, download);
                    ps.setBoolean(4, delete);

                    ps.executeUpdate();
            
        } catch (Exception e) {
            throw e;
        }


    }
    public void modifyPermission(int userId,int fileId,boolean download, boolean delete) throws Exception{

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                
                "update file_permissions set can_download=?,can_delete=? where user_id=? and file_id=?")) {
                    ps.setBoolean(1, download);
                    ps.setBoolean(2, delete);
                    ps.setInt(3, userId);
                    ps.setInt(4, fileId);
                    
                    ps.executeUpdate();
            
        } catch (Exception e) {
            throw e;
        }


    }
    public boolean alredyUserPresent(int userId,int file_id){
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select * from file_permissions where file_id=? and user_id=?")){

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    ResultSet rs=ps.executeQuery();
                    if(rs.next()){

                        return true;
                    }
                    return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    public boolean hasDownloadPermission(int userId, int file_id){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select id,can_download from file_permissions where file_id=? and user_id=?")) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    if(rs.next()){

                        return rs.getBoolean("can_download");
                    }
                    return false;
            
        } catch (Exception e) {
            return false;
        }
    }
    public boolean hasDeletePermission(int userId, int file_id){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select id,can_delete from file_permissions where file_id=? and user_id=?")) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    if(rs.next()){

                        return rs.getBoolean("can_delete");
                    }
                    return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    public List<FileMeta> getFileDetails(){
        List<FileMeta> list=new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select id,file_name,file_type from files")) {

                    

                    ResultSet rs=ps.executeQuery();
                    while(rs.next()){
                        FileMeta file=new FileMeta();
                        file.setFileId(rs.getInt("id"));
                        file.setFileName(rs.getString("file_name"));
                        file.setFileType(rs.getString("file_type"));
                        list.add(file);
                    }
                    return list;
            
        } catch (Exception e) {
            return list;
        }
    }
    
    
}
