package vo;

import po.Item;

public class ItemVO extends Item{
	private String imgPath;

	public ItemVO() {
		super();
	}

	public ItemVO(int id, String name, double price, int num, String imgPath) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.num = num;
		this.imgPath = imgPath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", price=" + price + ", num=" + num + ", imgPath=" + imgPath + "]";
	}

}

