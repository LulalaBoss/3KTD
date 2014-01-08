import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.awt.*;
import javax.swing.JFrame;

public class KeyTest extends GameCore implements KeyListener{

  public static void main(String[] args){
    new KeyTest().run();
  }
  
  private LinkedList messages = new LinkedList();
  
  public void init(){
    super.init();
	
	Window window = screen.getFullScreenWindow();
	
	window.setFocusTraversalKeysEnabled(false);
	
	window.addKeyListener(this);

    addMessage("KeyInputTest. Press Escape to exit");	
  }
  
  public void keyPressed(KeyEvent e){
    int keyCode = e.getKeyCode();
	
	if(keyCode == KeyEvent.VK_ESCAPE){
	  stop();
	}
	else{
	  addMessage("Pressed: " + KeyEvent.getKeyText(keyCode));
	  
	  e.consume();
	}
	
  }
  
  public void keyReleased(KeyEvent e){
    int keyCode = e.getKeyCode();
	addMessage("Released: " + KeyEvent.getKeyText(keyCode));
	e.consume();	
  }
  
  public void keyTyped(KeyEvent e){
    e.consume();
  }
  
  public synchronized void addMessage(String message){
    messages.add(message);
	
	if(messages.size() >= screen.getHeight() / FONT_SIZE){
	  messages.remove(0);
	}
	
  }
  
  public synchronized void draw(Graphics2D g){
    Window window = screen.getFullScreenWindow();
	
	g.setRenderingHint(
	  RenderingHints.KEY_TEXT_ANTIALIASING,
	  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
	g.setColor(window.getBackground());
	g.fillRect(0,0,screen.getWidth(),screen.getHeight());
	
    g.setColor(window.getForeground());
	int y = FONT_SIZE;
	for(int i=0;i<messages.size();i++){
	  g.drawString((String)messages.get(i),5,y);
	  y+=FONT_SIZE;
	}
	
  }

}