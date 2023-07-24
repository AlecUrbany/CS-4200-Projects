#ifndef BOARD_H
#define BOARD_H
#include <string>
#include <cstddef>
#include <vector>
#include <cmath>
#include <iostream>
using namespace std;

//Defining what string is a winning state
const string goal = "012345678";

class Board
{
	private:
	//Keeps track of our last state
	string statePrevious;
	//Keeps track of the current state
	string stateCurrent;
	//How many steps to complete the puzzle
	int depth;
	//Checks to make sure move is valid
	bool moveChecker(int direction, int& index);
	//Makes the move
	string moveMaker(string& tempString, int source, int destination);


	public:
	//Description of our board
	Board(string stateOriginal) : stateCurrent(stateOriginal), statePrevious("")
	{
		depth = 0;
	}

	//Getter for our state
	string getState() const
	{
		return stateCurrent;
	}
	vector<Board> successors();

	//Setters and getters for depth
	void setDepth(int d)
	{
		depth = d;
	}
	int getDepth() const
	{
		return depth;
	}
	
	//Setter for predecessor state.
	void setPredecessor(string& previous) 
	{
		statePrevious = previous;
	}
	string predecessor()
	{
		return statePrevious;
	}
	//Checker to see if it's at the goal state
	bool isGoal() const 
	{
		return stateCurrent == goal;
	}
	
	//Heuristic 1
	int hamming() const;

	//Heuristic 2
	int manhattan() const;
};

#endif
