import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

import processing.core.*;


public class ImageStore
{
   private HashMap<String, List<PImage>> images;
   private static final int COLOR_MASK = 0xffffff;

   // All the real work happens in this constructor and the functions it calls.
   public ImageStore(PApplet app, String filename, int tileWidth, int tileHeight)
   {
	   images = new HashMap<String, List<PImage>>();
      try
      {
            Scanner in = new Scanner(new FileInputStream(filename));
            while(in.hasNextLine())
            {
               processImageLine(app, in.nextLine().split("\\s+"));
            }
            in.close();
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public List<PImage> getImages(String key)
   {
      if(images.isEmpty() || !images.containsKey(key))
      {
         return new ArrayList<PImage>();
      }
      else
      {
         return images.get(key);
      }
   }

   private void processImageLine(PApplet app, String[] line)
   {
      /*
       * FormatOfLine -> key file r g b a
       * Key (String) -> name of object
       * File (String) -> route to image file from this directory
       * r, g, b (Int) -> Color(r, g, b)
       * a (Int) -> alpha
       */

      if(line.length < 0)
      {
         return;
      }

      String key = line[0];
      PImage img = app.loadImage(line[1]);

      if(line.length == 6)
      {
         try
         {
            int c = app.color(Integer.parseInt(line[2]),
               Integer.parseInt(line[3]), Integer.parseInt(line[4]));
            img = setAlpha(img, c, Integer.parseInt(line[5]));
         }
         catch (NumberFormatException nfe)
         {
            System.out.println("Trust me. You'll never see this message.");
         }
      }

      List<PImage> associatedImages = getImages(key);
      associatedImages.add(img);
      images.put(key, associatedImages);
   }

   private static PImage setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
               img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
      return img;
   }
}
