package tree;

public class Leaf extends Tree 
{
	private static final long serialVersionUID = -7768085031731316566L;
	private char character;
	
	public Leaf(char character, int frequency)
	{
		super(frequency);
		this.character = character;
	}
	
	public char getCharacter()
	{
		return this.character;
	}
}
