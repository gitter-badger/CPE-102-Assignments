import java.util.ArrayList;
import java.util.List;

import processing.core.PImage;

public abstract class Actor extends Positionable {
	private List<Action> pendingActions;

	public Actor(Point position, String name, List<PImage> images) {
		super(position, name, images);
		pendingActions = new ArrayList<Action>();
	}

	protected List<Action> getPendingActions() {
		return pendingActions;
	}

	protected void addPendingAction(Action toAdd) {
		pendingActions.add(toAdd);
	}
	protected void removePendingAction(Action toAdd) {
		pendingActions.remove(toAdd);
	}
   public void clearPendingActions() {
      pendingActions = new ArrayList<Action>();
   }

	public void scheduleAction(WorldModel world, long ticks, ImageStore iStore,
      long scheduleRate){
		world.scheduleAction(this, this.createAction(world, iStore),
         ticks + scheduleRate);
	}

	protected abstract Action createAction(WorldModel world, ImageStore iStore);
}
