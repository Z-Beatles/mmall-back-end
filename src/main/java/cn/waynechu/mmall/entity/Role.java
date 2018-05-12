package cn.waynechu.mmall.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 13:47
 */
@Data
public class Role implements Serializable {

    private Integer id;

    private String name;

    private String nameZh;

    private String info;

    private Date createTime;

    private Date updateTime;
}
