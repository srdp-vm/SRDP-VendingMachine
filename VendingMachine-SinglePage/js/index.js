class Instrcution {
  constructor(operation, item) {
    this.operation = operation;
    this.item = item;
  }
}

function loadPageIn(loaderstyle) {
  //loadpage载入时，transition作用class为in
  $('#transition').removeClass();
  $('#transition').addClass('in');
  $('#loadpage').show();
  //200ms之后显示loader
  setTimeout(function () {
    $('#loader').removeClass();
    $('#loader').addClass(loaderstyle);
    $('#loader').show();
  }, 200);
  document.title = "Loading...";
}

function loadPageOut() {
  //loadpage退出时，作用class为out
  $('#transition').removeClass();
  $('#transition').addClass('out');
  //500ms之后隐藏loader
  setTimeout(function () {
    $('#loader').hide();
  }, 500);
  //等待退出动画播放完毕后隐藏页面
  setTimeout(function () {
    $('#loadpage').hide();
  }, 750);
}

$('button#open').click(function () {
  //向服务器发送开门消息
  var instruction = new Instrcution('open', null);
  sendMessage(JSON.stringify(instruction));
  //index页面隐藏，载入load页面，标题改为loading
  $('#index').hide();
  loadPageIn('ajaxloader');
  //loadpage显示800ms之后
  setTimeout(function () {
    //loadpage消失
    loadPageOut();
    //显示shoppingcar
    $('#shoppingcar').show();
    //标题改为shoppingc
    document.title = "ShoppingCar";
    $("[rel='icon']").attr('href', 'img/shoppingcar.ico');
  }, 800);
});

//滚动条滚动时淡入header的方法
$(document).ready(function () {
  $(document).scroll(function () {
    var scrolltop = $(document).scrollTop();
    if (scrolltop <= 100) {
      var opacity = scrolltop / 100;
      $('#header').css('opacity', opacity);
    } else {
      $('#header').css('opacity', 1);
    }
  });
});

//屏幕滚动到指定的div
function gotoItem(id) {
  var div_item = document.getElementById(id);
  //height 为div距离body顶端的距离
  var height = div_item.offsetTop;
  //height减掉header的高度 再减掉20的缓冲距离
  height = height - 50 - 20;
  //滚动到此位置
  $(document).scrollTop(height);
  console.log("gotoItem called");
}

//判断商品是否存在，返回其在数组中的下标，如果没有找到返回-1
function findItem(item) {
  for (let i = 0; i < app.items.length; i++) {
    if (item.id == app.items[i].id) {
      return i;
    }
  }
  return -1;
}

//添加
function add(item) {
  var index = findItem(item);
  if (index == -1) {
    addItem(item);
  } else {
    addCount(index);
  }
}

//添加一条商品
function addItem(item) {
  app.items.push(item);
}

//增加商品index的数量
function addCount(index) {
  app.items[index].num++;
}

//删除
function del(item) {
  var index = findItem(item);
  if (index != -1) {
    var num = app.items[index].num;
    if (num > 1) {
      decCount(index);
    } else {
      delItem(index);
    }
  }
}

//删除一条商品
function delItem(index) {
  app.items.splice(index, 1);
}

//减少商品index的数量
function decCount(index) {
  app.items[index].num--;
}

//结算,生成订单
//订单每一条目的Class
class OrderItem {
  constructor(id, name, num, price) {
    this.id = id;
    this.name = name;
    this.num = num;
    this.price = price;
  }
}

//订单Class
class Order {
  constructor(items, count, totalprice) {
    this.items = items;
    this.count = count;
    this.totalprice = totalprice;
  }
}

//遍历Vue.data生成订单order，转换成json备用（发送给服务器）
function settleUp() {
  var items = new Array();
  app.items.forEach(item => {
    var orderItem = new OrderItem(item.id, item.name, item.num, item.price);
    items.push(orderItem);
  });
  var order = new Order(items, app.carCount, app.totalPrice);
  var instruction = new Instrcution('checkout', order);
  sendMessage(JSON.stringify(instruction));
  $('#shoppingcar').hide();
  loadPageIn('loader-ball');
}
