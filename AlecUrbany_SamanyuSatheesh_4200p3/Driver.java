// Project 3 Group Submission by Alec Urbany and Samanyu Satheesh
import java.util.ArrayList;
import java.util.Scanner;
import static java.lang.Character.getNumericValue;
import static java.lang.Character.toLowerCase;

public class Driver
{
	private static ArrayList<Pair> pairs = new ArrayList<>();
	private static ArrayList<Pair> finalPairs = new ArrayList<>();
	private static Board gameBoard;	
	private static int maxSeconds;
	private static long startTime;
	private static boolean foundWin = false;


	public static void main(String[] args)
	{
		boolean computerFirst = true;
		Scanner userInput = new Scanner(System.in);
		
		//Validates user input for yes or no
		System.out.print("Would you like to go first? (y/n): ");
		String playerFirstInput = userInput.nextLine();
		while (!(playerFirstInput.equalsIgnoreCase("y") || playerFirstInput.equalsIgnoreCase("n")))
		{
			System.out.print("Error: Only y or n is accepted.\nPlease try again.\n\n");
			System.out.print("Would you like to go first? (y/n): ");
			playerFirstInput = userInput.nextLine();
		}
		if (playerFirstInput.equals("y")||playerFirstInput.equals("Y"))
		{
			computerFirst = false;
		}

		//Validates user input to make sure its an int
		boolean isInt = false;
		userInput = new Scanner(System.in);
		while (!isInt)
		{
			System.out.print("How long should the computer think (in seconds)? ");
			String inputReader = userInput.nextLine();
			try
			{
				maxSeconds = Integer.parseInt(inputReader);
				isInt = true;
			}
			catch (NumberFormatException e)
			{
				System.out.print("Error: Only integers are accepted.\nPlease try again.\n\n");
			}

		}

		//Now we can create our game board
		gameBoard = new Board(computerFirst);
		gameBoard.print();

		boolean computerWins = false;
		//While the game board is not won, the computer and player will move.
		while (!gameBoard.currentState.winner())
		{
			//Prints out computer's move and empty board state
			if (computerFirst)
			{
				Move computerMove = computerTurn();
				gameBoard.currentState.board[computerMove.row - 1][computerMove.column - 1] = 1;
				gameBoard.addMove(computerMove);

				//Checks to see if the computer wins
				gameBoard.print();
				if (gameBoard.currentState.winner(true))
				{
					computerWins = true;
					break;
				}
			}

			else if (!gameBoard.currentState.isEmpty())
			{
				Move computerMove = computerTurn();
				gameBoard.currentState.board[computerMove.row - 1][computerMove.column - 1] = 1;
				gameBoard.addMove(computerMove);

				//If the computer wins, set the win state to be true
				gameBoard.print();
				if (gameBoard.currentState.winner(true))
				{
					computerWins = true;
					break;
				}
			}

			//Array of each player move
			int[] move = playerTurn(gameBoard.currentState.board);
			gameBoard.currentState.board[move[0]][move[1]] = 2;
			gameBoard.addMove(new Move(2, move[0] + 1, move[1] + 1));
			gameBoard.print();
		}

		if (computerWins) // computer won the game
		{
			System.out.println("Computer wins!");
			System.out.println("Game Over!");
		}
		else // player won the game
		{
			System.out.println("Player wins!");
			System.out.println("Game Over!");
		}
		userInput.close();
	}

	//Checks to see if move is legal
	private static boolean isLegal(int row, int column, int[][] board)
	{
		//Will check the bounds of the moves. Special case to catch the first player move board state
		if ((row < 0 || row >= 8 || column < 0 || column >= 8) && (row == -1 && column == -1))
		{
			return false;
		}

		//Will check the bounds of the moves
		if (row < 0 || row >= 8 || column < 0 || column >= 8)
		{
			System.out.println("Error: space is out of bounds");
			return false;
		}

		//Will check to see if the space is already occupied
		if (board[row][column] == 1 || board[row][column] == 2)
		{
			System.out.println("Error: Space already occupied. Please try another space.");
			return false;
		}
	
		return true;
	}

	private static int[] playerTurn(int[][] board)
	{
		int row = -1;
		int column = -1;

		while (!isLegal(row, column, board))
		{
			String moveIn = "";
			while(moveIn.length() != 2)
			{
				System.out.print("Choose your move: ");
				moveIn = new Scanner(System.in).nextLine();
			}
			System.out.println();
			//Cheeky fix to allow for lowercase values
			row = (int) toLowerCase(moveIn.charAt(0)) - 97;
			column = getNumericValue(moveIn.charAt(1)) - 1;
		}

		return new int[]{row, column};
	}	

	private static Move computerTurn()
	{
		search(gameBoard.currentState);
		return getBestMove();
	}

	//Function to find the best move for the computer to make
	private static Move getBestMove()
	{
		State bestState = finalPairs.get(0).currentState;
		int bestValue = finalPairs.get(0).value;
		for (Pair pair : finalPairs)
		{
			if (pair.value > bestValue)
			{
				bestValue = pair.value;
				bestState = pair.currentState;
			}
			
			else if (pair.value == bestValue && pair.currentState.computerComparer() > bestState.computerComparer())
			{
				bestState = pair.currentState;
			}
		}

		Move bestMove = gameBoard.currentState.getMove(bestState);

		return bestMove;
	}
	
	//Timer function
	private static double timer()
	{
		return (((System.nanoTime() - startTime))/1000000000.0);
	}
	
	//Function to see if we've reached the max amount of seconds.
	private static boolean outOfTime()
	{
		return timer() > maxSeconds;
	}

	//Our search function to find the best move
	private static void search(State root)
	{
		startTime = System.nanoTime();
		for (int i = 1; i <= 500; i++)
		{
			pairs.clear();

			int result = alphaBeta(root, root, i, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

			//If we run out of time, will terminate the function and let us know
			if (result == -1)
			{
				System.out.println("Max time reached");
				break;
			}

			//Goes through as many depths as possible in order to get the best option
			finalPairs.clear();
			for (Pair pair : pairs)
				finalPairs.add(new Pair(pair));

			if (finalPairs.size() > 0)
				{
					//Uncomment this if you want to see the depths
					//System.out.println("Depth " + i + ": " + timer() + " seconds");
				}

			if (foundWin)
			{
				foundWin = false;
				break;
			}
		}
		System.out.printf("Optimal move found in: %.3f Seconds\n", timer());
		System.out.println();
	}

	//Our alpha beta algorithm
	private static int alphaBeta(State root, State currentState, int depth, int alpha, int beta, boolean computer)
	{
		if (currentState.winner(!computer))
		{
			foundWin = true;
			if (currentState.isChild(root, !computer))
				{
					pairs.add(new Pair(999999999, currentState));
				}

			return currentState.computerComparer();
		}

		if (depth == 0)
		{
			if (currentState.isChild(root, !computer))
				{
					pairs.add(new Pair(currentState.computerComparer(), currentState));
				}

			return currentState.computerComparer();
		}

		//Using our list of next moves
		ArrayList<State> successors = currentState.successors(computer);

		int value;
		//If and else statement for whether or not the current player is a computer
		if (computer)
		{
			value = Integer.MIN_VALUE;
			for (State child : successors)
			{
				value = Math.max(value, alphaBeta(root, child, depth - 1, alpha, beta, false));
				alpha = Math.max(alpha, value);

				if (outOfTime())
				return -1;

				if (alpha >= beta)
				break;
			}
		}
		else
		{
			value = Integer.MAX_VALUE;
			for (State child : successors)
			{
				value = Math.min(value, alphaBeta(root, child, depth - 1, alpha, beta, true));
				beta = Math.min(beta, value);

				if (outOfTime())
				return -1;

				if (alpha >= beta)
				break;
			}
		}

		if (currentState.isChild(root, !computer))
		pairs.add(new Pair(value, currentState));

		return value;
	}

}

//Class for pair. Used to easily find any pairs
class Pair
{
	int value;
	State currentState;

	public Pair(int value, State currentState)
	{
		this.value = value;
		this.currentState = currentState;
	}

	public Pair(Pair copyPair)
	{
		this.value = copyPair.value;
		this.currentState = new State(copyPair.currentState);
	}
}

//Class to define what a move is
class Move
{
	private int marker;
	int row;
	int column;

	public Move(int marker, int row, int column)
	{
		this.marker = marker;
		this.row = row;
		this.column = column;
	}
	
	//Converts unreadable information from the moves into a readable string
	public String translator()
	{
		return (char) (65 + row - 1) + Integer.toString(column);
	}
}