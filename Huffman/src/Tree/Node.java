package Tree;

public class Node extends Tree 
{
	private static final long serialVersionUID = 5408698763868611692L;
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
