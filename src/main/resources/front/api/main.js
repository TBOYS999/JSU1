//获取所有的菜品分类
function categoryListApi() {
    return $axios({
      'url': '/category/list',
      'method': 'get',
    })
}

function categoryListApi1() {
    return $axios({
        'url': '/category1/list',
        'method': 'get',
    })
}

function categoryListApi2() {
    return $axios({
        'url': '/category2/list',
        'method': 'get',
    })
}


//获取菜品分类对应的菜品
function dishListApi(data) {
    return $axios({
        'url': '/dish/list',
        'method': 'get',
        params:{...data}
    })
}

function dishListApi1(data) {
    return $axios({
        'url': '/dish1/list',
        'method': 'get',
        params:{...data}
    })
}

function dishListApi2(data) {
    return $axios({
        'url': '/dish2/list',
        'method': 'get',
        params:{...data}
    })
}

//获取菜品分类对应的套餐
function setmealListApi(data) {
    return $axios({
        'url': '/setmeal/list',
        'method': 'get',
        params:{...data}
    })
}
function setmealListApi1(data) {
    return $axios({
        'url': '/setmeal1/list',
        'method': 'get',
        params:{...data}
    })
}
function setmealListApi2(data) {
    return $axios({
        'url': '/setmeal2/list',
        'method': 'get',
        params:{...data}
    })
}
//获取购物车内商品的集合
function cartListApi(data) {
    return $axios({
        'url': '/shoppingCart/list',
        'method': 'get',
        params:{...data}
    })
}

//购物车中添加商品
function  addCartApi(data){
    return $axios({
        'url': '/shoppingCart/add',
        'method': 'post',
        data
      })
}

//购物车中修改商品
function  updateCartApi(data){
    return $axios({
        'url': '/shoppingCart/sub',
        'method': 'post',
        data
      })
}

//删除购物车的商品
function clearCartApi() {
    return $axios({
        'url': '/shoppingCart/clean',
        'method': 'delete',
    })
}

//获取套餐的全部菜品
function setMealDishDetailsApi(id) {
    return $axios({
       // 'url': `/setmeal/${id}`,
        'url': '/front/cartData.json',
        'method': 'get',
    })
}

// function setMealDishDetailsApi1(id) {
//     return $axios({
//         'url': `/setmeal1/dish1/${id}`,
//         'method': 'get',
//     })
// }
//
// function setMealDishDetailsApi2(id) {
//     return $axios({
//         'url': `/setmeal2/${id}`,
//         'method': 'get',
//     })
// }
//
//
