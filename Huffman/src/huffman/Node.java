package huffman;

@SuppressWarnings("rawtypes")
public class Node implements Comparable
{
	private byte character;
	private int frequency;
	private Node leftBranch;
	private Node rightBranch;
	
	public Node(byte character, int frequency, Node leftBranch, Node rightBranch)
	{
		this.character = character;
		this.frequency = frequency;
		this.leftBranch = leftBranch;
		this.rightBranch = rightBranch;
	}
	
	public Node(byte character, int frequency)
	{
		this.character = character;
		this.frequency = frequency;
		this.leftBranch = null;
		this.rightBranch = null;
	}
		
	public Node(Node leftBranch, Node rightBranch)
	{
		//TODO check char fusion
		if (leftBranch.getCharacter() < rightBranch.getCharacter())
			this.character = leftBranch.getCharacter();
		else
			this.character = rightBranch.getCharacter();
		this.frequency = leftBranch.getFrequency() + rightBranch.getFrequency();
		this.leftBranch = leftBranch;
		this.rightBranch = rightBranch;
	}
	
	public int compareTo(Object arg)
    {
            Node other = (Node) arg;
            if (this.frequency == other.frequency)
                    return this.character-other.character;
            else
                    return this.frequency-other.frequency;
    }
	
	private boolean isLeaf()
	{
		if (getLeftBranch() == null && getRightBranch() == null)
			return false;
		else
			return true;
	}
	
	public void printPath(String path)
	{
		if (isLeaf())
			System.out.println(getCharacter() + " " + path);
		if (leftBranch != null)
			leftBranch.printPath(path + '0');
		if (rightBranch != null)
			rightBranch.printPath(path + '1');
	}

	public byte getCharacter()
	{
		return character;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
		
	public Node getLeftBranch()
	{
		return leftBranch;
	}
		
	public Node getRightBranch()
	{
		return rightBranch;
	}
}
