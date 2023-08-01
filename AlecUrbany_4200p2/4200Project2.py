import random
import time

while True:
	mode = int(input("Enter Which Algorithm you want.\nSteepest Hill Climb [1] Genetic [2] Exit [3]\n"))

	#Steepest Hill Climb
	if mode == 1:
		print("-------Steepest Hill Climbing-------")

		#prints the board for our Hill Climbing Algorithm
		def printBoardHill(board, n):
			print("Board State:")
			for i in range(len(board)):
				print(str(board[i]) + " ", end="")
				if (i + 1)%n == 0:
					print()
			print("Heuristic Cost: ", getHeuristics(board, n))
			print()

		#Function to print out total stats from our runs
		def printStats(iterations, successes, runTime):
			print()
			print("-----STATS-----")
			print("Total Runs: " + str(iterations))
			print("Total Successes: " + str(successes))
			print("Success rate: " + str(successes / iterations))
			print("Average Runtime: " + str(runTime) + " seconds")

		#Calculates whether or not there are collisions between pieces
		def collisionFinder(board, n):
			collisions = 0
			for i in range(len(board)):
				#Looks at each queen and adds up the amount of collisions
				if board[i] == 1:
					for x in range(1, n):
						if ((i-(n*x)) >= 0):
							up = (i-(n*x))
							#Looking up
							if (board[up] == 1):
								collisions += 1
							#Looking diagonal up left
							if (int((up-x)/n) == int(up/n)) and (up-x) >= 0:
								upLeft = (up-x)
								if (board[upLeft] == 1):
									collisions += 1
							#Looking diagonal up right
							if (int((up+x)/n) == int(up/n)):
								upRight = (up+x)
								if (board[upRight] == 1):
									collisions += 1
						# checking below current index
						if ((i+(n*x)) < len(board)):
							down = (i+(n*x))
							#Looking down 
							if (board[down] == 1):
								collisions += 1
							#Looking diagonal down left
							if (int((down-x)/n) == int(down/n)):
								downLeft = down - x
								if (board[downLeft] == 1):
									collisions += 1
							#Looking diagonal down right
							if (int((down+x)/n) == int(down/n)) and ((down+x) < len(board)):
								downRight = (down+x)
								if (board[downRight] == 1):
									collisions += 1
							#No need to look left or right, as we place queens randomly by row
			return collisions

		#Calculates how many colisions are present on the board. If it's zero, we have a success
		def getHeuristics(board, n):
			collisions = collisionFinder(board, n)
			#Small janky fix - Cuts our colliisons in half due to helper function counting positions twice
			return int(collisions / 2)

		#Gets children and finds their successor
		def getChild(board, n):
			child = []
			heuristicCost = getHeuristics(board, n)

			for row in range(n):
				#Makes a temporary board
				for col in range(n):
					boardTemp = []
					boardTemp.extend(board[:row * n])
					new_row = [0] * n
					new_row[col] = 1
					boardTemp.extend(new_row)
					boardTemp.extend(board[(row + 1) * n:])
					tempHeuristic = getHeuristics(boardTemp, n)
					if (boardTemp != board) and (tempHeuristic < heuristicCost):
						child = boardTemp.copy()
						heuristicCost = tempHeuristic
			return child

		#Creates a random board state to work off of.
		def createBoard(n):
			generated_board = []
			for i in range(n):
				j = random.randint(0, n - 1)
				row = [0] * n
				row[j] = 1
				generated_board.extend(row)
			return generated_board

		#The actual algorithm
		def steepestHillClimb(board, n, iterations, printState=False):
			steps = 0
			success = False
			latestBoard = board.copy()

			if (printState):
				printBoardHill(latestBoard, n)

			#Will continually search for an answer until the max iteration is released. This is done through the child with the smallest heuristic
			for i in range(iterations):
				nextNode = getChild(latestBoard, n).copy()

				if (printState and len(nextNode) != 0):
					printBoardHill(nextNode, n)
				steps += 1

				#If we get a child with no heuristic cost, an answer has been found
				if (len(nextNode) != 0) and (getHeuristics(nextNode, n) == 0):
					success = True
					break

				#If we get a child with a heuristic cost, no answer can be found
				if (len(nextNode) == 0):
					break

				#adds the board to the list
				latestBoard = nextNode.copy()
			return steps, success


		if __name__ == "__main__":
				n = int(input("Enter Number of Queens: "))
				iterations = int(input("Enter Number of Iterations: "))
				successes = False
				successfulSteps = 0
				failedSteps = 0
				start = 0
				end = 0
				start = time.time()
				for i in range(iterations):
					print("Iteration = " + str(i + 1) + ":")
					step_count, success = steepestHillClimb(createBoard(n), n, iterations, printState=True)
					if (success):
						print("-------Success-------")
						print("Steps needed: " + str(step_count))
						print()
						successfulSteps += step_count
					else:
						print("-------Failure-------")
						print("Steps needed: " + str(step_count))
						print()
						failedSteps += step_count
					successes += success
				end = time.time()
				runTime = round(((end-start)/iterations), 4)
				printStats(iterations, successes, runTime)
				exit()

	#Genetic Algorithm
	if mode == 2:
		print("-------Genetic Algorithm-------")
		#Generate random chromosomes
		def createChromosome(size):
			return [random.randint(1, n) for _ in range(n)]
		
		#Makes the board that will be printed out in the end
		def boardMaker():
			board = []
			for x in range(n):
				board.append(["0"] * n)
			for i in range(n):
				board[n-output[i]][i]= "1"
			return board
		

		#random chromosome mutation
		def mutate(x): 
			length = len(x)
			mutation = random.randint(1, length)
			c = random.randint(0, length-1)
			x[c] = mutation
			return x

		#reproduce and cross the different chromosomes		
		def reproduce(x, y):
			n = len(x)
			c = random.randint(0, n - 1)
			return x[0:c] + y[c:n]
		
		def printBoardGenetic(board):
			for row in board:
				print(" ".join(row))
		
		def printGenes(chrom):
			print("Queen Positions (Chromosomes): " + str(chrom) + " Fitness: " + str(fitness(chrom)))

		#Calculates the fitness in the chromosome
		def fitness(chromosome):
			horizHits = sum([chromosome.count(queen)-1 for queen in chromosome])/2
			diagHits = 0
			n = len(chromosome)
			diagL = [0] * (2*n)
			diagR = [0] * (2*n)

			for i in range(n):
				diagL[i + chromosome[i] - 1] += 1
				diagR[len(chromosome) - i + chromosome[i] - 2] += 1

			diagHits = 0
			for i in range(2*n-1):
				counter = 0
				if diagL[i] > 1:
					counter += diagL[i]-1
				if diagR[i] > 1:
					counter += diagR[i]-1
				diagHits += counter / (n-abs(i-n+1))
			return int(fitnessMax - (horizHits + diagHits))
		
		#Tells you the odds (probability)
		def dontTellMeTheOdds(chromosome, fitness):
			return fitness(chromosome)/fitnessMax

		#Randomly selects populations after zipping them
		def randSelect(population, probabilities):
			probPop = zip(population, probabilities)
			total = sum(w for c, w in probPop)
			rando = random.uniform(0, total)
			incrementer = 0
			for c, w in zip(population, probabilities):
				if incrementer + w >= rando:
					return c
				incrementer += w

		#Algorithm for queen placement and changes
		def queenAlgo(population, fitness):
			popNew = []
			probabilities = [dontTellMeTheOdds(n, fitness) for n in population]
			mutationProb = 0.03

			#Picks the best two chromosomes and creates two new ones
			for i in range(len(population)):
				x = randSelect(population, probabilities)
				y = randSelect(population, probabilities)
				child = reproduce(x, y)
				if random.random() < mutationProb:
					child = mutate(child)
				printGenes(child)
				popNew.append(child)
				if fitness(child) == fitnessMax:
					break
			return popNew

		if __name__ == "__main__":
			n = int(input("Enter Number of Queens: "))

			population = [createChromosome(n) for _ in range(100)]	
			#Calculates our fitness. Ideally for 8-queens, we want a fitness score of 28 for the solution
			fitnessMax = (n*(n-1))/2

			generation = 1

			start = time.time()
			while not fitnessMax in [fitness(chrom) for chrom in population]:
				print("------------ GENERATION " + str(generation) + " ------------")
				population = queenAlgo(population, fitness)
				print()
				print("Max Fitness = " + str(max([fitness(n) for n in population])))
				generation += 1
			print("-------Success-------")
			print("Generations Needed: " + str(generation-1))
			print()

			output = []
			for chrom in population:
				if fitness(chrom) == fitnessMax:
					output = chrom
					print("Solution: ")
					printGenes(chrom)

			end = time.time()
			runTime = round((end-start), 4)	
			board = boardMaker()
			
			print()
			printBoardGenetic(board)
			print()
			print("-----STATS-----")
			print("Solved by generation: " + str(generation-1))
			print("Average Runtime: " + str(runTime) + " seconds")
			print()
			exit()

	if mode == 3:
		exit()

	if mode != 1 or 2 or 3:
		print("Error, please input a number from 1-3")
		print()
		continue