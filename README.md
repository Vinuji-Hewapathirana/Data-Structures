# Data-Structures & Algorithms

This is focused on implementing a program for an autonomous robot to navigate through a grid-based environment with obstacles. The created Java code has implemented the program using A* (A-star) search algorithm to find the optimal path from its starting point to a goal position while avoiding collisions with obstacles. The algorithm calculates the shortest path considering both the actual costs of movement and a heuristic estimate of the remaining distance to the goal. 

## Overview
- **‘Node’ Class** –  This Node class which is used in the “RobotGrid” class defines a node structure representing a position on the grid.   
- **‘PriorityQueue’ Class** – This class implements a priority queue data structure which is used to store nodes during the search process. 
- **‘Grid’ Class** – This class represents the grid environment (2D array – grid dimensions given by the user) where the pathfinding takes place. It allows setting start position, goal position which are placed according to the user inputs and obstacles which are placed randomly. 
- **‘PathDirections’ Class** – This class includes a custom implementation of an ArrayList used to store the directions of the path followed by the robot. 
- **‘main’ Method** – This is the entry point of the program. It prompts the user to input grid dimensions, start position, and goal position. Then it generates obstacles randomly within the grid, finds the shortest path using A* search, and visualizes the grid along with the path and the relevant cell coordinates.
- **‘visualizeGrid’ Method** – This method is responsible for visually representing the grid. It prints out the grid with different symbols.
- **‘findShortestPath’ Method** – This method implements the A* search algorithm to find the optimal path between the start and goal positions. It considers obstacles and calculates the heuristic using the Manhattan distance (which considers only the main four directions around the robot).  
- **‘heuristic’ Method** – This method calculates the heuristic value between two points (Manhattan distance). It calculates the absolute differences in x and y coordinates between two points, sums them up and provide the shortest distance between the points.
- **‘getPathDirections’ Method** – This is used to determine the directions of the path followed by the robot. It iterates through the grid array and adds the coordinates of the cells marked as part of the path (value 4) to a collection, indicating the sequence of movements made by the robot. 
- **‘printPathDirections’ Method** – This method prints out the directions of the path followed by the robot from the start position to the goal position with the relevant cell coordinates. It takes the sequence of cell coordinates obtained from the “getPathDirections” method and calculates the direction of movement between consecutive cells using “calculateDirection” methods. 
- **‘calculateDirection’ Method** – This method calculates the direction of movement between two points. It compares the x and y coordinates of the current and next points, and determine the direction of the movement. 
- **‘pathFound’ Method** – This checks whether a valid path has been found from the start position to the goal position. It iterates through the grid array and returns true if any cell in the grid is marked as part of the path (value 4), else it returns false. 
