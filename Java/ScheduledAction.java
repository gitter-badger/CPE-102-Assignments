public class ScheduledAction
   implements Comparable<ScheduledAction>{

	private Action toExecute;
	private long timeToExecute;

	public ScheduledAction(Action toDo, long whenToDoIt) {
		this.toExecute = toDo;
		this.timeToExecute = whenToDoIt;
	}

	public Action getAction(){
		return toExecute;
	}

	public long getTime(){
		return timeToExecute;
	}

	public int compareTo(ScheduledAction o2) {
		return Long.compare(this.timeToExecute, o2.timeToExecute);
	}
}
