package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Categoryb;

public interface CategorybService extends IService<Categoryb> {

    public void remove(Long id);
}
