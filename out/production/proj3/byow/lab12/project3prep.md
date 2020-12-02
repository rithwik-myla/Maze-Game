# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: My implementation didn't break down the the problem into smaller subproblems to solve. Instead I tried to do it all in a couple of methods. However the given implementation was spread out and broken down into simpler sub problems.

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: The hexagon is a wall in the world and the tesselation is the map of the entire world.

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: I would think of writing a drawTile function that draws a tile at a certain x and y.

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: A hallway is not closed and is narrower while a room is closed by walls and is wider. They are both open spaces.
