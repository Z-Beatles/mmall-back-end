package cn.waynechu.mmall.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author waynechu
 * Created 2018-05-12 00:14
 */
@Data
public class Category {

    private Long id;

    private Long parentId;

    private String name;

    private Integer status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}
