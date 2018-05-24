package cn.waynechu.mmall.service;

import cn.waynechu.mmall.common.ServerResponse;
import cn.waynechu.mmall.entity.Category;

import java.util.List;

/**
 * @author waynechu
 * Created 2018-05-22 21:28
 */
public interface CategoryService {

    ServerResponse addCategory(int parentId, String categoryName);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 获取同级分类列表
     *
     * @param parentId 当前分类
     * @return 同级分类列表
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    /**
     * 获取当前分类id及所有子分类id列表
     *
     * @param categoryId 当前分类id
     * @return 当前分类id及所有子分类id
     */
    ServerResponse<List<Integer>> getCategoryAndChildrenById(Integer categoryId);
}
