public class ScheduledAction implements Comparable<ScheduledAction> {

	private Action toExecute;
	private long timeToExecute;

	public ScheduledAction(Action toDo, long whenToDoIt) {
		this.toExecute = toDo;
		this.timeToExecute = whenToDoIt;
	}

	public Action getAction() {
		return toExecute;
	}

	public long getTime() {
		return timeToExecute;
	}

	public int compareTo(ScheduledAction o2) {
		return Long.compare(this.timeToExecute, o2.timeToExecute);
	}

	public boolean equals(Object o) {
		if (o instanceof ScheduledAction) {
			ScheduledAction casted = (ScheduledAction) o;
			return casted.getAction() == this.getAction();
		} else if (o instanceof Action) {
			Action casted = (Action) o;
			return this.getAction() == casted;
		}
		return false;
	}
}
