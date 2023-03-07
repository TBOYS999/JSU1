package com.itheima.reggie.dto;

import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Fruit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FruitDto extends Fruit {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
