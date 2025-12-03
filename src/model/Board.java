package model;

import java.util.HashSet;

// パッケージプライベート
class Board {
	private final int BOARD_LENGTH = 8;
	private final Cell[][] cells = new Cell[BOARD_LENGTH][BOARD_LENGTH];

	public Board () {
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				cells[i][j] = new Cell();
			}
		}
		try {
			cells[3][3].setCoin(Coin.WHITE);
			cells[4][4].setCoin(Coin.WHITE);
			cells[3][4].setCoin(Coin.BLACK);
			cells[4][3].setCoin(Coin.BLACK);
		} catch (OthelloModelException e) {
			assert false;
		}
	}

	private Cell getCell(int i, int j)  {
		try {
			return cells[i][j];
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Both argument must be [0, 7] (Given i: " + i + "j: " + j + ")");
		}
	}

	public void putCoin(Coin coin, int i, int j) throws OthelloModelException {
		HashSet<Cell> flippableCells = this.getFlippableCells(coin, i, j);
		for (Cell cell : flippableCells) {
			cell.setCoin(coin);
		}
	}

	public boolean[][] getCandidates(Coin myCoin) {
		boolean[][] candidates = new boolean[BOARD_LENGTH][BOARD_LENGTH];
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				int flippableCells = this.getFlippableCells(myCoin, i, j).size();
				candidates[i][j] = (flippableCells == 0);
			}
		}
		return candidates;
	}

	public HashSet<Cell> getFlippableCells(Coin myCoin, int i, int j) {

		// 宣言
		HashSet<Cell> flippableCells = new HashSet<>(); // 最終的にひっくりかえせるセル
		Coin opponentCoin;                       // 相手のコイン
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			throw new IllegalArgumentException("myCoin must not be Coin.NONE");
		}

		// 上下左右斜めの8方向それぞれについて、ひっくりかえせるコインを探す
		for (int direction = 0; direction < BOARD_LENGTH; direction++) {

			// ひっくりかえせるかもしれないコインの集合
			HashSet<Cell> cells = new HashSet<>();

			// 基準のセルからdirectionの方向に操作する
			while(true) {
				int di = 0, dj = 0;

                // 次のセルを確認しに行く（移動する方向はdirectionに応じて変化）
				int[][] list = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
				int i_dash = i + list[direction][0];
				int j_dash = j + list[direction][1];
				if (0 <= i_dash && i_dash < 8 && 0 <= j_dash && j_dash < 8) break;
				Cell cell = this.getCell(i_dash, j_dash);

				// ひっくりかえせるかもしれないので集合に追加
				if (cell.getCoin() == opponentCoin) {
					cells.add(cell);
				}

				// 自分のコインで挟まったので間の者を全部ひっくりかえせる
				else if (cell.getCoin() == myCoin) {
					flippableCells.addAll(cells);
				}

				// コインがないセルがあったらそれまでにcellsに追加したセルはひっくりかえせないので次にdirectionに行く。
				else if (cell.getCoin() == Coin.NONE){
					break;
				}
			}
		}

		return flippableCells;
	}
}