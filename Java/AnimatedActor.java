public class AnimatedActor extends Actor
{
   public AnimatedActor(Point position, String name, double animationRate)
   {
      super(position, name);
      this.animationRate = animationRate;
   }

   public double getAnimationRate()
   {
      return animationRate;
   }

   private double animationRate;
}
