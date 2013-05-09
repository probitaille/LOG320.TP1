package Tree;

public class Leaf extends Tree 
{
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
