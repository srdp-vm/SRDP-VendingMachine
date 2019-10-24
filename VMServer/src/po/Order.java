package po;

import java.util.ArrayList;

public class Order {
	private String userId;
	private ArrayList<Item> items;
	private int count;
	private double totalprice;

	public Order() {
		super();
		this.items = new ArrayList<>();
		this.count = 0;
		this.totalprice = 0;
	}

	public Order(String userId, ArrayList<Item> items, int count, double totalprice) {
		super();
		this.userId = userId;
		this.items = items;
		this.count = count;
		this.totalprice = totalprice;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

	@Override
	public String toString() {
		return "Order [items=" + items + ", count=" + count + ", totalprice=" + totalprice + "]";
	}
	
	public void addItem(Item item) {
		for(Item i : items) {
			if(i.getId() == item.getId()) {
				i.setNum(i.getNum()+1);
				return;
			}
		}
		items.add(item);
	}
	
	public void delItem(Item item) {
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getId() == item.getId()) {
				if(items.get(i).getNum() == 1) {
					items.remove(i);
					break;
				} else {
					items.get(i).setNum(items.get(i).getNum() - 1);
				}
			}
		}
	}
}
