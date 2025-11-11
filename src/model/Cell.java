package model;

public class Cell {
	private CellStatus cellStatus;

	Cell () {
		this.cellStatus = CellStatus.NONE;
	}

	public CellStatus getCellStatus() {
		return this.cellStatus;
	}

	public void setCellStatus(CellStatus cellStatus) {
		if (this.hasCoin()) throw new RuntimeException("this cell already has a coin");
		this.cellStatus = cellStatus;
	}

	public boolean hasCoin() {
		return cellStatus != CellStatus.NONE;
	}

	public void flip() throws RuntimeException {
		if (this.cellStatus == CellStatus.BLACK) {
			this.cellStatus = CellStatus.WHITE;
		} else if (this.cellStatus == CellStatus.WHITE) {
			this.cellStatus = CellStatus.BLACK;
		} else {
			throw new RuntimeException("this cell has no coin");
		}
	}
}

