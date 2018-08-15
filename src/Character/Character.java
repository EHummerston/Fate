package Character;
import items.Armour;
import items.Armoury;
import items.Item;

import java.util.ArrayList;


public class Character {

	private int gold;
	
	private int strength, agility, propagation, resonance;

	private ArrayList<Item> equippedWeapons;
	private Item[] equippedArmour;
	private int rightEquipped, leftEquipped;

	public Character ()
	{
		equippedWeapons = new ArrayList<Item>();
		equippedArmour = new Item[Armour.ARMOUR_SLOT_MAX];
		for(int i = 0; i < Armour.ARMOUR_SLOT_MAX; i++)
		{
			equippedArmour[i] = new Item ("UNARMOURED");
		}
		
		equippedWeapons.add(new Item("UNARMED"));
		
		equipRight("UNARMED");
		equipLeft("UNARMED");
		
		strength = 5;
		agility = 5;
		propagation = 5;
		resonance = 5;

	}

	public void equipRight(String weaponIdentifier)
	{
		int equippable = -1;
		for(int i = 0; (i < equippedWeapons.size()) && (equippable < 0); i++)
		{
			if(equippedWeapons.get(i).getNameIdentifier().equals(weaponIdentifier))
			{
				equippable = i;
			}	
		}
		if(equippable >= 0)
		{

			rightEquipped = equippable;
			System.out.println("Equipped right hand: " + equippedWeapons.get(rightEquipped).getNameOutput());

			if(Armoury.getWeapon(equippedWeapons.get(leftEquipped).getNameIdentifier()).isTwoHanded())
			{
				equipLeft("UNARMED");
			}
			if(Armoury.getWeapon(equippedWeapons.get(rightEquipped).getNameIdentifier()).isTwoHanded())
			{
				leftEquipped = rightEquipped;
			}
		}
		else
		{
			System.out.println("Couldn't equip right hand weapon: " + weaponIdentifier);
		}
	}

	public void equipLeft(String weaponIdentifier)
	{
		int equippable = -1;
		for(int i = 0; (i < equippedWeapons.size()) && (equippable < 0); i++)
		{
			if((equippedWeapons.get(i).getNameIdentifier().equals(weaponIdentifier)) &&
					!(Armoury.getWeapon(equippedWeapons.get(i).getNameIdentifier()).isTwoHanded()))
			{
				equippable = i;
			}	
		}

		if(equippable >= 0)
		{


			leftEquipped = equippable;
			System.out.println("Equipped left hand: " + equippedWeapons.get(leftEquipped).getNameOutput());

			if(Armoury.getWeapon(equippedWeapons.get(leftEquipped).getNameIdentifier()).isTwoHanded())
			{
				equipLeft("UNARMED");
			}
			if(Armoury.getWeapon(equippedWeapons.get(rightEquipped).getNameIdentifier()).isTwoHanded())
			{
				leftEquipped = rightEquipped;
			}
		}
		else
		{
			System.out.println("Couldn't equip left hand weapon: " + weaponIdentifier);
		}
	}

	public int getGold() {
		return gold;
	}

	public void offsetGold(int offset) {
		this.gold += offset;
	}

	public int getStrength() {
		return strength;
	}

	public int getAgility() {
		return agility;
	}

	public int getPropagation() {
		return propagation;
	}

	public int getResonance() {
		return resonance;
	}

	public ArrayList<Item> getEquippedWeapons() {
		return equippedWeapons;
	}

	public Item[] getEquippedArmour() {
		return equippedArmour;
	}

	public int getRightEquipped() {
		return rightEquipped;
	}

	public int getLeftEquipped() {
		return leftEquipped;
	}
}
