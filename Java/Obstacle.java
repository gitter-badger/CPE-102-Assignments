public class Obstacle
    extends Positionable{

    public Obstacle(Point position, String name){
        super(position,  name);
    }

    public String entityString(){
        return String.format("obstacle %1$ %2$", super.getName(), super.getPosition().toString());
    }
}