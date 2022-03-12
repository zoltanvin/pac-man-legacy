<h1 align="center">
  <br>
  <a href="https://github.com/zolvin/pac-man-legacy"><img src="https://github.com/zolvin/pac-man-legacy/blob/main/Misc/logo.png" alt="Image rotator" width="200"></a>
  <br>
  Pac-Man Legacy
  <br>
</h1>

<h3 align="center">Winning strategy for the game</a></h3>
<p align="center">
  <a href="#key-features-">Key Features</a> •
  <a href="#clone-and-use-">Clone and Use</a> •
  <a href="#sample-agent-">Sample Agent</a> •
  <a href="#java-program-">Java Program</a> •
  <a href="#rules-">Rules</a> •
  <a href="#requirements-">Requirements</a>
</p>
 
![screenshot](https://github.com/zolvin/pac-man-legacy/blob/main/Misc/demo2.gif)

# Key Features 🔑

- The classic Pac-Man game with graphic interface represented
- Showcase of a sample strategy
- Showcase of the implemented strategy

# Clone And Use 📋

- To install `java` follow the instructions from the [official](https://java.com/en/download/) website
- In case you want to help developing it or simply saving it, you can fork the repository just by clicking the button on the top-right corner of this page
- After the successful installation of `java`, clone the repository into your local system using below command:
  - ```
     git clone https://github.com/zolvin/pac-man-legacy.git
    ```
  - This will clone the whole repository in your system
- Starting in IntelliJ:
  - This project needs `javafx` libraries. To install `JavaFX` follow the instructions from the [official](https://openjfx.io/openjfx-docs/) website
  - After the project is loaded in IntelliJ, navigate into Project Structure
  - Set VM options:
  - ```
     --module-path %PATH_TO_FX% 
     --add-modules javafx.controls,javafx.fxml
    ```  
   - Set program arguments: `10 game.pm.PMGame 1234567890 5000 10000 Agent`
    
- Starting from command line with graphics:
  - Navigate into the project folder. After that, execute following commands:
   - ```
      javac -cp game_engine.jar Agent.java
      java -jar game_engine.jar 10 game.pm.PMGame 1234567890 5000 10000 Agent
     ```
    - `10`: fps/debug parameter. Determines the speed of the game. Special value is 0, which is the evaluation mode without display
    - `game.pm.PMGame`: Game class, it should always be the same
    - `1234567890`: random seed
    - `5000`: maximum thinking time (ms)
    - `10000`: maximum iteration number
    - `Agent`: Pac-Man controller implementation
 
 - Starting from command line without graphics:
   - ```
      javac -cp game_engine.jar Agent.java
      java -jar game_engine.jar 0 game.pm.PMGame 1234567890 5000 10000 Agent
     ```
    - The output channel of the console shows the result achieved by the agent
    - Eg: `0 game.pm.strategies.Agent 0 1020.0 1650` where: 
    - `0`: ID of the winning player (it will always be 0)
    - `game.pm.strategies.Agent`: player class
    - `0`: player ID (it will always be 0)
    - `1020.0`: score achieved
    - `1650`: remaining thinking time (ms)

# Sample Agent 💬
- An agent that randomly selects four possible directions with the following code
- ```java
   importgame.pm.strategies.Strategy;
   importgame.pm.PMGame;
   public class SampleStrategy extends Strategy{
    @Override
    public int getDirection(int id, PMGame game){
      return random.nextInt(4);
      }
   }
  ```

# Java Program 💾
- Diagnoses each frame which direction the player should go
- Determines the walls around the player for routes
- Follows the tiles with food when it's possible
- Takes into consideration the distance of the ghosts with weighted value
- Chases the ghosts when they are close and edible, run away when they are not

# Rules 🔧
- In the game there is a maze with food placed in it. The task is to navigate Pac-Man in the maze. Each food reward collected increases the player's score. The game is complicated by the wandering of four ghosts that has to be avoided.
- The player has 3 lives, the number of lives decreases by one when he encounters a ghost. The game ends when the last life is lost
- Gaining one food increases the score by `10`
- There are 4 special foods on the field, value of 50 points each. Upon obtaining these, the ghosts become edible for a time
- Extra points can be earned for hunting the edible ghost (extra points for spirits increase exponentially with the use of the special food (200, 400, 800, 1600)
- Pac-Man can be navigated using the UP, LEFT, DOWN, RIGHT actions. Cannot go through the maze wall
- The maze is a tiled area of `31x28`, tile size is `40x40`
- Moving agents (ghost, pacman) has different speeds
- Two objects are on one tile if both the location of the object falls on the same tile area. The location point is defined by the geometric center of the object
- In the case where the geometric center of Pacman and one of the ghosts is on a tile, the state is considered to be contact regardless of the actual geometric location
- The source code for ghosts is available in the engine:
  - Blinky: `game.pm.strategies.Blinky.java`
  - Pinky: `game.pm.strategies.Pinky.java`
  - Inky: `game.pm.strategies.Inky.java`
  - Clyde: `game.pm.strategies.Clyde.java`
- `game.pm.strategies.GreedyStrategy` is an example strategy that illustrates certain things, eg. cloning
- Maximum thinking time: `5000ms`
- Maximum usable memory: `5G`
- Maximum iteration number: `10000`
- The framework does not use random decisions, the random seed only influences any random decisions on its own implementation

# Requirements 🔨
- The solution must not consist of replaying a prefabricated sequence of steps 
- If random numbers are used, only the inherited field can be used and seed migration is prohibited 
- The class containing the solution cannot be bundled 
- The solution cannot write to the screen
- The solution cannot open a file, start a new thread

</br>
:star: Star me on GitHub — it helps!
