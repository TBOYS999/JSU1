package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.CategorybMapper;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategorybServicelmpl extends ServiceImpl<CategorybMapper, Categoryb> implements CategorybService {
    @Autowired
    private SnackService snackService;

    @Autowired
    private SnackSetmealService snackSetmealService;

        /**
         * 根据id删除分类，删除之前需要进行判断
         * @param id
         */
        @Override
        public void remove(Long id) {
            LambdaQueryWrapper<Snack> snackLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //添加查询条件，根据分类id进行查询
            snackLambdaQueryWrapper.eq(Snack::getCategoryId,id);
            int count1 = snackService.count(snackLambdaQueryWrapper);

            //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
            if(count1 > 0){
                //已经关联菜品，抛出一个业务异常
                throw new CustomException("当前分类下关联了菜品，不能删除");
            }

            //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
            LambdaQueryWrapper<SnackSetmeal> snackSetmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //添加查询条件，根据分类id进行查询
            snackSetmealLambdaQueryWrapper.eq(SnackSetmeal::getCategoryId,id);
            int count2 =snackSetmealService.count(snackSetmealLambdaQueryWrapper);
            if(count2 > 0){
                //已经关联套餐，抛出一个业务异常
                throw new CustomException("当前分类下关联了套餐，不能删除");
            }

            //正常删除分类
            super.removeById(id);
    }
}