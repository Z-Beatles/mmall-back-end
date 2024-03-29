package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.service.FtpService;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author waynechu
 * Created 2018-05-23 22:34
 */
@Slf4j
@Service
public class FtpServiceImpl implements FtpService {

    @Autowired
    private Environment environment;

    private FTPClient ftpClient;

    @Override
    public void connectToFTP(String ip, String user, String password) {
        ftpClient = new FTPClient();
        // 开发环境下将FTP过程中使用的命令输出到控制台
        if (environment.acceptsProfiles("dev")) {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }

        try {
            ftpClient.connect(ip);
            ftpClient.login(user, password);
        } catch (IOException e) {
            log.error("连接FTP服务器失败", e);
        }
    }

    @Override
    public boolean uploadFileToFTP(File file, String ftpHostDir, String serverFilename) {
        try {
            ftpClient.changeWorkingDirectory(ftpHostDir);
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            // 设置文件类型
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 使用被动模式建立连接
            ftpClient.enterLocalPassiveMode();

            @Cleanup InputStream inputStream = new FileInputStream(file);
            boolean result = ftpClient.storeFile(serverFilename, inputStream);
            if (result) {
                log.info("文件上传FTP服务器成功，FTP目录：{}，上传文件名：{}", ftpHostDir, serverFilename);
            }
            return result;
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return false;
    }

    @Override
    public void downloadFileFromFTP(String ftpRelativePath, String copyToPath) {
        try {
            @Cleanup FileOutputStream fos = new FileOutputStream(copyToPath);
            ftpClient.retrieveFile(ftpRelativePath, fos);
        } catch (IOException e) {
            log.error("下载文件失败", e);
        }
    }

    @Override
    public void disconnectFTP() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error("FTP断开连接失败", e);
            }
        }
    }
}
