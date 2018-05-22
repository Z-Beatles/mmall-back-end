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

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    ServerResponse getCategoryAndChildrenById(Integer categoryId);
}
