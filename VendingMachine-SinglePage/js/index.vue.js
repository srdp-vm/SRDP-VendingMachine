app = new Vue({
  el: '#app',
  data: {
    items: [],
  },
  computed: {
    totalPrice: function () {
      var tprice = 0;
      for (var i = 0, len = this.items.length; i < len; i++) {
        tprice += this.items[i].price * this.items[i].num;
      }
      return tprice;
    },
    carCount: function () {
      return this.items.length;
    }
  }
})
