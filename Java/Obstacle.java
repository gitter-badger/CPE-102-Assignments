public class Obstacle
    extends Positionable{

    public Obstacle(Point position, String name){
        super(position,  name);
    }

    public String entityString(){
        return String.format("obstacle %1$s %2$s", super.getName(), super.getPosition().toString());
    }
}