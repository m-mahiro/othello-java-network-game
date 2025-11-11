package model;

import java.util.HashSet;

public class Board {
	final private Cell[][] cells = new Cell[9][9];

	Board () {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				cells[i][j] = new Cell();
			}
		}
	}

	public Cell getCell(int i, int j) {
		return cells[i][j];
	}

	public void putCoin(CellStatus myCellStatus, int i, int j) {
		CellStatus opponentCellStatus = myCellStatus.getOppositeStatus();

		Cell cell = this.getCell(i, j);
		if (cell.hasCoin()) {
			return; // todo: また後で何とか考える.
		}
		cell.setCellStatus(myCellStatus);


		for (int direction = 0; direction < 8; direction++) {

			HashSet<Cell> cells = new HashSet<>();

			while(true) {
				int di = 0;
				int dj = 0;

//				=========== 値を更新する部分 ===========
				int[][] list = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
				int i_dash = i + list[direction][0];
				int j_dash = j + list[direction][1];
				if (0 <= i_dash && i_dash < 9 && 0 <= j_dash && j_dash < 9) {
					break;
				}

//				=========== それぞれのセルについて処理を行う部分 ===========
				cell = this.getCell(i_dash, j_dash);

				if (cell.getCellStatus() == opponentCellStatus) {
					cells.add(cell);

				} else if (cell.getCellStatus() == myCellStatus) {
					for (Cell c: cells) {
						c.setCellStatus(myCellStatus);
					}
					break;

				} else {
					break;
				}

			}

		}

	}
}