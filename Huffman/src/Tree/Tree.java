package Tree;

public abstract class Tree implements Comparable<Tree>  
{
	private int frequency;
	
	public Tree(int frequency)
	{
		this.frequency = frequency;
	}
	
	public int compareTo(Tree tree)
	{
		return this.getFrequency() - tree.getFrequency();
	}
	
	public int getFrequency()
	{
		return this.frequency;
	}
}
