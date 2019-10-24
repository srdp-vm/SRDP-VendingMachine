package vo;

public class Instruction {
	private String operation;
	private ItemVO item;

	public Instruction() {
		super();
	}

	public Instruction(String operation, ItemVO item) {
		super();
		this.operation = operation;
		this.item = item;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public ItemVO getItem() {
		return item;
	}

	public void setItem(ItemVO item) {
		this.item = item;
	}

	@Override
	public String toString() {
		if (item != null) {
			return "Instruction [operation=" + operation + ", item=" + item.toString() + "]";
		} else {
			return "Instruction [operation=" + operation + ", item=" + item + "]";
		}
	}

}
