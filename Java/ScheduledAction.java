import java.util.Comparator;


public class ScheduledAction
	implements Comparator<ScheduledAction>{

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

	public int compare(ScheduledAction o1, ScheduledAction o2) {
		return Long.compare(o1.timeToExecute, o2.timeToExecute);
	}
}
