import java.util.ArrayList;
import java.util.Arrays;

public class State
{
	int[][] board;

	public State()
	{
		board = new int[8][8];
	}

	private State(int[][] board)
	{
		this.board = board;
	}

	public State(State copyState)
	{
		this.board = Arrays.stream(copyState.board).map(r -> r.clone()).toArray(int[][]::new);
	}

	//Compares the computer's and players scores
	public int computerComparer()
	{
		return (checker(true) - checker(false));
	}

	//Checks to see if they player is a computer
	public int isComputer(boolean computer)
	{
		if (computer)
		{
			return 1;
		}

		else
		{
			return 2;
		}	
	}

	//Our perverbial hat-trick. This function will figure out whether the current player has won, as well as the heuristic values for the AI
	private int checker(boolean computer)
	{
		//Our heuristic value. Will change depending on board state, as well as whether or not the computer winners.
		int heuristic = 0;

		int player = isComputer(computer);

		//Checks for a winner, if computer winners, rewards them generously
		if (winner(computer))
		heuristic += 1000000000;

		//Goes through each column and row to check for empty and occupied spaces
		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == 0)
				{
					int emptySpaces = 0;
					int yourSpaces = 0;
					int enemySpaces = 0;
					boolean isOccupied = false;

					//Checks the column going up 3 spaces
					for (int i = 1; i <= 3; i++)
					{
						if (row - i >= 0)
						{
							int next = board[row - i][column];
							//If there is nothing, increment empty spaces
							if (next == 0)
							{
								emptySpaces++;
							}
							
							//If there's something there' it's the enemy
							else if (next != player)
							{
								enemySpaces++;
								isOccupied = true;
							}
							
							//It's your space
							else
							{
								yourSpaces++;
							}
						}
					}
					//Takes the data we just learned to add points to the heuristic
					heuristic += calculateHeuristic(isOccupied, emptySpaces, yourSpaces, enemySpaces);

					
					emptySpaces = 0;
					yourSpaces = 0;
					enemySpaces = 0;
					isOccupied = false;

					//Checks the column going down 3 spaces
					for (int i = 1; i <= 3; i++)
					{
						if (row + i < 8)
						{ // In bounds?
							int next = board[row + i][column];
							//If there is nothing, increment empty spaces
							if (next == 0)
							{
								emptySpaces++;
							}
							
							//If there's something there' it's the enemy
							else if (next != player)
							{
								enemySpaces++;
								isOccupied = true;
							}
							
							//It's your space
							else
							{
								yourSpaces++;
							}
						}
					}
					//Takes the data we just learned to add points to the heuristic
					heuristic += calculateHeuristic(isOccupied, emptySpaces, yourSpaces, enemySpaces);

					
					emptySpaces = 0;
					yourSpaces = 0;
					enemySpaces = 0;
					isOccupied = false;
					//Checks the row going left 3 spaces
					for (int i = 1; i <= 3; i++) { // Get next 3 spaces in direction
						if (column - i >= 0)
						{
							int next = board[row][column - i];
							//If there is nothing, increment empty spaces
							if (next == 0)
							{
								emptySpaces++;
							}
							
							//If there's something there' it's the enemy
							else if (next != player)
							{
								enemySpaces++;
								isOccupied = true;
							}
							
							//It's your space
							else
							{
								yourSpaces++;
							}
						}
					}
					//Takes the data we just learned to add points to the heuristic
					heuristic += calculateHeuristic(isOccupied, emptySpaces, yourSpaces, enemySpaces);

					emptySpaces = 0;
					yourSpaces = 0;
					enemySpaces = 0;
					isOccupied = false;

					//Checks the row going right 3 spaces
					for (int i = 1; i <= 3; i++)
					{
						if (column + i < 8)
						{
							int next = board[row][column + i];
							//If there is nothing, increment empty spaces
							if (next == 0)
							{
								emptySpaces++;
							}
							
							//If there's something there' it's the enemy
							else if (next != player)
							{
								enemySpaces++;
								isOccupied = true;
							}
							
							//It's your space
							else
							{
								yourSpaces++;
							}
						}
					}
					//Takes the data we just learned to add points to the heuristic
					heuristic += calculateHeuristic(isOccupied, emptySpaces, yourSpaces, enemySpaces);
				}
			}
		}

		return heuristic;
	}

	//calculates our heuristic. Our program wants as many points as possible
	private static int calculateHeuristic(boolean isOccupied, int emptySpaces, int yourSpaces, int enemySpaces)
	{
		if (yourSpaces == 3)
		{
			return 1000000; // the higher the value of that state, the higher the number returned
		}

		else if (yourSpaces == 2)
		{
			if (enemySpaces == 0)
			{
				return 1000;
			}
			else
			{
				return 100;
			}
		}
		
		else if (yourSpaces == 1)
		{
			if (enemySpaces == 0)
			{
				return 250;
			}
			else
			{
				return 25;
			}
		}

		if (enemySpaces == 3)
		{
			return 10000;
		}
		
		else if (enemySpaces == 2)
		{
			return 1500;
		}
		
		else if (enemySpaces == 1)
		{
			return 100;
		}
		
		//Incase everything is empty
		return 25;
	}

	//Returns who the winner is
	public boolean winner()
	{
		return winner(true) || winner(false);
	}

	//Takes in the whether or not the current player is a computer, Will then return
	public boolean winner(boolean computer)
	{
		boolean winner = false;

		int player = isComputer(computer); // determine if we're working with the player or computer

		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == player)
				{
					//up and down wins
					if (row <= 8 - 4 && board[row + 1][column] == player && board[row + 2][column] == player && board[row + 3][column] == player)
					{
						winner = true;
					}

					//left and right wins
					if (column <= 8 - 4 && board[row][column + 1] == player && board[row][column + 2] == player && board[row][column + 3] == player)
					{
						winner = true;
					}
				}
			}
		}

		return winner;
	}

	//Gets the next move
	public Move getMove(State nextState)
	{
		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == 0 && nextState.board[row][column] == 1)
				return new Move(1, row + 1, column + 1);
			}
		}
		return null;
	}

	//Checks to see if board state is empty
	public boolean isEmpty()
	{
		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				if (board[row][column] != 0)
				return false;
			}
		}

		return true;
	}

	//Looks for next state to get to
	public ArrayList<State> successors(boolean computer)
	{
		ArrayList<State> results = new ArrayList<>();

		int player = isComputer(computer); // check who current player is

		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				int currentSpace = board[row][column];

				if (currentSpace == 0)
				{
					int[][] newState = Arrays.stream(board).map(r -> r.clone()).toArray(int[][]::new);

					newState[row][column] = player;
					results.add(new State(newState));
				}
			}
		}

		return results;
	}

	//Checks to see if the move is a child state
	public boolean isChild(State state, boolean computer)
	{
		int differences = 0;

		int player = isComputer(computer); // check who the player is

		for (int row = 0; row < 8; row++)
		{
			for (int column = 0; column < 8; column++)
			{
				if (state.board[row][column] != board[row][column])
				{
					if (state.board[row][column] == 0 && board[row][column] == player)
					{
						differences += 1; // count the number of differences between states
					}

					else
					{
						return false;
					}
				}
			}
		}

		if (differences == 1) // if there is exactly one difference, this means it is a child of the state
		{
			return true;
		}

		else
		{
			return false;
		}
	}
}
