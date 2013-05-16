package tree;

import java.io.Serializable;

public abstract class Tree implements Comparable<Tree>, Serializable
{
	private static final long serialVersionUID = 8340917866747573270L;
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
