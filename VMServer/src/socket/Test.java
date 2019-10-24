package socket;

import com.google.gson.Gson;

import po.Order;
import vo.Instruction;

public class Test {

	public static void main(String[] args) {
		String json = "{\"operation\":\"checkout\",\"item\":{\"items\":[{\"id\":6,\"name\":\" 崂山 白花蛇草水风味饮料 330ml\",\"num\":3,\"price\":9},{\"id\":2,\"name\":\"可口可乐 Coca-Cola 汽水 碳酸饮料 330ml\",\"num\":1,\"price\":2.5}],\"count\":2,\"totalprice\":29.5}}";
		String orderJson = json.substring(json.indexOf("\"item\":")+7, json.lastIndexOf("}"));
		System.out.println(orderJson);
		Instruction instruction2 = new Gson().fromJson(json, Instruction.class);
		System.out.println(instruction2);
		Order order = new Gson().fromJson(orderJson, Order.class);
		System.out.println(order);
	}
}
