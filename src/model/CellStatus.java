package model;

public enum CellStatus {
	BLACK,
	WHITE,
	NONE;

	public CellStatus getOppositeStatus() throws RuntimeException{
		if (this == BLACK) {
			return WHITE;
		} else if (this == WHITE) {
			return BLACK;
		} else {
			throw new RuntimeException("this cell has no coin");
		}
	}
}

