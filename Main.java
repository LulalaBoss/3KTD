import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main extends GameCore{
  
  public static void main(String[] args){
	
	new Main().run();

  }
  
  private static final long DEMO_TIME = 10000;
  
  private ScreenManager screen;
  private Image bgImage;
  private Image furby2;
  private Image transparentImage;
  
  private Animation anim;
  private Sprite s;
  
  
  public void loadImages(){
    bgImage = loadImage("background1.jpg");
	
	furby2 = loadImage("Alvin.gif");
	//transparentImage = loadImage("Starbucks.jpg");
    Image furby0 = loadImage("bf0.png");
	Image furby1 = loadImage("bf1.png");
	
	
	// Create animation: adding frame
	anim = new Animation();
	anim.addFrame(furby0, 250);
	anim.addFrame(furby1, 100);
	anim.addFrame(furby0, 250);
	anim.addFrame(furby1, 150);
	anim.addFrame(furby0, 300);
	anim.addFrame(furby1, 100);
	
	// create a sprite object
	float dx = 0.05f;
	float dy = 0.05f;
	s = new Sprite(anim);
	s.setX(0);
	s.setY(0);
	s.setVelocityX(dx);
	s.setVelocityY(dy);
	
  }
  
  private Image loadImage(String fileName){
    return new ImageIcon(fileName).getImage();
  }
  
  public void animationLoop(){
    long startTime = System.currentTimeMillis();
	long currTime = startTime;
	
	while(currTime - startTime < DEMO_TIME){
	  long elapsedTime = System.currentTimeMillis() - currTime;
	  currTime += elapsedTime;
	  
	  // update animation
	  anim.update(elapsedTime);
	  
	  // update sprite
	  s.update(elapsedTime);
	  
	  // draw to screen
	  Graphics2D g = screen.getGraphics();
	  draw(g);
	  g.dispose();
	  
	  // take a nap
	  try{
	    Thread.sleep(20);
	  }catch(InterruptedException ex){}
	
	}
		
  }
  
  public void draw(Graphics2D g){
    // draw background
	g.drawImage(bgImage, 0, 0, null);
	
	// draw animation
	g.drawImage(anim.getImage(), 0, 0, null);

	g.drawImage(furby2, 100, 0, null);
	
    
	// draw sprite
	g.drawImage(s.getImage(), Math.round(s.getX()), Math.round(s.getY()), null);
     
  }
  
  
}