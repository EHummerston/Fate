package items;

public class Weapon {

	private int reach, speed, damage, weight;
	private boolean catalyst, twoHanded;
	
	public Weapon(int reach, int speed, int damage, int weight, boolean catalyst, boolean twoHanded) {
		super();
		this.reach = reach;
		this.speed = speed;
		this.damage = damage;
		this.weight = weight;
		this.catalyst = catalyst;
		this.twoHanded = twoHanded;
	}

	public int getReach() {
		return reach;
	}

	public int getSpeed() {
		return speed;
	}

	public int getDamage() {
		return damage;
	}

	public int getWeight() {
		return weight;
	}

	public boolean isCatalyst() {
		return catalyst;
	}

	public boolean isTwoHanded() {
		return twoHanded;
	}
	
	
	
}
