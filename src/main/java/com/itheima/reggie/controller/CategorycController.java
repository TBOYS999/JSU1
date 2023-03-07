package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Categoryc;
import com.itheima.reggie.service.CategorycService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category2")//接口修改
@Slf4j
public class CategorycController {
    @Autowired
    private CategorycService categorycService;

    /**
     * 新增分类
     * @param categoryc
     * @return
     */
    @PostMapping//需要设置接口
    public R<String> save(@RequestBody Categoryc categoryc){
        log.info("category:{}",categoryc);
        categorycService.save(categoryc);
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
        Page<Categoryc> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Categoryc> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Categoryc::getSort);

        //分页查询
        categorycService.page(pageInfo,queryWrapper);
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
        categorycService.remove(ids);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param categoryc
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Categoryc categoryc){
        log.info("修改分类信息：{}",categoryc);

        categorycService.updateById(categoryc);

        return R.success("修改分类信息成功");
    }
    /**
     * 根据前端传回的type在category list中进行条件查询，返回符合条件的(添加菜品时，选择菜品分类的下拉框)
     * @param categoryc
     * @return
     */
    @GetMapping("/list")
    public R<List<Categoryc>>list(Categoryc categoryc)//可以直接用type，也可以用这个
    {//条件构造器
        LambdaQueryWrapper<Categoryc>queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(categoryc.getType()!=null,Categoryc::getType,categoryc.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Categoryc::getSort).orderByDesc(Categoryc::getUpdateTime);
        List<Categoryc> list = categorycService.list(queryWrapper);
        return R.success(list);
    }
}
