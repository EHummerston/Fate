package items;

public class Armour {
	private int physicalDefence, nonphysicalDefence, flexibility, imbued, slot;
	
	public static final int ARMOUR_SLOT_HEAD = 0,
							ARMOUR_SLOT_SHOULDERS = 1,
							ARMOUR_SLOT_CHEST = 2,
							ARMOUR_SLOT_ARMS = 3,
							ARMOUR_SLOT_LEGS = 4,
							ARMOUR_SLOT_FEET = 5,
							ARMOUR_SLOT_BACK = 6,
							ARMOUR_SLOT_MAX = 6;

	public Armour(int physicalDefence, int nonphysicalDefence, int flexibility,
			int imbued, int slot) {
		super();
		this.physicalDefence = physicalDefence;
		this.nonphysicalDefence = nonphysicalDefence;
		this.flexibility = flexibility;
		this.imbued = imbued;
		this.slot = slot;
		
	}

	public int getPhysicalDefence() {
		return physicalDefence;
	}

	public int getNonphysicalDefence() {
		return nonphysicalDefence;
	}

	public int getFlexibility() {
		return flexibility;
	}

	public int getImbued() {
		return imbued;
	}
	
	public int getSlot() {
		return slot;
	}
}
