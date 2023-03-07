package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Categoryc;
import com.itheima.reggie.entity.Fruit;
import com.itheima.reggie.entity.FruitSetmeal;
import com.itheima.reggie.mapper.CategorycMapper;
import com.itheima.reggie.service.CategorycService;
import com.itheima.reggie.service.FruitService;
import com.itheima.reggie.service.FruitSetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategorycServiceImpl extends ServiceImpl<CategorycMapper, Categoryc> implements CategorycService {
    @Autowired
    private FruitService fruitService;

    @Autowired
    private FruitSetmealService fruitSetmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param ids
     */
    @Override
    public void remove(Long ids) {
        LambdaQueryWrapper<Fruit> fruitLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        fruitLambdaQueryWrapper.eq(Fruit::getCategoryId,ids);
        int count1 = fruitService.count(fruitLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<FruitSetmeal> fruitsetmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        fruitsetmealLambdaQueryWrapper.eq(FruitSetmeal::getCategoryId,ids);
        int count2 = fruitSetmealService.count();
        if(count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(ids);
    }

}
