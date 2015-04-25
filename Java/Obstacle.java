import java.lang.Override;

public class Obstacle
    extends Positionable{

    public Obstacle(Point position, String name){
        super(position,  name);
    }

    public String entityString(){
        return String.format("obstacle %2$ %3$", super.getName(), super.getPosition().toString());
    }
}