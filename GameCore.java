import java.awt.*;
import javax.swing.ImageIcon;

public abstract class GameCore{
  protected static final int FONT_SIZE = 24;
  
  private static final DisplayMode POSSIBLE_MODES[] = {
    new DisplayMode(1024, 768, 32, 0),
    new DisplayMode(800, 600, 32, 0),
	new DisplayMode(800, 600, 24, 0),
	new DisplayMode(800, 600, 16, 0)
  };
  
  private boolean isRunning;
  protected ScreenManager screen;
  
  public void stop(){
    isRunning = false;
  }
  
  public void run(){
    try{
	  init();
	  gameLoop();
	}finally{
	  screen.restoreScreen();
	}
  }
  
  public void init(){
    screen = new ScreenManager();
	DisplayMode displayMode = 
	  screen.findFirstCompatibleMode(POSSIBLE_MODES);
	screen.setFullScreen(displayMode);

    Window window = screen.getFullScreenWindow();    
	window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
	window.setBackground(Color.blue);
    window.setForeground(Color.white);

    isRunning = true;	
  }
  
  protected Image loadImage(String fileName){
    return new ImageIcon(fileName).getImage();
  }
  
  public void gameLoop(){
    long startTime = System.currentTimeMillis();
	long currTime = startTime;
    
    while(isRunning){
	  long elapsedTime = System.currentTimeMillis() - currTime;
	  currTime += elapsedTime;

	  
      // update
      update(elapsedTime);

      // draw to the screen
      Graphics2D g = screen.getGraphics();
      draw(g);
      g.dispose();
      screen.update();

      // take a nap
      try{
	    Thread.sleep(20);
	  }catch(InterruptedException ex){
	  }	  
	}		
  }

  public void update(long elapsedTime){
  }
  

  // abstract class; must be overridden
  public abstract void draw(Graphics2D g);  
  
  
}