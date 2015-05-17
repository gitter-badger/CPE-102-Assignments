
public class ScheduledAction {
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
}
