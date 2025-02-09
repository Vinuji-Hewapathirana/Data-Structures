import java.util.Scanner;

public class RobotGrid {

    //Represents the nodes in the grid
    static class Node {
        int x, y;
        Node parent;
        double cost;
        double heuristic;

        Node(int x, int y, Node parent, double cost, double heuristic) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        //Method to compare nodes based on cost and heuristic
        boolean isSmallerThan(Node other) {
            return (this.cost + this.heuristic) < (other.cost + other.heuristic);
        }
    }

    //Class to store nodes based in their priority
    static class PriorityQueue {
        private Node[] heap;
        private int size;

        public PriorityQueue(int capacity) {
            heap = new Node[capacity];
            size = 0;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        //Adds a node to the queue
        public void add(Node node) {
            heap[size++] = node;
            int current = size - 1;
            while (current > 0 && heap[current].isSmallerThan(heap[parent(current)])) {
                swap(current, parent(current));
                current = parent(current);
            }
        }
        //Remove and return the node with the highest priority
        public Node poll() {
            Node node = heap[0];
            heap[0] = heap[--size];
            heap[size] = null;
            heapify(0);
            return node;
        }

        private void heapify (int i) {
            int left = leftChild(i);
            int right = rightChild(i);
            int smallest = i;
            if (left < size && heap[left].isSmallerThan(heap[smallest]))
                smallest = left;
            if (right < size && heap[right].isSmallerThan(heap[smallest]))
                smallest = right;
            if (smallest != i) {
                swap(i, smallest);
                heapify(smallest);
            }
        }

        private int parent(int i) {
            return (i - 1) / 2;
        }
        private int leftChild(int i) {
            return 2 * i + 1;
        }
        private int rightChild(int i) {
            return 2 * i + 2;
        }
        private void swap(int i, int j) {
            Node temp = heap[i];
            heap[i] = heap[j];
            heap[j] = temp;
        }
    }

    //Represents the grid environment
    static class Grid {
        private int[][] grid;
        private int rows;
        private int columns;

        public Grid(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            grid = new int[rows][columns];
        }

        public void placeObstacles(int numObstacles) {
            int obstaclesPlaced = 0;
            while (obstaclesPlaced < numObstacles) {
                int y = (int) (Math.random() * rows);
                int x = (int) (Math.random() * columns);
                if (grid[y][x] == 0) {
                    grid[y][x] = 1;
                    obstaclesPlaced++;
                }
            }
        }

        public void setStartPosition(int y, int x) {
            grid[y][x] = 2;
        }

        public void setGoalPosition(int y, int x) {
            grid[y][x] = 3;
        }

        public int[][] getGrid() {
            return grid;
        }
        public int getRows() {
            return rows;
        }
        public int getColumns() {
            return columns;
        }
    }

    //Custom implementation of ArrayList for storing path directions
    static class PathDirections {
        private String[] array;
        private int size;
        private static final int DEFAULT_CAPACITY = 10;

        public PathDirections() {
            array = new String[DEFAULT_CAPACITY];
            size = 0;
        }

        public void add(String direction) {
            if (size == array.length) {
                resize();
            }
            array[size++] = direction;
        }

        private void resize() {
            String[] newArray = new String[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }

        public String get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of bounds");
            }
            return array[index];
        }

        public int size() {
            return size;
        }
    }

    public static void main(String[] args) {
        System.out.println("Obstacles are indicated with '/'");
        System.out.println("Robot is indicated with 'R'");
        System.out.println("Goal position is indicated with 'G'");
        System.out.println("Path followed by the robot is indicated with 'P'");

        Scanner input = new Scanner(System.in);

        int rows, columns;
        do {
            System.out.println("\nEnter the number of rows for the grid (Maximum value is 50) : ");
            rows = input.nextInt();
            if (rows > 50) {
                System.out.println("Error: Number of rows cannot be greater than 50.");
            }
        } while (rows > 50);
        do {
            System.out.println("Enter the number of columns for the grid (Maximum value is 50) : ");
            columns = input.nextInt();
            if (columns > 50) {
                System.out.println("Error: Number of columns cannot be greater than 50.\n");
            }
        } while (columns > 50);

        Grid grid = new Grid(rows, columns);

        // Get start position
        int startY, startX;
        do {
            System.out.println("Enter the cell number of the row for the start position (0 to " + (rows - 1) + "): ");
            startY = input.nextInt();
            System.out.println("Enter the cell number of the column for the start position (0 to " + (columns - 1) + "): ");
            startX = input.nextInt();

            if (startY < 0 || startY >= rows || startX < 0 || startX >= columns) {
                System.out.println("Error: Start position is out of bounds. Please enter valid coordinates.\n");
            }
        } while (startY < 0 || startY >= rows || startX < 0 || startX >= columns);

        grid.setStartPosition(startY, startX);

        // Get goal position
        int goalY, goalX;
        do {
            System.out.println("Enter the cell number of the row for the goal position (0 to " + (rows - 1) + "): ");
            goalY = input.nextInt();
            System.out.println("Enter the cell number of the column for the goal position (0 to " + (columns - 1) + "): ");
            goalX = input.nextInt();

            if (goalY < 0 || goalY >= rows || goalX < 0 || goalX >= columns) {
                System.out.println("Error: Goal position is out of bounds. Please enter valid coordinates.\n");
            }
        } while (goalY < 0 || goalY >= rows || goalX < 0 || goalX >= columns);

        grid.setGoalPosition(goalY, goalX);

        int numObstacles = (int) (0.3 * rows * columns);
        grid.placeObstacles(numObstacles);
        findShortestPath(grid);
        visualizeGrid(grid.getGrid());
        PathDirections pathDirections = new PathDirections();

        // Check if a path was found before printing the path coordinates
        if (pathFound(grid.getGrid())) {
            System.out.println("\nRobot moved through the cells in this order: ");
            getPathDirections(grid.getGrid(), pathDirections);
            printPathDirections(pathDirections, grid.getGrid());
        } else {
            System.out.println("\nRobot moved through the cells in this order: \nNo path as there are too many obstacles.");
        }
    }

    //Prints the grid console
    public static void visualizeGrid(int[][] grid) {
        System.out.println(); // Print a blank line before printing the grid
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 0) {
                    System.out.print(". "); // Empty cell
                } else if (grid[i][j] == 1) {
                    System.out.print("/ "); // Obstacle cell
                } else if (grid[i][j] == 2) {
                    System.out.print("R "); // Start cell
                } else if (grid[i][j] == 3) {
                    System.out.print("G "); // Goal cell
                } else if (grid[i][j] == 4) {
                    System.out.print("P "); // Path cell
                }
            }
            System.out.println();
        }
    }

    //Implements the A* algorithm to find the optimal path
    public static void findShortestPath(Grid grid) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int rows = grid.getRows();
        int columns = grid.getColumns();

        PriorityQueue openList = new PriorityQueue(rows * columns);
        boolean[][] visited = new boolean[rows][columns];

        int startX = -1, startY = -1, goalX = -1, goalY = -1;

        int[][] gridArray = grid.getGrid();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (gridArray[i][j] == 2) {
                    startX = j;
                    startY = i;
                } else if (gridArray[i][j] == 3) {
                    goalX = j;
                    goalY = i;
                }
            }
        }

        openList.add(new Node(startX, startY, null, 0, heuristic(startX, startY, goalX, goalY)));

        int obstacleThreshold = (int) (0.8 * rows * columns);
        int numObstacles = 0;

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.x == goalX && current.y == goalY) {
                // Found the goal, reconstruct path
                while (current.parent != null) {
                    if (gridArray[current.y][current.x] != 2 && gridArray[current.y][current.x] != 3) {
                        gridArray[current.y][current.x] = 4; // Mark path with "/"
                    }
                    current = current.parent;
                }
                return;
            }

            visited[current.y][current.x] = true;

            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                if (newX >= 0 && newX < columns && newY >= 0 && newY < rows && gridArray[newY][newX] != 1 && !visited[newY][newX]) {
                    double newCost = current.cost + 1; // Assuming uniform cost

                    // Check for obstacles in the direction of movement
                    if (gridArray[newY][newX] == 1) {
                        newCost += 10; // Penalize for navigating through obstacles
                        numObstacles++;
                    }
                    Node neighbor = new Node(newX, newY, current, newCost, heuristic(newX, newY, goalX, goalY));
                    openList.add(neighbor);
                }
            }
        }
    }

    // Function to calculate Manhattan distance heuristic
    public static double heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    //Retrieve the path directions from the grid
    public static void getPathDirections(int[][] grid, PathDirections pathDirections) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 4) {
                    pathDirections.add("(" + j + ", " + i + ")");
                }
            }
        }
    }

    //Prints the path directions
    public static void printPathDirections(PathDirections pathDirections, int[][] grid) {
        if (pathDirections.size() == 0) {
            System.out.println("No path directions available.");
            return;
        }

        // Assuming each element in pathDirections is a string representing a cell coordinate in the format "(x, y)"
        for (int i = 0; i < pathDirections.size() - 1; i++) {
            String current = pathDirections.get(i);
            String next = pathDirections.get(i + 1);

            int currentX = Integer.parseInt(current.split(", ")[0].substring(1));
            int currentY = Integer.parseInt(current.split(", ")[1].substring(0, current.split(", ")[1].length() - 1));

            int nextX = Integer.parseInt(next.split(", ")[0].substring(1));
            int nextY = Integer.parseInt(next.split(", ")[1].substring(0, next.split(", ")[1].length() - 1));

            // Calculate direction based on difference in x and y
            String direction = calculateDirection(currentX, currentY, nextX, nextY);

            System.out.println("Move " + (i + 1) + ": (" + currentX + ", " + currentY + ") " + direction);
        }
        String last = pathDirections.get(pathDirections.size() - 1);
        int lastX = Integer.parseInt(last.split(", ")[0].substring(1));
        int lastY = Integer.parseInt(last.split(", ")[1].substring(0, last.split(", ")[1].length() - 1));
        System.out.println("Final position: (" + lastX + ", " + lastY + ")");
    }

    //Calculates the direction based on the difference in x and y coordinates
    private static String calculateDirection(int currentX, int currentY, int nextX, int nextY) {
        if (nextY == currentY) {
            return nextX > currentX ? "Right" : "Left";
        } else {
            return nextY > currentY ? "Down" : "Up";
        }
    }

    //Checks if a path was found in the grid
    public static boolean pathFound(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 4) {
                    return true;
                }
            }
        }
        return false;
    }
}



