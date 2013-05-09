package Tree;

public class Node extends Tree 
{
	private Tree leftBranch;
	private Tree rightBranch;
	
	public Node(Tree leftBranch, Tree rightBranch)
	{
		super(leftBranch.getFrequency() + rightBranch.getFrequency());
		this.leftBranch = leftBranch;
		this.rightBranch = rightBranch;
	}
	
	public Tree getLeftBranch()
	{
		return this.leftBranch;
	}
	
	public Tree getRightBranch()
	{
		return this.rightBranch;
	}
}
