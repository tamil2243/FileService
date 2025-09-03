package org.fileservice.repository;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.dto.DBFIleDownloadResponseDTO;
import org.fileservice.model.FileMeta;



public class FileRepository {



    public void uploadFile(String fileName, String contentType, FileInputStream fis, File file, int userId)throws Exception{
        Connection con = null;
        PreparedStatement uploadStmt = null;
        PreparedStatement permissionStmt=null;
        try {

            
            con=DBConnection.getConnection();
            con.setAutoCommit(false);
            uploadStmt=con.prepareStatement(
                "INSERT INTO files (file_name, file_type, file_data,uploader_id) VALUES (?, ?, ?,?)",Statement.RETURN_GENERATED_KEYS);
            
            uploadStmt.setString(1, fileName);
            uploadStmt.setString(2, contentType);
            uploadStmt.setBinaryStream(3, fis, (int) file.length()); 
            uploadStmt.setInt(4, userId);

            int affectedRowsForFileUpload= uploadStmt.executeUpdate();
            System.out.println("File uploaded successfully");
           
            int file_id = 0;
            try (ResultSet generatedKeys = uploadStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    file_id = generatedKeys.getInt(1);
                } else {
                    throw new UpdateFailedException("Failed to obtain file_id after insert.");
                }
            }

            permissionStmt = con.prepareStatement("insert into file_permissions(user_id,file_id,can_download,can_delete) values(?,?,?,?)");
            permissionStmt.setInt(1, userId);
            permissionStmt.setInt(2, file_id);
            permissionStmt.setBoolean(3, true);
            permissionStmt.setBoolean(4, true);
            int affectedRowsForPermission=permissionStmt.executeUpdate();

            if(affectedRowsForFileUpload==0 || affectedRowsForPermission==0){
                throw new UpdateFailedException("File can't  upload");
            }

            con.commit();
            System.out.println("Transaction successful!");
        }
        catch(UpdateFailedException e){
            if (con != null) {
               
                con.rollback();
                System.out.println("Transaction rolled back due to error: " + e.getMessage());
            }
            throw e; 
        }
        catch (Exception e) {
            if (con != null) {
               
                con.rollback();
                System.out.println("Transaction rolled back due to error: " + "Something went wrong during file upload");
                e.printStackTrace();
            }
            throw e;  
        } finally {
           
            if (uploadStmt != null) uploadStmt.close();
            if (permissionStmt != null) permissionStmt.close();
            if (con != null) {
                con.setAutoCommit(true); 
                con.close();
            }
        }
    }
    public Optional<DBFIleDownloadResponseDTO> getFileForDownload(int fileId){
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
            return Optional.ofNullable(response);
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
            e.printStackTrace();
        }
        return 0;
        
    }

    public int addPermission(int userId,int fileId,boolean download, boolean delete){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "insert into file_permissions(user_id,file_id,can_download,can_delete) values(?,?,?,?)")) {

                    ps.setInt(1, userId);
                    ps.setInt(2, fileId);
                    ps.setBoolean(3, download);
                    ps.setBoolean(4, delete);

                    return ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }
    public int updatePermission(int userId,int fileId,boolean download, boolean delete){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                
                "update file_permissions set can_download=?,can_delete=? where user_id=? and file_id=?")) {
                    ps.setBoolean(1, download);
                    ps.setBoolean(2, delete);
                    ps.setInt(3, userId);
                    ps.setInt(4, fileId);
                    
                    return ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }
    public boolean isUserPermissionExists(int userId,int file_id){
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasDeletePermission(int userId, int file_id){
        System.out.println("delete permision checker method");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select id,can_delete from file_permissions where file_id=? and user_id=?")) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    if(rs.next()){
                        System.out.println("permission fetched");
                        return rs.getBoolean("can_delete");
                    }
                
                    return false;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FileMeta> listAllFiles(){
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
            e.printStackTrace();
            return list;
        }
    }
    public int deleteFile(int fileId){
       
       

       
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "delete from files where id=?")) {
                    ps.setInt(1, fileId);
                    return ps.executeUpdate();
                    
                    
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
            
        }
        

    }
    public int deletePermission(int fileId){
         try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "delete from file_permissions  where file_id=?")) {
                    ps.setInt(1, fileId);
                    return ps.executeUpdate();

                   
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
            
        }
    }
    
    
}
