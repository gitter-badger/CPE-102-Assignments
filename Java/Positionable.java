
public abstract class Positionable
    extends Background{

    Point position;

    public Positionable(Point position, String name) {
        super(name);
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}