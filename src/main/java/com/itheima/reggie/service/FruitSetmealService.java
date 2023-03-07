package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.FruitSetmealDto;
import com.itheima.reggie.entity.FruitSetmeal;

import java.util.List;

public interface FruitSetmealService extends IService<FruitSetmeal> {
    public void saveWithFruits(FruitSetmealDto fruitSetmealDto);
    public void removeWithFruits(List<Long> ids);
    public void updateWithFruits(FruitSetmealDto fruitSetmealDto);
    public FruitSetmealDto getByIdWithFruits(Long id);
}
