package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.FruitDto;
import com.itheima.reggie.entity.Fruit;

public interface FruitService  extends IService<Fruit> {
    //新增菜品
    public  void saveWithFlavor(FruitDto fruitDto);
    public FruitDto getByIdWithFlavor(long id);
    public  void updateWithFlavor(FruitDto fruitDto);
}
