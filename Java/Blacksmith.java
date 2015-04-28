public class Blacksmith
    extends Actor{

    private int rate;
    private int resourceLimit;
    private int resourceCount;
    private int resourceDistance;


    public Blacksmith(Point position, String name, int rate, int resourceLimit, int resourceDistance) {
        super(position, name);
        this.rate = rate;
        this.resourceLimit = resourceLimit;
        this.resourceCount = 0;
        this.resourceDistance = resourceDistance;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public int getResourceDistance() {
        return resourceDistance;
    }

    public String entityString(){
        return String.format("blacksmith %1$ %2$ %3$ %4$", super.getName(),
                super.getPosition().toString(),
                this.resourceLimit,
                this.rate,
                this.resourceDistance);
    }
}