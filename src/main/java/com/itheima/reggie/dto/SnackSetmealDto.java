package com.itheima.reggie.dto;

import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.entity.SetmealSnack;
import com.itheima.reggie.entity.SnackSetmeal;
import lombok.Data;

import java.util.List;
@Data

public class SnackSetmealDto extends SnackSetmeal {
    private List<SetmealSnack> setmealSnacks;
    private String categoryName;
}
