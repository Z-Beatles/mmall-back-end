package cn.waynechu.mmall.web.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author waynechu
 * Created 2018-05-24 17:13
 */
@ApiIgnore
@Controller
@RequestMapping("/v1")
public class FileUploadController {

    @GetMapping("/upload")
    public String fileUpload() {
        return "upload";
    }
}
