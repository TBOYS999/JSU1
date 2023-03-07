package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SnackDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Categoryb;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Snack;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/dish1")
@Slf4j
public class SnackController {
    @Autowired
    private SnackService snackService;

    @Autowired
    private CategorybService categorybService;

    /**
     * 新增菜品
     * @param snackDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SnackDto snackDto){
        log.info(snackDto.toString());

        snackService.saveWithFlavor(snackDto);

        return R.success("新增零食成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //构造分页构造器对象
        Page<Snack> pageInfo = new Page<>(page, pageSize);
        Page<SnackDto> snackDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Snack> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Snack::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Snack::getUpdateTime);

        //执行分页查询
        snackService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,snackDtoPage,"records");//records是一个列表集合，不需要拷贝

        List<Snack> records = pageInfo.getRecords();

        List<SnackDto> list = records.stream().map((item) -> {
            SnackDto snackDto = new SnackDto();

            BeanUtils.copyProperties(item,snackDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Categoryb categoryb = categorybService.getById(categoryId);

            if(categoryb != null){
                String categoryName = categoryb.getName();
                snackDto.setCategoryName(categoryName);
            }
            return snackDto;
        }).collect(Collectors.toList());

        snackDtoPage.setRecords(list);

        return R.success(snackDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SnackDto> get(@PathVariable Long id){

       SnackDto snackDto = snackService.getByIdWithFlavor(id);
//        Snack snack=snackService.getByIdWithFlavor(id);

       return R.success(snackDto);
//        return R.success(snack);
    }

    /**
     * 修改菜品
     * @param snackDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SnackDto snackDto){
        log.info(snackDto.toString());

        snackService.updateWithFlavor(snackDto);

        return R.success("修改零食成功");
    }
    /**
     * 根据id删除一个或批量删除菜品。
     *
     * @param ids 待删除的菜品id。
     * @return
     */
    @DeleteMapping
    public R<String> delete(String ids){
        String[] split = ids.split(","); //将每个id分开
        //每个id还是字符串，转成Long
        List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        snackService.removeByIds(idList);//执行批量删除
        log.info("删除的ids: {}",ids);
        return R.success("删除成功"); //返回成功
    }
    @PostMapping("/status/{st}")
    public R<String> setStatus(@PathVariable int st, String ids){
        //处理string 转成Long
        String[] split = ids.split(",");
        List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //将每个id new出来一个Dish对象，并设置状态
        List<Snack> snacks = idList.stream().map((item) -> {
            Snack snack = new Snack();
            snack.setId(item);
            snack.setStatus(st);
            return snack;
        }).collect(Collectors.toList()); //Dish集合

        log.info("status ids : {}",ids);
        snackService.updateBatchById(snacks);//批量操作
        return R.success("操作成功");
    }
    /**
     * 根据条件查询对应的菜品数据
     * @param snack
     * @return
     */
    @GetMapping("/list")
    public R<List<Snack>>list(Snack snack)
    {//构造查询条件
        LambdaQueryWrapper<Snack>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(snack.getCategoryId()!=null,Snack::getCategoryId,snack.getCategoryId());
        //添加条件，只显示在售菜品
        queryWrapper.eq(Snack::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Snack::getSort).orderByDesc(Snack::getUpdateTime);
        List<Snack>list=snackService.list(queryWrapper);
        return R.success(list);
    }


}
