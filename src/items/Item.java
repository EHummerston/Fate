package items;

public class Item {
	private String nameIdentifier;
	private String nameOutput;
	private String description;
	private int purchaseCost;
	private int itemMod;
	
	public Item(String weaponIdentifier)
	{
		this.nameIdentifier = weaponIdentifier;
		this.nameOutput = "Halberd";
		this.description = "omg can I have a poll of your arm hue";
		this.purchaseCost = 8;
		itemMod = 0;
	}

	public String getNameOutput() {
		return nameOutput;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(int purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public String getNameIdentifier() {
		return nameIdentifier;
	}

	public int getItemMod() {
		return itemMod;
	}

	public void setItemMod(int itemMod) {
		this.itemMod = itemMod;
	}
	
}
