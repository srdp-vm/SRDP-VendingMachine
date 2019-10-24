package po;

public class Item {
	protected int id;
	protected String name;
	protected int num;
	protected double price;

	public Item() {
		super();
	}

	public Item(int id, String name, int num, double price) {
		super();
		this.id = id;
		this.name = name;
		this.num = num;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", name=" + name + ", num=" + num + ", price=" + price + "]";
	}

}
