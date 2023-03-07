package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.dto.SnackSetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Categoryb;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SnackSetmeal;
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
@RequestMapping("/setmeal1")
@Slf4j
public class SnackSetmealController {
    @Autowired
    private SnackSetmealService snackSetmealService;

    @Autowired
    private CategorybService categorybService;

    @Autowired
    private SetmealSnackService setmealSnackService;

    /**
     * 新增套餐
     * @param snackSetmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SnackSetmealDto snackSetmealDto){
        log.info("套餐信息：{}",snackSetmealDto);

        snackSetmealService.saveWithSnacks(snackSetmealDto);

        return R.success("新增套餐成功");
    }
    @GetMapping("/list")
    public R<List<SnackSetmeal>> list(SnackSetmeal snackSetmeal) {
        log.info("setmeal:{}", snackSetmeal);
        //条件构造器
        LambdaQueryWrapper<SnackSetmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(snackSetmeal.getName()), SnackSetmeal::getName, snackSetmeal.getName());
        queryWrapper.eq(null != snackSetmeal.getCategoryId(), SnackSetmeal::getCategoryId, snackSetmeal.getCategoryId());
        queryWrapper.eq(null != snackSetmeal.getStatus(), SnackSetmeal::getStatus, snackSetmeal.getStatus());
        queryWrapper.orderByDesc(SnackSetmeal::getUpdateTime);

        return R.success(snackSetmealService.list(queryWrapper));
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
        Page<SnackSetmeal> pageInfo = new Page<>(page,pageSize);
        Page<SnackSetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<SnackSetmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,SnackSetmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(SnackSetmeal::getUpdateTime);

        snackSetmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<SnackSetmeal> records = pageInfo.getRecords();

        List<SnackSetmealDto> list = records.stream().map((item) -> {
            SnackSetmealDto snackSetmealDto=new SnackSetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,snackSetmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Categoryb categoryb = categorybService.getById(categoryId);
            if(categoryb != null){
                //分类名称
                String categorybName = categoryb.getName();
                snackSetmealDto.setCategoryName(categorybName);
            }
            return snackSetmealDto;
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

        snackSetmealService.removeWithSnacks(ids);

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
        LambdaUpdateWrapper<SnackSetmeal> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤条件
        updateWrapper.set(SnackSetmeal::getStatus,status).in(SnackSetmeal::getId,list);
        snackSetmealService.update(updateWrapper);

        return R.success("套餐信息修改成功");
    }
    /**
     * 根据id查询套餐信息
     *(套餐信息的回显)
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SnackSetmealDto> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息:{}", id);
        // 调用service执行查询。、
        SnackSetmealDto snackSetmealDto=snackSetmealService.getByIdWithSnacks(id);
        return R.success(snackSetmealDto);
    }
    /**
     * 修改套餐信息。
     *
     * @param snackSetmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SnackSetmealDto snackSetmealDto) {
        log.info("修改套餐信息{}", snackSetmealDto);
        // 执行更新。
        snackSetmealService.updateWithSnacks(snackSetmealDto);
        return R.success("修改套餐信息成功");
    }
}
