import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.io.IOException;

public class SimpleGame extends GameCore{
  public static void main(String[] args){
    new SimpleGame().run();
  }
  
  public static final float GRAVITY = 0.002f;
  
  private TileMap map;
  private TileMapRenderer renderer;
  private ResourceManager resourceManager;
  
  private Point pointCache = new Point();
  private Image bgImage;
  
  private boolean paused;
  
  protected GameAction jump;
  protected GameAction exit;
  protected GameAction moveLeft;
  protected GameAction moveRight;
  protected GameAction pause;
  protected GameAction fire;
  protected InputManager inputManager;
  
  
  public void init(){
    super.init();
	Window window = screen.getFullScreenWindow();
	inputManager = new InputManager(window);
	resourceManager = new ResourceManager(
      window.getGraphicsConfiguration());

	createGameActions();
	createSprite();
	paused = false;
	
  }
  
  public boolean isPaused(){
    return paused;
  }
  
  public void setPaused(boolean p){
    if(paused != p){
	  this.paused = p;
	  inputManager.resetAllGameActions();
	}
  }
  
  public void update(long elapsedTime){
    Creature player = (Creature)map.getPlayer();
	Animation fire = map.getAnim();
    checkSystemInput();
	if(!isPaused()){
	  // check game input
	  checkGameInput();
	  // update sprite
	  updateCreature(player, elapsedTime);
	  player.update(elapsedTime);
	  fire.update(elapsedTime);
	}
  }
  
  public void checkSystemInput(){
    if(pause.isPressed()){
	  setPaused(!isPaused());
	}
	if(exit.isPressed()){
	  stop();
	}
  }
  
  public void checkGameInput(){
    Player player = (Player)map.getPlayer();
    float velocityX = 0;
	renderer.techniqueOn = false;
	if(moveLeft.isPressed()){
	  velocityX-=player.getMaxSpeed();
	}
	if(moveRight.isPressed()){
	  velocityX+=player.getMaxSpeed();;
	}
	
	if(jump.isPressed()){
	  player.jump(false);
	}
	
	if(fire.isPressed()){
	  renderer.techniqueOn = true;
	}
    player.setVelocityX(velocityX);
	
  }
  
  public void draw(Graphics2D g){
    renderer.draw(g, map, screen.getWidth(), screen.getHeight());
  }
  
  public void createGameActions(){
    fire = new GameAction("fire");
    jump = new GameAction("jump",
	  GameAction.DETECT_INITIAL_PRESS_ONLY);
    exit = new GameAction("exit",
	  GameAction.DETECT_INITIAL_PRESS_ONLY);
	moveLeft = new GameAction("moveLeft");
	moveRight = new GameAction("moveRight");
	pause = new GameAction("pause",
	  GameAction.DETECT_INITIAL_PRESS_ONLY);
	
	inputManager.mapToKey(fire, KeyEvent.VK_Z);
	
	inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
	inputManager.mapToKey(pause, KeyEvent.VK_P);
	
	inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
	inputManager.mapToKey(moveLeft, KeyEvent.VK_A);
	inputManager.mapToKey(moveRight, KeyEvent.VK_D);
	  
  }
  
  public void createSprite(){
    bgImage = resourceManager.loadImage("largeGrassMap.png");
	
	// screen size = 16 x 12
	renderer = new TileMapRenderer();
    try{
	  map = resourceManager.loadMap("map.txt");
	}
    catch(IOException e){}	
	// load background images
	renderer.setBackground(bgImage);
	
	// create technique anim
	/*
	Animation fireAnim = resourceManager.createTechniqueAnim(
	  fire1,fire2,fire3,fire4,fire5)
    */
  }
  
  public Point getTileCollision(Sprite sprite,
        float newX, float newY)
  {
    float fromX = Math.min(sprite.getX(), newX);
    float fromY = Math.min(sprite.getY(), newY);
    float toX = Math.max(sprite.getX(), newX);
    float toY = Math.max(sprite.getY(), newY);

    // get the tile locations
    int fromTileX = TileMapRenderer.pixelsToTiles(fromX);
    int fromTileY = TileMapRenderer.pixelsToTiles(fromY);
    int toTileX = TileMapRenderer.pixelsToTiles(
      toX + sprite.getWidth() - 1);
    int toTileY = TileMapRenderer.pixelsToTiles(
      toY + sprite.getHeight() - 1);

    // check each tile for a collision
    for (int x=fromTileX; x<=toTileX; x++) {
      for (int y=fromTileY; y<=toTileY; y++) {
        if (x < 0 || x >= map.getWidth() ||
            map.getTile(x, y) != null)
        {
          // collision found, return the tile
          pointCache.setLocation(x, y);
          return pointCache;
        }
      }
    }

    // no collision found
    return null;
  }
  public boolean isCollision(Sprite s1, Sprite s2){
    // if the Sprites are the same, return false
    if (s1 == s2) {
      return false;
    }

    // if one of the Sprites is a dead Creature, return false
    if (s1 instanceof Creature && !((Creature)s1).isAlive()) {
      return false;
    }
    if (s2 instanceof Creature && !((Creature)s2).isAlive()) {
      return false;
	}

    // get the pixel location of the Sprites
    int s1x = Math.round(s1.getX());
    int s1y = Math.round(s1.getY());
    int s2x = Math.round(s2.getX());
    int s2y = Math.round(s2.getY());

    // check if the two sprites' boundaries intersect
    return (s1x < s2x + s2.getWidth() &&
            s2x < s1x + s1.getWidth() &&
            s1y < s2y + s2.getHeight() &&
            s2y < s1y + s1.getHeight());
  }

  private void updateCreature(Creature creature,
        long elapsedTime)
  {

    // apply gravity
    if (!creature.isFlying()) {
      creature.setVelocityY(creature.getVelocityY() +
        GRAVITY * elapsedTime);
    }

    // change x
    float dx = creature.getVelocityX();
    float oldX = creature.getX();
    float newX = oldX + dx * elapsedTime;
    Point tile =
      getTileCollision(creature, newX, creature.getY());
    if (tile == null) {
      creature.setX(newX);
    }
    else {
      // line up with the tile boundary
      if (dx > 0) {
        creature.setX(
          TileMapRenderer.tilesToPixels(tile.x) -
          creature.getWidth());
      }
      else if (dx < 0) {
        creature.setX(
        TileMapRenderer.tilesToPixels(tile.x + 1));
      }
      creature.collideHorizontal();
    }


    // change y
    float dy = creature.getVelocityY();
    float oldY = creature.getY();
    float newY = oldY + dy * elapsedTime;
    tile = getTileCollision(creature, creature.getX(), newY);
    if (tile == null) {
      creature.setY(newY);
    }
    else{
      // line up with the tile boundary
      if (dy > 0) {
        creature.setY(
          TileMapRenderer.tilesToPixels(tile.y) -
          creature.getHeight());
      }
      else if (dy < 0) {
        creature.setY(
          TileMapRenderer.tilesToPixels(tile.y + 1));
      }
      creature.collideVertical();
    }

  }
  
  
} 