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
	
	
}
