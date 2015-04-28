public class Ore
    extends Actor{

    private int rate;

    public Ore(Point position, String name, int rate){
        super(position, name);
        this.rate = rate;
    }

    public String entityString(){
        return String.format("ore %2$ %3$ %4$", super.getName(),
                        super.getPosition().toString(),
                        this.rate);
    }
}