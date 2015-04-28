public class Ore
    extends Actor{

    private int rate;

    public Ore(Point position, String name, int rate = 5000){
        super(position, name);
        this.rate = rate;
    }
}