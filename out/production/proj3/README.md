# Build Your Own World Design Document

**Partner 1: Akshat Jain **

**Partner 2: Rithwik Mylavarapu **

## General Overview:
First, we create a bunch of random rooms across the world. Then we have an AStar Agorithm to connect those rooms with hallways.


## Classes and Data Structures
WorldGenerator
	Instance Variables:
 	1. allRooms (array with all of the rectangles)
	2. gameBoard (the TETile[][] board)
	3. width (how wide the board is)
	4. height (how tall the board is)
	5. occupied (the points on the grid that are already filled; includes cushion area)
	6. notAccesssible (is there a wall or floor tile at this point?)
	7. seed (input seed from the user)
	8. random (pseudorandom generator)

Rectangle
	Instance Variables:
	1. width (width of rectangle)
	2. height (height of rectangle)

	These are the instance variables for the locations
    of the opening and closing locations of the rectangle.
    Opening is where the hallway BEGINS out of the rectangle.
    Closing is where the hallways ENTERS the rectangle.
	3. openingX
	4. openingY
	5. closingX
	6. closingY

	This is the information for the x and y coordinates for the
    bottom left corner of the rectangle.
	7. leftCornerX
	8. leftCornerY

	Whether or not the the opening and closing tiles have been visited.
    TRUE = It has been visited
    FALSE = It has not been visited
	9. private boolean openingBool
    10. private boolean closingBool

	11. random (Pseudorandom stuff)

Hallway
	Instance Variables:
	1. gameBoard (TETile baord)
	2. allRectangles (list of rectangles)
	3. visitedRectangles (list of rectangles that the hallways have attached to)
	4. notvisitedRectangles (list of rectangles that the hallways have not attached to)
	5. filled (which grid spots have been filled)
	6. closingToRectangleMap (Maps the closing point on each rectangle to the rectangle)
	7. notVisitedClosings (closing points that have not been attached by the hallways)
	8. nearestFinder (the WeirdPointSet instance)

KeyBoard
    Instance Variables:
    1. avatar (Point)
    2. end (Point)
    3. gameBoard (TETile[][])
    4. walled (ArrayList of walls)
    5. totalInput (String of the total moves on the world)
    6. ter1 (Renderer)
    7. seed (String of the seed number (N and S included))
    8. numTilesVisited (int of the number of tiles visited)

## Algorithms

WorldGenerator
	Methods:
 	1.roomGenerator (this generates the dimensions and place for the rooms)
 	2.checkCushion (this makes sure the rooms are spaced far enough and away from the edge
 		of the board)
 	3.setRectangleTiles (actually draws the rectangles on the tiles.
 	4.boardInitilaizer (Initialize the world of tiles with the cushion built in and initializing a 		boolean 2D array that keeps track of if each spot is filled)


Rectangle
	Methods:
	1. Rectangle (creates the instance variables)
	2. generateRandomOpening (creates a random opening spot in the room)
	3. generateRandomOpening (creates another random opening spot in the room if needed)
	4. surrounded (checks if everything around the given point is filled)
	5. generateRandomClosing (generates another random opening in the room, which is marked as a 		closing)

Hallway
	Methods:
	1. Hallway Constructor (iniates sequence to create all of the hallways)
	2. makeHallway (creates a singular hallway)
	3. nearestRectangle (finds the closest room given a room)
	4. findPath (this is the AStar algorithm)
	5. findBestPoint (finds the next point on the grid to go to)
	6. checkIfCanGo (checks if the point meets teh criteria (it has not been visited already
		and it is not in a filled spot))
	7. createWalls (given the path, draws out the walls)
	
KeyBoard
    Methods:
    1. KeyBoard Constructor (sets all of the instance variables and calls getNextKey to wait for user input)
    2. getNextKey(String input) (takes the input given after the seed number and moves the avatar accordingly)
    3. getNxtKet() (checks that while the input is not :Q it will keep reading user input and moving avatar)
    4. startOver() (if the avatar reaches the end then this method is called to print a gameOver screen and start over)
    
TERenderer
    Changes:
    1. Added a rendering method that only renders a 4x4 box around the avatar (kind of like a spotlight) and the ending point
    2. Changed the rendering method to work with the mouse tracker

These are the imports from other assignments (VERY standard):
	Point (from WeirdPointSet)
	WeirdPointSet(from WeirdPointSet)
## Persistence

