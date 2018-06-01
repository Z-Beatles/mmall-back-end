package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Category;

import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-22 21:28
 */
public interface CategoryService {

    Result addCategory(Long parentId, String categoryName);

    Result updateCategoryName(Long categoryId, String categoryName);

    /**
     * 获取同级分类列表
     *
     * @param parentId 当前分类
     * @return 同级分类列表
     */
    Result<List<Category>> getChildrenParallelCategory(Long parentId);

    /**
     * 获取当前分类id及所有子分类id列表
     *
     * @param categoryId 当前分类id
     * @return 当前分类id及所有子分类id
     */
    Result<List<Long>> getCategoryAndChildrenById(Long categoryId);
}
