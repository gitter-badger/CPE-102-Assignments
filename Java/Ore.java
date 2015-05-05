public class Ore extends Actor {

	private int rate;

	public Ore(Point position, String name, int rate) {
		super(position, name);
		this.rate = rate;
	}

	public String entityString() {
		return String.format("ore %1$s %2$s %3$s", super.getName(), super
				.getPosition().toString(), this.rate);
	}
}