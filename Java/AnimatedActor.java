import java.util.List;
import processing.core.PImage;

public abstract class AnimatedActor extends Actor {
	private long animationRate;

	public AnimatedActor(Point position, String name, long animationRate,
			List<PImage> images) {
		super(position, name, images);
		this.animationRate = animationRate;
	}

	public long getAnimationRate() {
		return animationRate;
	}

   public void scheduleAnimation(WorldModel world, long ticks, int repeatCount) {
      world.scheduleAction(this, createAnimationAction(world, repeatCount),
         ticks + animationRate);
   }

	public Action createAnimationAction(WorldModel world, int repeatCount){
		Action[] actions = {null};
		actions[0] = (long ticks)-> {
			removePendingAction(actions[0]);
			nextImage();

			if (repeatCount != 1){
			   scheduleAnimation(world, ticks, repeatCount - 1);
         }

			return getPosition();
		};

		return actions[0];

	}
}
