package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SnackDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Snack;

public interface SnackService  extends IService<Snack> {
    //新增菜品
    public  void saveWithFlavor(SnackDto snackDto);
    public SnackDto getByIdWithFlavor(long id);
    public  void updateWithFlavor(SnackDto snackDto);
}
