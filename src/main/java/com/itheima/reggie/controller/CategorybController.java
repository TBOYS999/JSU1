package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Categoryb;
import com.itheima.reggie.service.CategorybService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category1")//接口修改
@Slf4j
public class CategorybController {
    @Autowired
    private CategorybService categorybService;

    /**
     * 新增分类
     * @param categoryb
     * @return
     */
    @PostMapping//需要设置接口
    public R<String> save(@RequestBody Categoryb categoryb){
        log.info("category:{}",categoryb);
        categorybService.save(categoryb);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //分页构造器
        Page<Categoryb> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Categoryb> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Categoryb::getSort);

        //分页查询
        categorybService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除分类，id为：{}",ids);

        //categoryService.removeById(ids);
        categorybService.remove(ids);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param categoryb
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Categoryb categoryb){
        log.info("修改分类信息：{}",categoryb);

        categorybService.updateById(categoryb);

        return R.success("修改分类信息成功");
    }
    /**
     * 根据前端传回的type在category list中进行条件查询，返回符合条件的(添加菜品时，选择菜品分类的下拉框)
     * @param categoryb
     * @return
     */
    @GetMapping("/list")
    public R<List<Categoryb>>list(Categoryb categoryb)//可以直接用type，也可以用这个
    {//条件构造器
        LambdaQueryWrapper<Categoryb>queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(categoryb.getType()!=null,Categoryb::getType,categoryb.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Categoryb::getSort).orderByDesc(Categoryb::getUpdateTime);
        List<Categoryb> list = categorybService.list(queryWrapper);
        return R.success(list);
    }
}
