import processing.core.*;

public class Main extends PApplet
{
	// @Andrew
	// going to make these public for simplicity
	// as they are kind of like global variables
   public final int windowWidth = 640;
   public final int windowHeight = 480;
   public final int viewWidth = 20;
   public final int viewHeight = 15;
   public final int tileWidth = windowWidth/viewWidth;
   public final int tileHeight = windowHeight/viewHeight;

   private final int worldScale = 2;

   private ImageStore iStore;
   private WorldModel world;
   private WorldView viewPort;
   private Background defaultBgnd;
   public void setup()
   {
      size(windowWidth, windowHeight);
      iStore = new ImageStore(this, "Filename", tileWidth, tileWidth);

      defaultBgnd = new Background("DefaultImageName",
              iStore.getImages("DefaultImageName"));

      world = new WorldModel(viewHeight*worldScale, viewWidth*worldScale,
         defaultBgnd);

      viewPort = new WorldView("MOAR", args);

      world.loadFromSave(iStore, "savefile");
   }

   public void draw()
   {
      background(color(220, 230, 245));
      viewPort.draw(this, world);
   }

   public static void main(String[] args)
   {
      PApplet.main("Main");
   }
}
