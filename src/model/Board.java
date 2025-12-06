package model;

import java.util.HashSet;

// パッケージプライベート
class Board implements Cloneable {
	private int boardLength;
	private final Cell[][] cells;

	public Board(int boardLength) {

		if (boardLength % 2 == 1) {
			throw new IllegalArgumentException("一辺のセルの数は偶数である必要があります。");
		}

		this.boardLength = boardLength;
		this.cells = new Cell[boardLength][boardLength];

		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				cells[i][j] = new Cell();
			}
		}
		try {
			int a =  boardLength / 2 - 1;
			int b = boardLength / 2;
			cells[a][a].putCoin(Coin.WHITE);
			cells[b][b].putCoin(Coin.WHITE);
			cells[a][b].putCoin(Coin.BLACK);
			cells[b][a].putCoin(Coin.BLACK);
		} catch (OthelloModelException e) {
			throw new AssertionError(e);
		}
	}

	public Coin getCoin(int i, int j) {
		if (0 <= i && i < boardLength && 0 <= j && j < boardLength) {
			return this.cells[i][j].getCoin();
		} else {
			throw new IllegalArgumentException("Both argument must be [0, " + boardLength +"] (Given i: " + i + ", j: " + j + ")");
		}
	}

	public void putCoin(Coin coin, int i, int j) throws OthelloModelException {

		if (coin == Coin.NONE) throw new IllegalArgumentException("NONEではだめ");
		if (!(0 <= i && i < boardLength && 0 <= j && j < boardLength)) throw new IllegalArgumentException("Both argument must be [0, " + boardLength +"] (Given i: " + i + ", j: " + j + ")");

		HashSet<Cell> flippableCells = this.getFlippableCells(coin, i, j);
		if (flippableCells.isEmpty()) throw OthelloModelException.cannotPutCoin();

		Cell firstCell = this.cells[i][j];
		firstCell.putCoin(coin);

		for (Cell cell : flippableCells) {
			cell.flip();
		}
	}

	public boolean[][] getCandidates(Coin myCoin) {
		if (myCoin == Coin.NONE) throw new IllegalArgumentException("coinはNONEではだめ");

		boolean[][] candidates = new boolean[boardLength][boardLength];
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				int flippableCells = this.getFlippableCells(myCoin, i, j).size();
				candidates[i][j] = (flippableCells == 0);
			}
		}
		return candidates;
	}

	public boolean isAbleToPut(Coin coin, int i, int j) {
		if (coin == Coin.NONE) throw new IllegalArgumentException("coinはNONEではだめ");
		if (!(0 <= i && i < boardLength && 0 <= j && j < boardLength)) {
			throw new IllegalArgumentException("Both argument must be [0, " + boardLength +"] (Given i: " + i + ", j: " + j + ")");
		}

		return !this.getFlippableCells(coin, i, j).isEmpty();

	}

	/**
	 * WARNING: 一番初めに置いたセルは結果に含まれない
	 * @param myCoin
	 * @param i
	 * @param j
	 * @return
	 */
	public HashSet<Cell> getFlippableCells(Coin myCoin, int i, int j) {

		if (myCoin == Coin.NONE) throw new IllegalArgumentException("NONEはだめ");
		if (!(0 <= i && i < boardLength && 0 <= j && j < boardLength)) throw new IllegalArgumentException("Both argument must be [0, " + boardLength +"] (Given i: " + i + ", j: " + j + ")");

		// 宣言
		HashSet<Cell> flippableCells = new HashSet<>(); // 最終的にひっくりかえせるセル
		Coin opponentCoin;                              // 相手のコイン
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			throw new AssertionError(e); // review 使い方あってる?
		}

		// 上下左右斜めの8方向それぞれについて、ひっくりかえせるコインを探す
		for (int direction = 0; direction < boardLength; direction++) {

			// ひっくりかえせるかもしれないコインの集合
			HashSet<Cell> cells = new HashSet<>();

			int i_dash = i;
			int j_dash = j;
			// 基準のセルからdirectionの方向に操作する
			while(true) {
                // 次のセルを確認しに行く（移動する方向はdirectionに応じて変化）
				int[][] list = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
				i_dash += list[direction][0];
				j_dash += list[direction][1];
				Cell cell;
				if (0 <= i_dash && i_dash < boardLength && 0 <= j_dash && j_dash < boardLength) {
					cell = this.cells[i_dash][j_dash];
				} else {
					break;
				}


				if (cell.getCoin() == opponentCoin) {

					// ひっくりかえせるかもしれないので集合に追加
					cells.add(cell);
				} else if (cell.getCoin() == myCoin) {

					// 自分のコインで挟まったので間の者を全部ひっくりかえせる
					flippableCells.addAll(cells);
				} else if (cell.getCoin() == Coin.NONE){

					// コインがないセルがあったらそれまでにcellsに追加したセルはひっくりかえせないので次にdirectionに行く。
					break;
				}
			}
		}

		return flippableCells;
	}

	@Override
	public Board clone() {
		try {
			Board clone = (Board) super.clone();
			for (int i = 0; i < boardLength; i++ ) {
				for (int j = 0; j < boardLength; j++) {
					clone.cells[i][j] = this.cells[i][j].clone();
				}
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
}