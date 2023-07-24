#include "Board.h"
#include <string>
#include <cstddef>
#include <vector>
#include <cmath>
#include <iostream>
vector<Board> Board::successors()
{
	int currentIndex = stateCurrent.find('0');
	int indexZero = 0;
	vector<Board> children;

	//Movement in all directions
	for (int i = 0; i < 4; ++i)
	{
		string tempState(stateCurrent);

		if (moveChecker(i, indexZero) && moveMaker(tempState, currentIndex, indexZero) != statePrevious)
			{
				Board board(tempState);
				board.setPredecessor(stateCurrent);
				board.setDepth(depth + 1);
				children.push_back(board);
			}
	}

	return children;
}

bool Board::moveChecker(int direction, int& index)
{
	int indexZero = stateCurrent.find('0');

	switch (direction)
	{
		case 0:
			if (indexZero / 3 - 1 >= 0)
			{
				index = indexZero - 3;
				return true;
			}
		break;

		case 1:
			if (indexZero / 3 + 1 < 3)
			{
				index = indexZero + 3;
				return true;
			}
		break;

		case 2:
			if (indexZero % 3 - 1 >= 0)
			{
				index = indexZero - 1;
				return true;
			}
		break;

		case 3:
			if (indexZero % 3 + 1 < 3)
			{
				index = indexZero + 1;
				return true;
			}
		break;
	}

	return false;
}

string Board::moveMaker(string& stringTemp, int src, int dest)
{
	char temp = stringTemp[src];
	stringTemp[src] = stringTemp[dest];
	stringTemp[dest] = temp;
	return stringTemp;
}

int Board::hamming() const
{
	int index = 1;
	int result = 0;

	for (int i = 0; i < 9; ++i)
	{
		if (stateCurrent[i] == '0') continue;
		if (stateCurrent[i] != goal[index])  ++result;
		++index;
	}

	return result;
}

int Board::manhattan() const
{
	int index = 1;
	int goalIndex = 0;
	int result = 0;

	for (int i = 0; i < 9; ++i)
	{
		if (stateCurrent[i] == '0')
		{
			continue;
		}

		if (stateCurrent[i] != goal[index])
		{
			goalIndex = goal.find(stateCurrent[i]);
			
			//Adding the absolute value difference between the rows and columns
			result += abs((i/3) - (goalIndex/3));
			result += abs((i%3) - (goalIndex%3));
		}

		++index;
	}

	return result;
}