package org.fileservice.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.fileservice.Exception.MaximumFileLimitExceeded;
import org.fileservice.Exception.MaximumFileSizeExceeded;
import org.fileservice.Exception.UpdateFailedException;
import org.fileservice.model.FileMeta;



public class FileRepository {
    private final double MAXIMUM_ALLOWED_SIZE=10;


    public void uploadFile(String fileName, String contentType, double size,String file_path,int userId,String description)throws MaximumFileSizeExceeded,MaximumFileLimitExceeded,UpdateFailedException,Exception{
        Connection con = null;
        PreparedStatement uploadStmt = null;
        PreparedStatement permissionStmt=null;
        PreparedStatement descPermissionStmt=null;
        PreparedStatement check=null;
        PreparedStatement sizeCheckStmt=null;
        try {

            
            con=DBConnection.getConnection();
            con.setAutoCommit(false);
            // check user has how many files
            String checkQuery="select count(*) as total from file_permissions where user_id=? for update";
            check=con.prepareStatement(checkQuery);
            check.setInt(1, userId);
            ResultSet rs=check.executeQuery();
            if(rs.next() && rs.getInt("total")>=10)throw new MaximumFileLimitExceeded("user alredy has 10  files");


            String sizeQuery="select sum(f.file_size) as file_size from files f join file_permissions fp on fp.file_id=f.id where fp.user_id=?";
            sizeCheckStmt=con.prepareStatement(sizeQuery);
            sizeCheckStmt.setInt(1, userId);
            double currentSize=0;
            ResultSet resultForSize=sizeCheckStmt.executeQuery();
            if(resultForSize.next()){
                currentSize=resultForSize.getDouble("file_size");
            }
            if(currentSize+size>MAXIMUM_ALLOWED_SIZE)throw new MaximumFileSizeExceeded("Maximum file size exceeded. Allowed size Only 10 MB");


            // if user has less than 10 files proceed to upload a file
            uploadStmt=con.prepareStatement(
                "INSERT INTO files (file_name, file_type, file_size,uploader_id,file_path) VALUES (?, ?, ?,?,?)",Statement.RETURN_GENERATED_KEYS);
            
            uploadStmt.setString(1, fileName);
            uploadStmt.setString(2, contentType);
            uploadStmt.setDouble(3, size); 
            uploadStmt.setInt(4, userId);
            uploadStmt.setString(5, file_path);

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

            permissionStmt = con.prepareStatement("insert into file_permissions(user_id,file_id,permission) values(?,?,(select sum(id) from permission))");
            permissionStmt.setInt(1, userId);
            permissionStmt.setInt(2, file_id);
            
            int affectedRowsForPermission=permissionStmt.executeUpdate();

            if(affectedRowsForFileUpload==0 || affectedRowsForPermission==0){
                throw new UpdateFailedException("File can't  upload");
            }

            descPermissionStmt=con.prepareStatement("insert into file_descriptions(file_id,description) values(?,?)");
            descPermissionStmt.setInt(1, file_id);
            descPermissionStmt.setString(2, description);
            if(descPermissionStmt.executeUpdate()==0){
                throw new UpdateFailedException("File can't  upload");
            }
            con.commit();
            System.out.println("Transaction successful!");
        }
        catch(UpdateFailedException | MaximumFileLimitExceeded |MaximumFileSizeExceeded e){
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
            if(check!=null)check.close();
            if(sizeCheckStmt!=null)sizeCheckStmt.close();
            if (uploadStmt != null) uploadStmt.close();
            if (permissionStmt != null) permissionStmt.close();
            if(descPermissionStmt!=null)descPermissionStmt.close();
            if (con != null) {
                con.setAutoCommit(true); 
                con.close();
            }
        }
    }
    public Optional<FileMeta> getFileMetaDetails(int fileId){
        FileMeta fileMeta=null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "SELECT file_name, file_type, file_size,file_path FROM files WHERE id=?")) {
            
            
            ps.setInt(1, fileId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fileMeta=new FileMeta();
                fileMeta.setFileName(rs.getString("file_name"));
                fileMeta.setFileType(rs.getString("file_type"));
                fileMeta.setFilePath(rs.getString("file_path"));
                fileMeta.setFileSize(rs.getDouble("file_size"));
                
                
            } 

            return Optional.ofNullable(fileMeta);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.ofNullable(fileMeta);
        } 


    }
    

    public int addPermission(int userId,int fileId,int permision){

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "insert into file_permissions(user_id,file_id,permission) values(?,?,?)")) {

                    ps.setInt(1, userId);
                    ps.setInt(2, fileId);
                    ps.setInt(3, permision);

                    return ps.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }


    }
    public int updatePermission(int userId,int fileId,int permission)throws MaximumFileLimitExceeded,Exception{
        System.out.println("Entered in update permission method");
        Connection con =null;
        PreparedStatement ps =null;
        PreparedStatement check=null;
        try  {  

            con = DBConnection.getConnection();
            con.setAutoCommit(false);
            String checkQuery="select count(*) as total from file_permissions where user_id=? for update";
            check=con.prepareStatement(checkQuery);
            check.setInt(1, userId);
            ResultSet rs=check.executeQuery();
            if(rs.next() && rs.getInt("total")>=10)throw new MaximumFileLimitExceeded("user alredy has 10 files");
            
            String query="UPDATE file_permissions target JOIN file_permissions source ON source.user_id = target.user_id AND source.file_id = target.file_id SET target.permission = source.permission|? WHERE target.user_id = ? AND target.file_id = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1, permission);
            ps.setInt(2, userId);
            ps.setInt(3, fileId);
            
            
            
            return ps.executeUpdate();
            
        }catch(MaximumFileLimitExceeded e){
            throw e;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally {
           
            if (ps != null) ps.close();
            if (check != null) check.close();
            if (con != null) {
                con.setAutoCommit(true); 
                con.close();
            }
        }


    }
    public boolean isUserPermissionExists(int userId,int file_id){
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                "select * from file_permissions where file_id=? and user_id=?")){

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    ResultSet rs=ps.executeQuery();
                    return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasDownloadPermission(int userId, int file_id){
            String query="select * from file_permissions fp join permission p on fp.permission&p.id>0 where p.permission_type='Download' and file_id=? and user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    return rs.next();
                    
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasViewPermission(int userId, int file_id){
            String query="select * from file_permissions fp join permission p on fp.permission&p.id>0 where p.permission_type='View' and file_id=? and user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    return rs.next();
                    
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean hasSharePermission(int userId,int file_id){
        System.out.println("Share permission permision checker method");
       
        String query="select * from file_permissions fp join permission p on fp.permission&p.id>0 where p.permission_type='share' and file_id=? and user_id=?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query
                )) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean hasDeletePermission(int userId, int file_id){
        System.out.println("delete permision checker method");
       
        String query="select * from file_permissions fp join permission p on fp.permission&p.id>0 where p.permission_type='Delete' and file_id=? and user_id=?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query
                )) {

                    ps.setInt(1, file_id);
                    ps.setInt(2, userId);
                    

                    ResultSet rs=ps.executeQuery();
                    return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FileMeta> getAllFiles(int userId){
        List<FileMeta> list=new ArrayList<>();
        
        String query="with temp as(select id,file_name,file_type , file_size ,file_path from files where id in(select file_id from file_permissions where user_id=? ))select t.id as id,t.file_name as file_name,t.file_type as file_type, t.file_path as file_path,t.file_size as file_size,fd.description as file_description from temp t join file_descriptions fd on fd.file_id=t.id";
     
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                query)) {
                   
                    ps.setInt(1, userId);
                    ResultSet rs=ps.executeQuery();
                    while(rs.next()){
                        FileMeta file=new FileMeta();
                        file.setFileId(rs.getInt("id"));
                        file.setFileName(rs.getString("file_name"));
                        file.setFileType(rs.getString("file_type"));
                        file.setFileSize(rs.getDouble("file_size"));
                        file.setFilePath(rs.getString("file_path"));
                        file.setDescription(rs.getString("file_description"));
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

    public int getPermissionValue(String permissionType){
        String query="select id from permission where permission_type=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, permissionType);
                
                ResultSet rs=ps.executeQuery();
                if(rs.next())return rs.getInt("id");
                return 0;
                    
                    
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
            
        }

    }

    public List<String> getUserDetailsUsingFileId(int fileId){
        List<String> user=new ArrayList<>();
        String query="select ud.name as name from user_details ud join file_permissions fp on ud.id=fp.user_id where file_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
               ps.setInt(1, fileId);
                
                ResultSet rs=ps.executeQuery();

                while(rs.next()){
                    user.add(rs.getString("name"));
                }
                return user;
                
                    
                    
        } catch (Exception e) {
            e.printStackTrace();
            return user;
            
        }

    }
    



  
    
    
}
