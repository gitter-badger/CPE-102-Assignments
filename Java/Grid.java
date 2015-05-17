public class Grid {

	private int width;
	private int height;
	private Entity[][] cells;

	public Grid(int width, int height, Entity occupants) {
		this.width = width;
		this.height = height;
		this.cells = new Entity[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				// this.cells[row][col] = (occupants);
			}
		}
	}

	public Entity getCell(Point pt) {
		return cells[pt.getY()][pt.getX()];
	}

	public void setCell(Point pt, Entity occupant) {
		cells[pt.getY()][pt.getX()] = occupant;
	}
}
