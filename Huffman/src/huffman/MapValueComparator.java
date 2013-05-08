package huffman;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator implements Comparator<Byte>
{
	Map<Byte, Integer> temp;
	public MapValueComparator(Map<Byte, Integer> temp)
	{
		this.temp = temp;
	}
	
	@Override
	public int compare(Byte arg0, Byte arg1)
	{
		if (temp.get(arg0) >= temp.get(arg1))
			return -1;
		else
			return 1;
	}	
}
