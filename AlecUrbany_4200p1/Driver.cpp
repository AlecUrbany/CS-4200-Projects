#include <iostream>
#include <limits>
#include <string>
#include <algorithm>
#include <cctype>
#include <random>
#include <chrono>
#include "AStarAlgo.h"
#include "Board.h"
using namespace std;
using namespace std::chrono;

bool validatePuzzleInput(string puzzle);
bool isPuzzleSolvable(string puzzle);

int main()
{
	cout << "Please choose from the options below: \n" << endl;

	int choice = 0;
	string puzzle = "";
	unsigned seed;

	while (choice != 4)
	{
		
		cout << "[1] Single Test Random Puzzle" << endl;
		cout << "[2] Input a Single Test Puzzle (sorry no file reading!)" << endl;
		cout << "[3] Multi-Test Random Puzzles (500 randomized puzzles!)" << endl;
		cout << "[4] Exit" << endl;
		cout << "Enter input:  \n";

		if (!(cin >> choice))
		{
			//clears out any input.
			cin.clear();
			//This is so pressing enter doesnt count as an input.
			cin.ignore(numeric_limits<int>::max(), '\n');
			cout << "Invalid input. Integers only.\n" << endl;
			continue;
		}

		//clears out any input.
		cin.clear();
		//This is so pressing enter doesnt count as an input.
		cin.ignore(numeric_limits<int>::max(), '\n');

		if (choice == 1)
		{
				//Our list of numbers that can be used.
				puzzle = "012345678";
				seed = chrono::system_clock::now().time_since_epoch().count();

				do
				{
					shuffle(puzzle.begin(), puzzle.end(), default_random_engine(seed));
				}
				//Keeps checking to see if puzzle is able to be solved.
				while (!isPuzzleSolvable(puzzle));
				//Sets up our board and search engine
				Board board(puzzle);
				AStar astar;
				
			while(true)
			{
				cout << "Choose which Heuristic Value you'd like to see the steps to:"<< endl;
				cout << "[1] H1" << endl;
				cout << "[2] H2" << endl;
				cout << "Enter input:  \n";
				
				if (!(cin >> choice))
				{
					cout << "Invalid input. Integers 1 or 2 only.\n" << endl;
					continue;
				}

				if (choice == 1)
				{
					astar.heuristic1(board);
					return false;
				}

				else if (choice == 2)
				{
					astar.heuristic2(board);
					return false;
				}

				else if (choice != 2)
				{
					cout << "\nInvalid input. Integers 1 or 2 only" << endl;
				}
				cout << endl;
			}
		}

		else if (choice == 2)
		{
			do
			{
				cout << "Enter puzzle:  ";
				getline(cin, puzzle);
				puzzle.erase(remove_if(puzzle.begin(), puzzle.end(), [](unsigned char x){return isspace(x);}), puzzle.end());
			}

			while (validatePuzzleInput(puzzle) == false);

			if (isPuzzleSolvable(puzzle))
			{
				//Creating instances of our puzzle board and A* search
				Board board(puzzle);
				AStar astar;
				while(true)
				{
					cout << "Choose which Heuristic Value you'd like to see the steps to:"<< endl;
					cout << "[1] H1" << endl;
					cout << "[2] H2" << endl;
					cout << "Enter input:  \n";
					
					if (!(cin >> choice))
					{
						cout << "Invalid input. Integers 1 or 2 only.\n" << endl;
						continue;
					}

					if (choice == 1)
					{
						astar.heuristic1(board);
						return false;
					}

					else if (choice == 2)
					{
						astar.heuristic2(board);
						return false;
					}

					else if (choice != 2)
					{
						cout << "\nInvalid input. Integers 1 or 2 only" << endl;
					}
					cout << endl;
				}
			}

			else
			{
				cout << "Cannot solve puzzle\n" << endl;
			}
		}

		else if (choice == 3)
		{
			//Our goal state and list of numbers that can be used.
			puzzle = "012345678";
			AStar astar;

			int cases = 0;
			int tempDepthH1 = 0;
			int tempH1Cost = 0;
			unsigned long tempH1Time = 0;
			int depthH1[25];
			int depthH2[25];
			unsigned long avgTimeH1[25];
			unsigned long avgTimeH2[25];
			unsigned long avgCostH1[25];
			unsigned long avgCostH2[25];
			chrono::high_resolution_clock::time_point timeInitial, timeFinal;

			for (int i = 0; i < 51; ++i)
			{
				depthH1[i] = depthH2[i] = avgCostH1[i] = avgCostH2[i] = 0;
				avgTimeH1[i] = avgTimeH2[i] = 0;
			}


			while(cases < 500)
			{
				seed = chrono::system_clock::now().time_since_epoch().count();

				do
				{
					shuffle(puzzle.begin(), puzzle.end(), default_random_engine(seed));
				}
				//Keeps checking to see if puzzle is able to be solved.
				while (!isPuzzleSolvable(puzzle));
				//Sets up our board and search engine
				Board board(puzzle);
				
				timeInitial = chrono::high_resolution_clock::now();
				astar.heuristic1(board);
				timeFinal = chrono::high_resolution_clock::now();

				if (astar.getSolutionDepth() < 25)
				{
					tempDepthH1 = astar.getSolutionDepth();
					tempH1Cost = astar.getSearchCost();
					tempH1Time = duration_cast<chrono::milliseconds>(timeFinal - timeInitial).count();
				}

				else
				{
					continue;
				}

				timeInitial = chrono::high_resolution_clock::now();
				astar.heuristic2(board);
				timeFinal = chrono::high_resolution_clock::now();

				if (astar.getSolutionDepth() < 25)
				{
					depthH1[tempDepthH1] += 1;
					avgCostH1[tempDepthH1] += tempH1Cost;
					avgTimeH1[tempDepthH1] += tempH1Time;
					depthH2[astar.getSolutionDepth()] += 1;
					avgCostH2[astar.getSolutionDepth()] += astar.getSearchCost();
					avgTimeH2[astar.getSolutionDepth()] += duration_cast<chrono::milliseconds>(timeFinal - timeInitial).count();
					++cases;
				}

				else
				{
					continue;
				}
			}

			//Prinout and calculation for the stats
			cout << "d\tH1 cost\tH1 Cases\tH1 Time\tH2 cost\tH2 Cases\tH2 Time" << endl;
			for (int i = 0; i < 25; ++i)
			{
				if (depthH1[i] == 0 && depthH2[i] == 0)
				continue;

				cout << i << "\t";

				if (depthH1[i] != 0)
				{
					avgCostH1[i] = avgCostH1[i]/depthH1[i];
					avgTimeH1[i] = avgTimeH1[i]/depthH1[i];
				}
				cout << avgCostH1[i] << "\t" << depthH1[i] << "\t" << avgTimeH1[i] << "\t";

				if (depthH2[i] != 0)
				{
					avgCostH2[i] = avgCostH2[i]/depthH2[i];
					avgTimeH2[i] = avgTimeH2[i]/depthH2[i];
				}
				cout << avgCostH2[i] << "\t" << depthH2[i] << "\t" << avgTimeH2[i] << endl;
			}

			cout << endl << "Averages from 500 Random Puzzles" << endl << endl;
			return 0;
		}

		else if (choice != 4)
		{
			cout << "\nInvalid input. Numbers 1-4 only.\n" << endl;
		}
	}

  return 0;
}

bool isPuzzleSolvable(string puzzle)
{	
	int inversionCount = 0;
	for (int i = 0; i < 8; ++i)
	{
		for (int j = i + 1; j < 9; ++j)

		if (puzzle[j] != '0' && puzzle[i] != '0' && puzzle[i] > puzzle[j])
		{
			++inversionCount;
		}
	}

	//If inversions are odd, then it's unsolvable
	return (inversionCount % 2 == 0);
}

bool validatePuzzleInput(string puzzle)
{
	for (int i = 0; i < 9; ++i)
	{
		if (puzzle.find(to_string(i)) == string::npos)
		{
			cout << "\n Puzzle must be 9 numbers long. Puzzle must contain unique values using numbers 0-8.\n" << endl;
			return false;
		}
	}

  cout << endl;
  return true;
}