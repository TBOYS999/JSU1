package com.itheima.reggie.dto;

import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Snack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SnackDto extends Snack {
        private List<DishFlavor> flavors = new ArrayList<>();
        private String categoryName;

        private Integer copies;

}
