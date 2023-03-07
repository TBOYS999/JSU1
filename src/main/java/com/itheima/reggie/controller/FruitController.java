package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.FruitDto;
import com.itheima.reggie.entity.Categoryc;
import com.itheima.reggie.entity.Fruit;
import com.itheima.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/dish2")
@Slf4j
public class FruitController {
    @Autowired
    private FruitService fruitService;

    @Autowired
    private CategorycService categorycService;

    /**
     * 新增菜品
     * @param fruitDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody FruitDto fruitDto){
        log.info(fruitDto.toString());

        fruitService.saveWithFlavor(fruitDto);

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
        Page<Fruit> pageInfo = new Page<>(page, pageSize);
        Page<FruitDto> fruitDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Fruit> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Fruit::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Fruit::getUpdateTime);

        //执行分页查询
        fruitService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,fruitDtoPage,"records");//records是一个列表集合，不需要拷贝

        List<Fruit> records = pageInfo.getRecords();

        List<FruitDto> list = records.stream().map((item) -> {
            FruitDto snackDto = new FruitDto();

            BeanUtils.copyProperties(item,snackDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Categoryc categoryb = categorycService.getById(categoryId);

            if(categoryb != null){
                String categoryName = categoryb.getName();
                snackDto.setCategoryName(categoryName);
            }
            return snackDto;
        }).collect(Collectors.toList());

        fruitDtoPage.setRecords(list);

        return R.success(fruitDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<FruitDto> get(@PathVariable Long id){

       FruitDto snackDto = fruitService.getByIdWithFlavor(id);
//        Snack snack=snackService.getByIdWithFlavor(id);

       return R.success(snackDto);
//        return R.success(snack);
    }

    /**
     * 修改菜品
     * @param fruitDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody FruitDto fruitDto){
        log.info(fruitDto.toString());

        fruitService.updateWithFlavor(fruitDto);

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
        fruitService.removeByIds(idList);//执行批量删除
        log.info("删除的ids: {}",ids);
        return R.success("删除成功"); //返回成功
    }
    @PostMapping("/status/{st}")
    public R<String> setStatus(@PathVariable int st, String ids){
        //处理string 转成Long
        String[] split = ids.split(",");
        List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //将每个id new出来一个Dish对象，并设置状态
        List<Fruit> snacks = idList.stream().map((item) -> {
            Fruit fruit = new Fruit();
            fruit.setId(item);
            fruit.setStatus(st);
            return fruit;
        }).collect(Collectors.toList()); //Dish集合

        log.info("status ids : {}",ids);
        fruitService.updateBatchById(snacks);//批量操作
        return R.success("操作成功");
    }
    /**
     * 根据条件查询对应的菜品数据
     * @param snack
     * @return
     */
    @GetMapping("/list")
    public R<List<Fruit>>list(Fruit snack)
    {//构造查询条件
        LambdaQueryWrapper<Fruit>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(snack.getCategoryId()!=null,Fruit::getCategoryId,snack.getCategoryId());
        //添加条件，只显示在售菜品
        queryWrapper.eq(Fruit::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Fruit::getSort).orderByDesc(Fruit::getUpdateTime);
        List<Fruit>list=fruitService.list(queryWrapper);
        return R.success(list);
    }


}
