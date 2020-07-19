# Conways-Game-of-Life

![](gameplay.gif)

Recreation of Conway's Game of Life using Java Swing

Execute the GameOfLife.java class to create a new instance of the simulation. By default, there is a 10x10 grid. Click the 'Resize Board' to change the board to any dimension between (10,10) and (500,500). On many displays, larger boards have performance and rendering issues, board sizes up to 400x400 tend to perform better.

# Features 

<ul>
<li>Ability to change the size of the field from 10x10 up to 500x500.</li>
<li>Ability to manually set / clear any cell in order to set up patterns. +Ability to clear the entire field.</li>
<li>Ability to fill the field randomly.</li>
<li>Ability to advance the game by pressing a button.</li>
<li>Written with a Model View Controller architecture. </li>
<li>Ability to set the "survive" and "birth" thresholds to custom values.</li>
<li>Ability to toggle "torus" mode on or off. In torus mode, the field is treated as if it wraps around the edges back to the other edge.</li>
<li>A start/stop button that advances the game automatically using a separate thread with a delay between updates settable between 10 milliseconds and 1 second.</li>
</ul>
