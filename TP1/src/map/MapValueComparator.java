package map;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator implements Comparator<Character>
{
	Map<Character, Integer> temp;
	public MapValueComparator(Map<Character, Integer> temp)
	{
		this.temp = temp;
	}
	
	@Override
	public int compare(Character arg0, Character arg1)
	{
		if (temp.get(arg0) >= temp.get(arg1))
			return -1;
		else
			return 1;
	}	
}

