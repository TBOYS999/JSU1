package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.dto.SnackSetmealDto;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.SnackMapper;
import com.itheima.reggie.mapper.SnackSetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealSnackService;
import com.itheima.reggie.service.SnackService;
import com.itheima.reggie.service.SnackSetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SnackSetmealServiceImpl extends ServiceImpl<SnackSetmealMapper, SnackSetmeal> implements SnackSetmealService {
    @Autowired
    private SetmealSnackService setmealSnackService;
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param snackSetmealDto
     */
    @Transactional
    @Override
    public void saveWithSnacks(SnackSetmealDto snackSetmealDto) {
        //保存套餐的基本信息，操作setmeal,执行insert操作
        this.save(snackSetmealDto);
        List<SetmealSnack>setmealSnacks=snackSetmealDto.getSetmealSnacks();
        setmealSnacks.stream().map((item)->
        {
          item.setSetmealId(snackSetmealDto.getId());
          return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealSnackService.saveBatch(setmealSnacks);

    }
    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithSnacks(List<Long>ids)
    {
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<SnackSetmeal> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.in(SnackSetmeal::getId,ids);
        queryWrapper.eq(SnackSetmeal::getStatus,1);
        int count=this.count(queryWrapper);
        if (count>0)
        {
            throw new CustomException("套餐正在售卖中，不能删除");

        }
        //如果不能删除，抛出一个业务异常
        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        //删除关系表中的数据
        LambdaQueryWrapper<SetmealSnack>lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealSnack::getSetmealId,ids);
        setmealSnackService.remove(lambdaQueryWrapper);
    }
    /**
     * 通过id查询套餐信息， 同时还要查询关联表setmeal_dish的菜品信息进行回显。
     *
     * @param id 待查询的id
     */
    public SnackSetmealDto getByIdWithSnacks(Long id) {
        // 根据id查询setmeal表中的基本信息
        SnackSetmeal snackSetmeal = this.getById(id);
        SnackSetmealDto snackSetmealDto= new SnackSetmealDto();
        // 对象拷贝。
        BeanUtils.copyProperties(snackSetmeal, snackSetmealDto);
        // 查询关联表setmeal_dish的菜品信息
        LambdaQueryWrapper<SetmealSnack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealSnack::getSetmealId, id);
        List<SetmealSnack> setmealSnacksList = setmealSnackService.list(queryWrapper);
        //设置套餐菜品属性
        snackSetmealDto.setSetmealSnacks(setmealSnacksList);
        return snackSetmealDto;
    }
    /**
     * 更新套餐信息，不仅要更新setmeal基本信息， 还要更新套餐所对应的菜品到setmeal_dish表
     *
     * @param snackSetmealDto
     */
    @Transactional
    @Override
    public void updateWithSnacks(SnackSetmealDto snackSetmealDto) {
        // 保存setmeal表中的基本数据。
        this.updateById(snackSetmealDto);
        // 先删除原来的套餐所对应的菜品数据。
        LambdaQueryWrapper<SetmealSnack> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealSnack::getSetmealId, snackSetmealDto.getId());
        setmealSnackService.remove(queryWrapper);
        // 更新套餐关联菜品信息。setmeal_dish表。
        // Field 'setmeal_id' doesn't have a default value] with root cause
        // 所以需要处理setmeal_id字段。
        // 先获得套餐所对应的菜品集合。
        List<SetmealSnack> setmealSnacks = snackSetmealDto.getSetmealSnacks();
        //每一个item为SetmealDish对象。
        setmealSnacks = setmealSnacks.stream().map((item) -> {
            //设置setmeal_id字段。
            item.setSetmealId(snackSetmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 重新保存套餐对应菜品数据
        setmealSnackService.saveBatch(setmealSnacks);
    }

}
