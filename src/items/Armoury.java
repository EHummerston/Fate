package items;

public class Armoury {

	public static Weapon getWeapon(String weaponName)
	{
		Weapon returnWeapon;
		//							reach, speed, damage, weight, catalyst
		//							Total should equal 11, being a catalyst is two points.
		switch(weaponName)
		{

		default:
			returnWeapon = new Weapon (0 , 0, 0, 0, false, false);
			break;

		case "HALBERD":
			returnWeapon = new Weapon ( 4, 2, 3, 2, false, true ) ;
			break;

		case "GREATAXE":
			returnWeapon = new Weapon ( 3, 1, 4, 3, false, true ) ;
			break;

		case "STAFF":
			returnWeapon = new Weapon ( 3, 1, 2, 3, true, true ) ;
			break;

		case "LONGBOW":
			returnWeapon = new Weapon ( 8, 1, 3, 1, false, true ) ;
			break;
		}

		return returnWeapon;

	}

	public static Armour getArmour(String armourName)
	{
		Armour returnArmour;
		//							physicalDefence, nonphysicalDefence, flexibility, imbued, slot
		
		switch(armourName)
		{

		default:
			returnArmour = new Armour (0,0,0,0,0);
			break;

		}

		return returnArmour;

	}

}

