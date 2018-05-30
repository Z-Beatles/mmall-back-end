package cn.waynechu.mmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author waynechu
 * Created 2018-05-21 11:09
 */
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /** 类别id **/
    private Long id;
    /** 父类id：当id为0说明是根节点，一级类别 **/
    private Long parentId;
    /** 类目名称 **/
    private String name;
    /** 类目状态：0已废弃，1正常，默认0 **/
    private Integer status;
    /** 排序编号：同类展示顺序，数值相等则自然排序 **/
    private Integer sortOrder;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}