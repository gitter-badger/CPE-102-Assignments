public class Vein extends Actor {

	private int rate;
	private int resourceDistance;

	public Vein(Point position, String name, int rate, int resourceDistance) {
		super(position, name);
		this.rate = rate;
		this.resourceDistance = resourceDistance;
	}

	public String entityString() {
		return String.format("vein %1$s %2$s %3$s", super.getName(), super
				.getPosition().toString(), this.rate);
	}
}