package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SnackDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Snack;
import com.itheima.reggie.mapper.SnackMapper;
import com.itheima.reggie.service.SnackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SnackServiceImpl extends ServiceImpl<SnackMapper, Snack> implements SnackService {
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param snackDto
     */
@Transactional
    @Override
    public  void saveWithFlavor(SnackDto snackDto)
    {

        //保存菜品的基本信息到数据表中
        this.save(snackDto);

    }
    public SnackDto getByIdWithFlavor(long id)
    {
        //查询菜品的基本信息，从dish表中查询
        Snack snack=this.getById(id);
        SnackDto snackDto=new SnackDto();
        BeanUtils.copyProperties(snack,snackDto);
        return snackDto;
//        return snack;
    }
    @Transactional
    @Override
    public  void updateWithFlavor(SnackDto snackDto)
    {
        //更新 snack表信息
        this.updateById(snackDto);
    }
}
