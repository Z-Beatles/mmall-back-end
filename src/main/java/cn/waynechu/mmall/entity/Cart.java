package cn.waynechu.mmall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author waynechu
 * Created 2018-05-21 11:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    /** 购物车id **/
    private Long id;
    /** 用户id **/
    private Long userId;
    /** 商品id **/
    private Long productId;
    /** 购买数量 **/
    private Integer quantity;
    /** 是否勾选：0未勾选，1已勾选，默认0 **/
    private Integer checked;
    /** 创建时间 **/
    private LocalDateTime createTime;
    /** 更新时间 **/
    private LocalDateTime updateTime;
}