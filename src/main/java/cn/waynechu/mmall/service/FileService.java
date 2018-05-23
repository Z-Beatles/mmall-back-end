package cn.waynechu.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author waynechu
 * Created 2018-05-23 21:52
 */
public interface FileService {

    String upload(MultipartFile file, String path);
}
