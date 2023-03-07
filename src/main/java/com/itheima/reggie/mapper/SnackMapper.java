package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Snack;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SnackMapper extends BaseMapper<Snack> {
}
