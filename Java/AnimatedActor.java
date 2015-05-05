public class AnimatedActor extends Actor
{
   private double animationRate;

   public AnimatedActor(Point position, String name, double animationRate)
   {
      super(position, name);
      this.animationRate = animationRate;
   }

   public double getAnimationRate()
   {
      return animationRate;
   }
}
