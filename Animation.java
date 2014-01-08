import java.awt.Image;
import java.util.ArrayList;

public class Animation{

  private ArrayList frames;
  
  // Animation Image set (static)
  private ArrayList set;
  
  private int currFrameIndex;
  private long animTime;
  private long totalDuration;
  
  // Orientation of the player; right is 0 (default), left is 1 
  private int orientation;
  
  public Animation(){
    frames = new ArrayList();
	set = new ArrayList();
	totalDuration = 0;
	orientation = 1;
	start();
  }

  private Animation(ArrayList frames, long totalDuration) {
    this.frames = frames;
    this.totalDuration = totalDuration;
    start();
  }
  
  public Object clone() {
    return new Animation(frames, totalDuration);
  }
  
  public synchronized void addFrame(Image image, long duration){
    totalDuration += duration;
	frames.add(new AnimFrame(image, totalDuration));
  }
  
 // Add set of player image
 public synchronized void addSet(Image image){
	set.add(image);
  } 
  
  public synchronized void start(){
    animTime = 0;
	currFrameIndex = 0;
  }
  
  public synchronized void update(long elapsedTime){
    if(frames.size() > 1){
	  animTime += elapsedTime;
	  
	  if(animTime >= totalDuration){
	    animTime = animTime % totalDuration;
		currFrameIndex = 0;
	  }
	  
	  while(animTime > getFrame(currFrameIndex).endTime){
	    currFrameIndex++;
	  }
	  
	}
	
  }
  
  public synchronized Image getImage(){
    if(frames.size() == 0){	  
	  Image i = getSet();
	  return i;
	}
	else{
	  return getFrame(currFrameIndex).image;
	}
  }
  
  public void setOrientation(int o){
    orientation = o;
  }
  
  private AnimFrame getFrame(int i){
    return (AnimFrame) frames.get(i);
  }
  
  private Image getSet(){
    // if RIGHT:
    if(orientation == 0){	  
	  return (Image) set.get(1);
	}
	// else LEFT
	else{
	  return (Image) set.get(0);
	}
  }
   
  private class AnimFrame{
    Image image;
    long endTime;
	
	public AnimFrame(Image image, long endTime){
	  this.image = image;
	  this.endTime = endTime;
	}
	
  }
  
  


}