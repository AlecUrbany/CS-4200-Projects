#ifndef ASTARALGO_H
#define ASTARALGO_H
#include <queue>
#include <unordered_map>
#include <stdexcept>
#include <unordered_set>
#include "Board.h"
using namespace std;

//Heurisic 1
struct compareH1
{
	bool operator()(const Board& left, const Board& right)
	{
		return (left.hamming() + left.getDepth()) > (right.hamming() + right.getDepth());
	}
};

//Heurisic 2
struct compareH2
{
	bool operator()(const Board& left, const Board& right)
	{
		return (left.manhattan() + left.getDepth()) > (right.manhattan() + right.getDepth());
	}
};

//Class for A* search
class AStar
{
	private:
		int cost;
		int depth;
		unordered_set<string> visited;
		//Whether we use H1 or H2
		void search(priority_queue<Board, vector<Board>, compareH1>& priorityQueue, unordered_map<int, vector<Board>>& node);
		void search(priority_queue<Board, vector<Board>, compareH2>& priorityQueue, unordered_map<int, vector<Board>>& node);
	public:
		//
		AStar() : visited()
		{
			cost = 0; depth = 0;
		}

		//Hamming Heuristic
		void heuristic1(Board& initial);

		//Manhatten Heuristic
		void heuristic2(Board& initial);

		//Getter to get how many steps it took to get our solution
		int getSolutionDepth()
		{
			return depth;
		}

		//Getter to see how much the solution cost
		int getSearchCost()
		{
			return cost;
		}
};

#endif
