package cn.waynechu.mmall.service;

import java.io.File;

/**
 * @author waynechu
 * Created 2018-05-23 22:33
 */
public interface FTPService {

    void connectToFTP(String host, String user, String pass);

    boolean uploadFileToFTP(File file, String ftpHostDir , String serverFilename);

    void downloadFileFromFTP(String ftpRelativePath, String copytoPath);

    void disconnectFTP();
}
