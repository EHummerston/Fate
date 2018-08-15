package Character;

public class Title {

	final static String[] words = {
		//A
		"abyss",
		"amber",
		"ancient",
		"arcane",
		"armour",
		"armoured",
		"arms",
		"assassin",
		"axe",
		"azure",
		//B
		"bane",
		"battle",
		"beast",
		"behemoth",
		"blade",
		"blood",
		"breaker",
		"bringer",
		//C
		"carrion",
		"chaos",
		"charm",
		"charming",
		"cloud",
		//D
		"dark",
		"death",
		"deft",
		"demon",
		"doom",
		"dragon",
		"druid",
		"duel",
		"duelist",
		//E
		"eagle",
		"earth",
		"ebon",
		"elder",
		"emerald",
		//F
		"fey",
		"fiend",
		"fire",
		"flame",
		"forest",
		"fox",
		"frost",
		//G
		"giant",
		"gold",
		"golden",
		"grand",
		"great",
		"grim",
		"guard",
		"guardian",
		//H
		"havoc",
		"hawk",
		"hex",
		"holy",
		"hunter",
		//I
		"ice",
		"iron",
		//J

		//K
		"keeper",
		"knight",
		//L
		"life",
		"light",
		"lightning",
		"lion",
		"lord",
		//M
		"master",
		"mage",
		"magic",
		"malevolence",
		"malevolent",
		"moon",
		"monk",
		"mountain",
		//N
		"night",
		//O
		"obsidian",
		"oracle",
		"order",
		//P
		"pain",
		"paladin",
		"prayer",
		"priest",
		"protector",
		//Q

		//R
		"ranger",
		"rascal",
		"razor",
		"rogue",
		"runner",
		//S
		"sacred",
		"sage",
		"sanguine",
		"scarlet",
		"shadow",
		"shaman",
		"silent",
		"silver",
		"sky",
		"slayer",
		"soldier",
		"sorcerer",
		"soul",
		"spear",
		"spectre",
		"spirit",
		"stalker",
		"steel",
		"storm",
		"sun",
		"swift",
		"sword",
		//T
		"tempest",
		"templar",
		"thief",
		//U
		"unholy",
		//V

		//W
		"walker",
		"warden",
		"warlock",
		"war",
		"warrior",
		"wind",
		"witch",
		"wizard",
		"wolf",
		//X

		//Y

		//Z


	};

	final static String[] conjunctions = {
		"",
		"-",
		" ",
		//		", ",
		//		", the ",
		" of ",
		" of the ",
	};

	int[] word;
	int[] conjuncts;
	boolean the;

	public Title(int[] titleID, int[] conID, boolean thethe)
	{
		word = new int[titleID.length];
		for(int i = 0; i < titleID.length; i++)
		{
			word[i] = Math.abs(titleID[i])%(words.length);
		}

		if(word.length > 0)
		{
			conjuncts = new int[word.length - 1];
		}
		else
		{
			conjuncts = new int[0];
		}


		for(int i = 0; i < conjuncts.length; i++)
		{
			if(conID != null)
			{
				if(i<conID.length)
				{
					conjuncts[i] = Math.abs(conID[i])%(conjunctions.length);
				}
				else
				{

					conjuncts[i] = 0;	
				}
			}
			else
			{
				conjuncts[i] = 0;	
			}

		}
		//word = titleID;
		the = thethe;

	}

	public static int[] toIntArray(String[] input)
	{
		int[] output = new int[input.length];
		for(int i = 0; i < output.length; i++)
		{
			if(input[i].toCharArray()[0]<97)
			{
				char[] charray = input[i].toCharArray();
				charray[0] += 32;
				input[i]=String.valueOf(charray);
			}
			for(int j = 0; j < words.length; j++)
			{
				if(input[i] == words[j])
				{
					output[i]=j;
					break;
				}
			}
		}
		return output;
	}

	public static int[] toIntArrayConjunctions(String[] input)
	{
		int[] output = new int[input.length];
		for(int i = 0; i < output.length; i++)
		{
			for(int j = 0; j < conjunctions.length; j++)
			{
				if(input[i] == conjunctions[j])
				{
					output[i]=j;
					break;
				}
			}
		}
		return output;
	}
	

	public String toString(boolean sentence)
	{

		String title = new String();
		if(word.length > 0)
		{

			if(sentence)
			{
				if(the)
				{
					title += "the ";
				}
				else
				{
					if((words[word[0]].toCharArray()[0] == 'a')
							||(words[word[0]].toCharArray()[0] == 'e')
							||(words[word[0]].toCharArray()[0] == 'i')
							||(words[word[0]].toCharArray()[0] == 'o')
							||(words[word[0]].toCharArray()[0] == 'u') )
					{
						title += "an ";
					}
					else
					{
						title += "a ";
					}
				}
			}

			for(int i = 0; i < word.length; i++)
			{

				String addWord=words[word[i]];

				if((i==0)||(conjuncts[i-1] != 0))
				{
					char[] addWordCharray = addWord.toCharArray();

					addWordCharray[0] -= 32;

					addWord = String.valueOf(addWordCharray);
				}


				title+=addWord;

				if((i!=word.length-1))
				{
					title+=conjunctions[conjuncts[i]];
				}
			}

		}
		return title;
	}
	
	public static char Constanant (int charID, boolean caps)
	{
		//Pass an int between 0 and 20 to simulate an array of characters.
		charID++;
		int[] vowels = {1,5,9,15,21};
		for(int i = 0; i < vowels.length; i++)
		{
			if(charID >= vowels[i])
			{
				charID++;
			}
		}
		if(caps)
		{
			charID+=64;
		}
		else
		{
			charID += 96;
		}
		return (char) charID;
	}
	
	public static char Vowel (int charID, boolean caps)
	{
		//Pass an int between 0 and 4 to return the appropriate vowel.
		switch(charID)
		{
		case 0:
			charID = 'a';
			break;
		case 1:
			charID = 'e';
			break;
		case 2:
			charID = 'i';
			break;
		case 3:
			charID = 'o';
			break;
		case 4:
			charID = 'u';
			break;
		}
		if(caps)
		{
			charID -= 32;
		}
		return (char)charID;
	}
	
}


