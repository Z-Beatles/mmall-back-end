package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.properties.FTPServerProperties;
import cn.waynechu.mmall.service.FTPService;
import cn.waynechu.mmall.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author waynechu
 * Created 2018-05-23 21:52
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FTPService ftpService;

    @Autowired
    private FTPServerProperties ftpServerProperties;

    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        // 获取文件的扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 使用UUID作为上传的文件名称
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        // 创建目录
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        // 创建文件
        File targetFile = new File(path, uploadFileName);

        try {
            // 上传文件
            file.transferTo(targetFile);

            // 将targetFile上传到FTP服务器上
            String ip = ftpServerProperties.getIp();
            String user = ftpServerProperties.getUser();
            String password = ftpServerProperties.getPassword();
            ftpService.connectToFTP(ip, user, password);
            // 上传到ftp目录下的img文件夹下，注意如果该文件夹不存在，FTP会返回550 Failed to change directory.错误
            boolean uploadFlag = ftpService.uploadFileToFTP(targetFile, "img", targetFile.getName());
            ftpService.disconnectFTP();

            // 上传完成后，删除upload下面的文件
            if (uploadFlag) {
                targetFile.delete();
            }
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        return targetFile.getName();
    }
}
