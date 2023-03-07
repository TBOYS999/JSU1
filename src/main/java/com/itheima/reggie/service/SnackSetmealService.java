package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.dto.SnackSetmealDto;
import com.itheima.reggie.entity.SnackSetmeal;

import java.util.List;

public interface SnackSetmealService extends IService<SnackSetmeal> {
    public void saveWithSnacks(SnackSetmealDto snackSetmealDto);


    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithSnacks(List<Long> ids);
    public void updateWithSnacks(SnackSetmealDto snackSetmealDto);
    public SnackSetmealDto getByIdWithSnacks(Long id);
}
