package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.FruitSetmealDto;
import com.itheima.reggie.entity.Categoryc;
import com.itheima.reggie.entity.FruitSetmeal;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal2")
@Slf4j
public class FruitSetmealController {
    @Autowired
    private FruitSetmealService fruitSetmealService;

    @Autowired
    private CategorycService categorycService;

    @Autowired
    private SetmealFruitService setmealFruitService;

    /**
     * 新增套餐
     * @param fruitSetmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody FruitSetmealDto fruitSetmealDto){
        log.info("套餐信息：{}",fruitSetmealDto);

        fruitSetmealService.saveWithFruits(fruitSetmealDto);

        return R.success("新增套餐成功");
    }
    @GetMapping("/list")
    public R<List<FruitSetmeal>> list(FruitSetmeal fruitSetmeal) {
        log.info("setmeal:{}", fruitSetmeal);
        //条件构造器
        LambdaQueryWrapper<FruitSetmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(fruitSetmeal.getName()), FruitSetmeal::getName, fruitSetmeal.getName());
        queryWrapper.eq(null != fruitSetmeal.getCategoryId(), FruitSetmeal::getCategoryId, fruitSetmeal.getCategoryId());
        queryWrapper.eq(null != fruitSetmeal.getStatus(), FruitSetmeal::getStatus, fruitSetmeal.getStatus());
        queryWrapper.orderByDesc(FruitSetmeal::getUpdateTime);

        return R.success(fruitSetmealService.list(queryWrapper));
    }
    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //分页构造器对象
        Page<FruitSetmeal> pageInfo = new Page<>(page,pageSize);
        Page<FruitSetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<FruitSetmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,FruitSetmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(FruitSetmeal::getUpdateTime);

        fruitSetmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<FruitSetmeal> records = pageInfo.getRecords();

        List<FruitSetmealDto> list = records.stream().map((item) -> {
            FruitSetmealDto fruitSetmealDto=new FruitSetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,fruitSetmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Categoryc categoryc = categorycService.getById(categoryId);
            if(categoryc != null){
                //分类名称
                String categorycName = categoryc.getName();
                fruitSetmealDto.setCategoryName(categorycName);
            }
            return fruitSetmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        fruitSetmealService.removeWithFruits(ids);

        return R.success("套餐数据删除成功");
    }


    /**
     * 根据id(批量)停售/启售套餐信息
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateMulStatus(@PathVariable Integer status, Long[] ids){
        List<Long> list = Arrays.asList(ids);

        //构造条件构造器
        LambdaUpdateWrapper<FruitSetmeal> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤条件
        updateWrapper.set(FruitSetmeal::getStatus,status).in(FruitSetmeal::getId,list);
        fruitSetmealService.update(updateWrapper);

        return R.success("套餐信息修改成功");
    }
    /**
     * 根据id查询套餐信息
     *(套餐信息的回显)
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<FruitSetmealDto> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息:{}", id);
        // 调用service执行查询。、
        FruitSetmealDto fruitSetmealDto=fruitSetmealService.getByIdWithFruits(id);
        return R.success(fruitSetmealDto);
    }
    /**
     * 修改套餐信息。
     *
     * @param fruitSetmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody FruitSetmealDto fruitSetmealDto) {
        log.info("修改套餐信息{}", fruitSetmealDto);
        // 执行更新。
        fruitSetmealService.updateWithFruits(fruitSetmealDto);
        return R.success("修改套餐信息成功");
    }
}
