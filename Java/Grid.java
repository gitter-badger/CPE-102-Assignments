public class Grid {

   private int width;
   private int height;
   private List<Entity> cells;

   public Grid(int width, int height, Entity occupants) {
      this.width = width;
      this.height = height;
      this.cells = new ArrayList<List<Entity>>();

      for (int row = 0; row<height, row++) {
         this.cells.append(new ArrayList<Entity>());
         for (int col = 0; col<width; col++) {
            this.cells.get(row).append(occupants);
         }
      }
   }

   public Entity getCell(Point pt) {
      return cells.get(pt.getY()).get(pt.getX());
   }

   public void setCell(Point pt, Entity occupant) {
      cells.get(pt.getY()).get(pt.getX()) = occupant;
   }
}
