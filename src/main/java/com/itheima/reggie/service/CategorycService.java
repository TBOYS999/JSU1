package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Categoryc;

public interface CategorycService extends IService<Categoryc> {

    public void remove(Long ids);

}
