/*
Please Read the Following Game Rules:

Please turn up the volume for the best game experience. At least turn up to 30%.

Optional: When playing, please FULL SCREEN!

You are able to move in all 4 directions! Just use the 4 arrows on your keyboard.

DO NOT go behind the grey bear, as it can attack you.

Two ways you can win this game: 
1.Wait for the cop to arrive.
2.Run into Bonnie and gain his trust. When trust level reaches 100, he will take you to cop.

GOOD LUCK!!!
*/



package SourceFolder;

import javax.sound.sampled.*;
import java.io.File;
import javax.swing.JOptionPane;
public class Game
{
  private Grid grid;
  private int userRow;
  private int userCol;
  private int msElapsed;
  private int timesGet;
  private int timesAvoid;


  
  public Game()
  {
    playSound("background.wav");
    
    JOptionPane.showMessageDialog(null, "(Please turn up the volume for the best game experience. At least turn up to 30%) \nYou are able to move in all 4 directions! \nAvoid the grey bears at any costs! DO NOT go behind the grey bear, as it can attack you. \nTwo ways you can win this game: \n1.Wait for the cop to arrive. \n2.Run into Bonnie and gain his trust. When trust level reaches 100, he will take you to cop. \nGOOD LUCK!!!", "Please Read the Following Game Rules", JOptionPane.WARNING_MESSAGE);
    grid = new Grid(9, 16); //try 8 b 20, originally is 5,10


    userRow = 2;
    msElapsed = 0;
    timesGet = 0;
    timesAvoid = 0;
    updateTitle();
    grid.setImage(new Location(userRow, 0), "user.gif");
    
  }

  
  public void play()
  {
    while (!isGameOver())
    {
      grid.pause(100); //decrease this pause increases the speed,default is 100
      handleKeyPress();
      if (msElapsed % 500 == 0)
      {
        scrollLeft();
        populateRightEdge();
      }
      updateTitle();
      msElapsed += 100;
    }
  }



  /*
  
  public void handleKeyPress()
  {
    int key = grid.checkLastKeyPressed();
    if(key ==38){
      grid.setImage(new Location(userRow, 0), null);
      userRow--;
    }else if(key ==40){
      grid.setImage(new Location(userRow, 0), null);
      userRow++;
    }
    //if the userRow is too small, (less than 0) set it to 0
    //if too large, set it to bottom row
    handleCollision(new Location(userRow,0));
    grid.setImage(new Location(userRow, 0), "user.gif");
  }
  */

 public void handleKeyPress() {
    int key = grid.checkLastKeyPressed();

    grid.setImage(new Location(userRow, userCol), null);


    if (key == 38) { // Up arrow key
        userRow--;
    } else if (key == 40) { // Down arrow key
        userRow++;
    } else if (key == 37) { // Left arrow key
        userCol--;
    } else if (key == 39) { // Right arrow key
        userCol++;
    }

    // Ensure the userRow and userCol values are within valid bounds
    if (userRow < 0) {
        userRow = 0;
    } else if (userRow >= grid.getNumRows()) {
        userRow = grid.getNumRows() - 1;
    }


    if (userCol < 0) {
        userCol = 0;
    } else if (userCol >= grid.getNumCols()) {
        userCol = grid.getNumCols() - 1;
    }

    // Handle collisions and update the user's image
    handleCollision(new Location(userRow, userCol));
    grid.setImage(new Location(userRow, userCol), "user.gif");
    // for(int i=0; i< grid.getNumRows(); i++){
    //   for(int j=0; j< grid.getNumCols(); j++){
    //     if(!(i==userRow))
    //   }
    // }
}





  public void populateRightEdge()
  {
    //int maxRow = grid.getNumRows()-1;
    int randomRow = (int) (Math.random()*grid.getNumRows());
    if(Math.random()<0.7)
        grid.setImage(new Location(randomRow, grid.getNumCols()-1), "avoid.gif");
    else
        grid.setImage(new Location(randomRow, grid.getNumCols()-1), "get.gif");
  }
  


  public void scrollLeft()
  {
    for(int i=0; i<grid.getNumRows();i++){
    for(int col =1; col<grid.getNumCols(); col++){
      if(!(i==userRow && col==userCol)){
        String image = grid.getImage(new Location(i,col));
        grid.setImage(new Location(i, col-1), image);}
    }
    }
    for(int i=0; i<this.grid.getNumRows(); i++){
      grid.setImage(new Location(i,grid.getNumCols()-1),null);}

      handleCollision(new Location(userRow,0));
    // grid.setImage(new Location(userRow,0),"user.gif");
  }
  




  public void handleCollision(Location loc)
  {
    String img = grid.getImage(loc);
    if("avoid.gif".equals(img)){
      //do stuff
      playSound("wow.wav");
      System.out.println("You asked Bonnie to take you to Cop. Bonnie refused.");  //you can modify this
      timesGet++; // Increment timesGet by 1 when "get.gif" is detected
    }else if("get.gif".equals(img)){
      //do stuff
      

      //NOTE: if you find the game too hard, just comment out the code chunk below:

      System.out.println("A Grey Bear? Wait no... AHHHHHHH!!!!"); //you can modify this
      playSound("scream.wav");
       grid.setTitle("Game Over. The grey bear caught you and ate you.");
       JOptionPane.showMessageDialog(null, "Game Over. The grey bear caught you and ate you.", "You Should Try Again :)", JOptionPane.WARNING_MESSAGE);
       try {
            Thread.sleep(1000); // Pause for 1 seconds (1000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0); // Exit the program




        
    }
  }
  





  public int getScore()
  {
    return 0;
  }
  
  public void updateTitle()
  {
    int remainingTimeInSeconds = 129 - (msElapsed / 1000); // Calculate remaining time in seconds

    int score = timesGet * 10; // Calculate the score

    if ("avoid.gif".equals(grid.getImage(new Location(userRow, userCol)))) {
        score += 10; // Increase score by 10 if "avoid.gif" is detected
    }

    if (remainingTimeInSeconds > 0) {
        grid.setTitle("Survive Until the Cop Arrives: " + remainingTimeInSeconds + " seconds | Bonnie's Trust Level: " + score);
    } else {
        grid.setTitle("Congratulations!!! You survived!!!");
        System.out.println("Congratulations!!! You survived!!!");
        playSound("congratulation.wav");
        try {
            Thread.sleep(5000); // Pause for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0); // Exit the program
    }

    if(score ==100){
        grid.setTitle("Congratulations!!! You survived!!!");
        System.out.println("Congratulations!!! You survived!!!");
        playSound("congratulation.wav");
        JOptionPane.showMessageDialog(null, "You are safe now. You persuaded Bonnie to take you to the cop.", "Your Lucky Day!!!", JOptionPane.WARNING_MESSAGE);
        try {
            Thread.sleep(5000); // Pause for 5 seconds (5000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0); // Exit the program
    
    }


  }
  
  public boolean isGameOver()
  {
    return false;
  }
  
  public static void test()
  {
    Game game = new Game();
    game.play();
  }

private void playSound(String soundFile) {
    try {
        File file = new File(soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


  public static void main(String[] args)
  {
    test();
  }
}