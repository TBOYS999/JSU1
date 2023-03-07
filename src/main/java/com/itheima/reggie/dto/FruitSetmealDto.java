package com.itheima.reggie.dto;

import com.itheima.reggie.entity.SetmealFruit;
import com.itheima.reggie.entity.FruitSetmeal;
import lombok.Data;

import java.util.List;
@Data

public class FruitSetmealDto extends FruitSetmeal {

    private List<SetmealFruit> setmealFruits;

    private String categoryName;

}
