package tools;

import java.awt.Color;

public class ColourSets {

	public static final Color[]
			ENVIRONMENT_WOOD = {
		new Color(80,64,48),
		new Color(112,96,48),
		new Color(152,128,72),
		new Color(200,176,104)
	},

	ENVIRONMENT_LEAVES = {
		new Color(40,64,48),
		new Color(40,152,56),
		new Color(96,248,32),
		new Color(216,248,56)
	},

	ENVIRONMENT_GRASS = {
		new Color(127,173,113),
		new Color(135,201,113),
		new Color(176,228,106)
	},

	ENVIRONMENT_SKY = {
		new Color(121,201,249),
		new Color(136,208,249),
		new Color(156,215,249),
		new Color(176,222,249),
		new Color(189,228,249)
	},
	
	ENVIRONMENT_SEA = {
			new Color(105,146,242),
			new Color(134,173,242),
			new Color(155,201,242)
		},
	
	ENVIRONMENT_WOOD_SPOOKY = {
			new Color(64,64,64),
			new Color(96,96,96),
			new Color(128,128,128),
			new Color(176,176,176)
		},

	ENVIRONMENT_GRASS_SPOOKY = {
			
			new Color(127,113,173),
			new Color(135,113,201),
			new Color(176,106,228)
		},
	
	ENVIRONMENT_SKY_SPOOKY = {
		new Color(249,201,121),
		new Color(249,208,136),
		new Color(249,215,156),
		new Color(249,222,176),
		new Color(249,228,189)
	},
	
	ENVIRONMENT_SEA_SPOOKY = {
			new Color(242,146,105),
			new Color(242,173,134),
			new Color(242,201,155)
		},
	
	ENVIRONMENT_LEAVES_SPOOKY = {
				new Color(48,64,40),
				new Color(56,152,40),
				new Color(32,248,96),
				new Color(56,248,216)
			},

	METAL_GOLD = {
		new Color(249,201,121),
		new Color(249,208,136),
		new Color(249,215,156),
		new Color(249,222,176),
		new Color(249,228,189)
	};
	
	
	
	public static Color[] HSVSet(float h1, float s1, float v1, float h2, float s2, float v2, int depth)
	{
		Color[] c = new Color[depth];
		
		for(int i = 0; i < depth; i++)
		{
			float f;
			if(depth>1)
			{
				f = (float)i/(depth-1);
			}
			else
			{
				f = 0;
			}
			
			float h = (h2-h1)*f + h1;
			float s = (s2-s1)*f + s1;
			float v = (v2-v1)*f + v1;
			
			c[i] = ColourTools.HSVtoRGB(h, s, v);
			
			
		}
		
		return c;
		
	}
}


