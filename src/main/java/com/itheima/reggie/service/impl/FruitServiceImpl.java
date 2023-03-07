package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.FruitDto;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Fruit;
import com.itheima.reggie.mapper.FruitMapper;
import com.itheima.reggie.service.FruitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FruitServiceImpl extends ServiceImpl<FruitMapper, Fruit> implements FruitService {
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param fruitDto
     */
@Transactional
    @Override
    public  void saveWithFlavor(FruitDto fruitDto)
    {

        //保存菜品的基本信息到数据表中
        this.save(fruitDto);

    }
    public FruitDto getByIdWithFlavor(long id)
    {
        //查询菜品的基本信息，从dish表中查询
        Fruit fruit=this.getById(id);
        FruitDto fruitDto=new FruitDto();
        BeanUtils.copyProperties(fruit,fruitDto);
        return fruitDto;
//        return fruit;
    }
    @Transactional
    @Override
    public  void updateWithFlavor(FruitDto fruitDto)
    {
        //更新 snack表信息
        this.updateById(fruitDto);
    }
}
