import java.util.ArrayList;

public class Board
{
	private ArrayList<Move> movementList = new ArrayList<>();
	public State currentState = new State();
	private boolean computerFirst;

	public Board(boolean computerFirst)
	{
		this.computerFirst = computerFirst;
	}

	//Turns the board into a string
	private String boardString()
	{
		StringBuilder result = new StringBuilder();
		//This is for the top row, where we see the column names
		result.append("  ");

		//This covers the spacing between the columns
		for (int i = 1; i <= 8; i++)
		{
			result.append(i + " ");
		}

		//Newline to begin the Rows
		result.append("\n");

		//Cheeky solution to print the letters.
		//We take the ascii values and increment up the list as we go on
		int letter = 65;
		for (int i = 0; i < 8; i++)
		{
			result.append((char) letter + " ");

			//Spaces between each board column space
			for (int j = 0; j < 8; j++)
			{
				char marker = whoMoved(currentState.board[i][j]);
				result.append(marker + " ");
			}

			//Newline for the next row and letter
			result.append("\n");
			letter++;
		}

		result.append("\n");

		return result.toString();
	}

	@Override
	public String toString()
	{
		return stringAdjacent(boardString(), moveList(computerFirst)); // combining the board representation and the move list
	}

	public void print()
	{
		System.out.println(toString());
	}

	//Add a move to the list of movements
	public void addMove(Move move)
	{
		movementList.add(move);
	}

	//Takes movement data and will display a symbol on the board according to whether the player moved or computer moved
	private static char whoMoved(int mover)
	{
		char result;
		//No Moves
		if (mover == 0)
		{
			result = '-';
			return result;
		}

		//Computer Moves
		if (mover == 1)
		{
			result = 'X';
			return result;
		}

		//Player Moves
		if (mover == 2)
		{
			result = 'O';
			return result;
		}

		//Incase there's an error
		else
		{
			result = ' ';
			return result;
		}

	}

	//The literal list of moves taken
	private String moveList(boolean computerFirst)
	{
		StringBuilder result = new StringBuilder();

		//Selects our title
		if (!computerFirst)
		{
			result.append("Player vs. Computer");
		}

		else
		{
			result.append("Computer vs. Player");
		}

		for (int i = 0; i < movementList.size(); i++)
		{
			String listOfMoves = movementList.get(i).translator();
			//Lists out each move. Checks for whether it's even or odd. 
			if (i % 2 == 0)
			{
				result.append("\n  " + (i / 2 + 1) + ". " + listOfMoves);
			}

			else
			{
				result.append(" " + listOfMoves);
			}
		}

		return result.toString();
	}

	// the board and move list need to be printed side by side, so this method splits and prints them integrated together
	private String stringAdjacent(String string1, String string2)
	{
		//Will be used to break up the strings for the board and the movelist
		String splitter = "\n";

		//Dictates the placement of the player vs computer logo
		String inserter = "  ";

		// split the data into individual parts
		String[] string1Split = string1.split(splitter);
		String[] string2Split = string2.split(splitter);

		//Gets which string is the longest
		int maxLength = 0;
		for (String s : string1Split)
		{
			if (maxLength < s.length())
				{
					maxLength = s.length();
				}
		}

		//Goes through and rebuilds the string with strings 1 and 2 combined.
		StringBuilder b = new StringBuilder();
		int largerLength = Math.max(string1Split.length, string2Split.length);
		for (int i = 0; i < largerLength; i++)
		{
			//Works on adding back string 1
			if (i < string1Split.length)
			{
				b.append(string1Split[i]);
			}
			//Works on adding back string 2
			if (i < string2Split.length)
			{
				if (i >= string1Split.length)
				{
					//Adds in a buffer so that the move list is still in line with the table when the table ends
					b.append("                  ");
				}
				b.append(inserter).append(string2Split[i]);
			}

		b.append("\n");
		}

		return b.toString();
	}

}
