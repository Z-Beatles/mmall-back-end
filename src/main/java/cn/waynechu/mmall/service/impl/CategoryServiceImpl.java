package cn.waynechu.mmall.service.impl;

import cn.waynechu.mmall.common.Const;
import cn.waynechu.mmall.common.Result;
import cn.waynechu.mmall.entity.Category;
import cn.waynechu.mmall.mapper.CategoryMapper;
import cn.waynechu.mmall.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author waynechu
 * Created 2018-05-22 21:28
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result addCategory(Long parentId, String categoryName) {
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(Const.CategoryStatus.USAGE);

        int insertCount = categoryMapper.insertSelective(category);
        if (insertCount > 0) {
            return Result.createBySuccessMessage("添加品类成功");
        }
        return Result.createByErrorMessage("添加品类失败");
    }

    @Override
    public Result updateCategoryName(Long categoryId, String categoryName) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int updateCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (updateCount > 0) {
            return Result.createBySuccessMessage("更新品类名称成功");
        }
        return Result.createByErrorMessage("更新品类名称失败");
    }

    @Override
    public Result<List<Category>> getChildrenParallelCategory(Long parentId) {
        List<Category> categoryList = categoryMapper.getChildrenParallelCategory(parentId);
        if (categoryList.isEmpty()) {
            log.warn("未找到子分类，当前分类ID：{}", parentId);
        }
        return Result.createBySuccess(categoryList);
    }

    @Override
    public Result<List<Long>> getCategoryAndChildrenById(Long categoryId) {
        Set<Category> categorySet = new HashSet<>();
        // 递归获取子分类
        Set<Category> categorySetByRecursion = getChildCategory(categorySet, categoryId);

        List<Long> categoryIdList = new ArrayList<>();
        for (Category categoryItem : categorySetByRecursion) {
            categoryIdList.add(categoryItem.getId());
        }
        return Result.createBySuccess(categoryIdList);
    }


    /**
     * 递归查找子节点
     **/
    private Set<Category> getChildCategory(Set<Category> categorySet, Long categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        // 查找子节点，子节点为空则递归体返回
        List<Category> categoryList = categoryMapper.getChildrenParallelCategory(categoryId);
        for (Category categoryItem : categoryList) {
            getChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
