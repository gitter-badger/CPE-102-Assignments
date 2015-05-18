import java.util.List;
import processing.core.PImage;

public abstract class AnimatedActor extends Actor {
	private double animationRate;

	public AnimatedActor(Point position, String name, double animationRate,
			List<PImage> images) {
		super(position, name, images);
		this.animationRate = animationRate;
	}

	public double getAnimationRate() {
		return animationRate;
	}
	
	public Action createAnimationAction(WorldModel world, int repeatCount){
		Action[] actions = {null};
		actions[0] = (long ticks) -> {
			removePendingAction(actions[0]);
			nextImage();
			
			if (repeatCount != 1){
				world.scheduleAction(this,createAnimatedAction(world, repeatCount-1), ticks + (long)animationRate);
			}
			
			return getPosition();
		};
		
		return actions[0];
		
	}

	private Action createAnimatedAction(WorldModel world, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
