#include "AStarAlgo.h"
#include <queue>
#include <unordered_map>
#include <stdexcept>
#include <unordered_set>
#include <cstring>
#include <iostream>
#include <array>

//Heuristic 1 and 2 are identical minus the cout and the fact that they use different heuristics.
void AStar::heuristic1(Board& initial)
{
	cost = 0;
	depth = 0;

	priority_queue<Board, vector<Board>, compareH1> priorityQueue;
	unordered_map<int, vector<Board>> states;
	priorityQueue.push(initial);
	search(priorityQueue, states);
	cout << "Hamming - H1 cost = " << cost << endl;
}

void AStar::heuristic2(Board& initial)
{
	cost = 0;
	depth = 0;

	priority_queue<Board, vector<Board>, compareH2> priorityQueue;
	unordered_map<int, vector<Board>> states;
	priorityQueue.push(initial);

	search(priorityQueue, states);
	cout << "Manhattan - H2 cost = " << cost << endl;
}

void TraverseString(string &str, int length)
{
	//A rather hackey solution to get better printing of the steps
	int i = 0;
	int digit = 0;
	int row = 0;
	int iteration = 0;
	for (auto &ch : str) 
	{
		if (isdigit(ch) == 1)
		{
			if (digit == 2)
			{
				cout<< ch << endl;
				digit = 0;
				i++;

			}

			else
			{
				cout<< ch << " ";
				digit++;
				i++;
			}
		}

		else if (ch == ':')
		{
			if (iteration == 0)
			{
				cout<< ch << " " << iteration << " (our inputted puzzle)";
				iteration++;
				i++;
			}

			else
			{
				cout<< ch << " " << iteration;
				iteration++;
				i++;
			}			
		}
		
		else
		{
			cout<< ch;
			i++;
		}
	}
	cout << endl;
}

//Our A* search
//This is heuristic 1
void AStar::search(priority_queue<Board, vector<Board>, compareH1>& priorityQueue, unordered_map<int, vector<Board>>& node)
{
	Board puzzle = priorityQueue.top();

	while (!puzzle.isGoal())
	{
		if (priorityQueue.empty()) return;
		{
			++cost;
		}

		puzzle = priorityQueue.top();
		priorityQueue.pop();

		try
		{
			node[puzzle.getDepth()].push_back(puzzle);
		}
		
		catch (const out_of_range& oor)
		{
			vector<Board> vectorBoard;
			vectorBoard.push_back(puzzle);
			node[puzzle.getDepth()] = vectorBoard;
		}

		for (Board& success: puzzle.successors())
		{
			//If it's not visited
			if (visited.find(success.getState()) == visited.end())
			{
				priorityQueue.push(success);
			}
		}

		visited.insert(puzzle.getState());
	}

	//Once the puzzle has been successfully solved, we can backtrack
	visited.clear();
	string bestSteps = puzzle.getState();
	string current = puzzle.getState();

	//Backtracking is done here
	for (int d = puzzle.getDepth(); d >= 0; --d)
	{
		for (Board& board: node[d])
		{
			if (board.getState() == current && board.predecessor() != "")
			{
				string iteration = to_string(d);
				bestSteps = "Step:\n" + board.predecessor() + "\n" + bestSteps;
				current = board.predecessor();
			}
		}
	}

	depth = puzzle.getDepth();
	TraverseString(bestSteps, bestSteps.size());
	cout << "^Final Step \n" << endl << endl;
	cout << "Depth = " << puzzle.getDepth() << ", ";
}

//Heuristic 2 A* search
void AStar::search(priority_queue<Board, vector<Board>, compareH2>& priorityQueue, unordered_map<int, vector<Board>>& node)
{
	Board puzzle = priorityQueue.top();

	while (!puzzle.isGoal())
	{
		if (priorityQueue.empty()) return;
		{
			++cost;
		}

		puzzle = priorityQueue.top();
		priorityQueue.pop();

		try
		{
			node[puzzle.getDepth()].push_back(puzzle);
		}

		catch (const out_of_range& oor)
		{
			vector<Board> vectorBoard;
			vectorBoard.push_back(puzzle);
			node[puzzle.getDepth()] = vectorBoard;
		}

		for (Board& success: puzzle.successors())
		{
			//If it's not visited
			if (visited.find(success.getState()) == visited.end())
			{
				priorityQueue.push(success);
			}
		}

		visited.insert(puzzle.getState());
	}
	
	//Once the puzzle has been successfully solved, we can backtrack
	visited.clear();
	string bestSteps = puzzle.getState();
	string current = puzzle.getState();

	//Backtracking is done here
	for (int d = puzzle.getDepth(); d >= 0; --d)
	{
		for (Board& board: node[d])
		{
			if (board.getState() == current && board.predecessor() != "")
			{
				string iteration = to_string(d);
				bestSteps = "Step:\n" + board.predecessor() + "\n" + bestSteps;
				current = board.predecessor();
			}
		}
	}

	depth = puzzle.getDepth();
	TraverseString(bestSteps, bestSteps.size());
	cout << "^Final Step \n" << endl << endl;
	cout << "Depth = " << puzzle.getDepth() << ", ";
}
