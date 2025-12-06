package model;

import java.util.ArrayList;
import java.util.HashSet;


public class Board implements Cloneable {
	public static final int BOARD_LENGTH = 8;
	private final Cell[][] cells;

	public Board() {

		this.cells = new Cell[BOARD_LENGTH][BOARD_LENGTH];

		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				cells[i][j] = new Cell();
			}
		}
		try {
			int a =  BOARD_LENGTH / 2 - 1;
			int b = BOARD_LENGTH / 2;
			cells[a][a].putCoin(Coin.WHITE);
			cells[b][b].putCoin(Coin.WHITE);
			cells[a][b].putCoin(Coin.BLACK);
			cells[b][a].putCoin(Coin.BLACK);
		} catch (OthelloDomainException e) {
			throw new AssertionError(e);
		}
	}


	// ============================= パブリックメソッド =============================

	public Coin getCoin(int i, int j) {
		if (0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH) {
			return this.cells[i][j].getCoin();
		} else {
			throw new IllegalArgumentException("Both argument must be [0, " + BOARD_LENGTH +"] (Given i: " + i + ", j: " + j + ")");
		}
	}

	public boolean isValidMove(Coin coin, int i, int j) {

		// エラーハンドリング
		if (coin == Coin.NONE) throw new IllegalArgumentException("coinはNONEではだめ");
		if (!(0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH)) {
			throw new IllegalArgumentException("Both argument must be [0, " + BOARD_LENGTH +"] (Given i: " + i + ", j: " + j + ")");
		}

		// すでにコインが置いてある場所にはもちろんコインを置けない。
		if (this.cells[i][j].hasCoin()) return false;

		// オセロで1枚以上コインをひっくりかえせる場所にしか、コインを新たに置けない。
		try {
			return !this.getFlippableCells(coin, i, j).isEmpty();
		} catch (OthelloDomainException e) {
			throw new AssertionError(e);
		}
	}

	public int getWhiteCoins() {
		int count = 0;
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				Coin coin = cells[i][j].getCoin();
				if (coin == Coin.WHITE) count++;
			}
		}
		return count;
	}

	public int getBlackCoins() {
		int count = 0;
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				Coin coin = cells[i][j].getCoin();
				if (coin == Coin.BLACK) count++;
			}
		}
		return count;
	}

	@Override
	public Board clone() {
		try {
			Board clone = (Board) super.clone();
			for (int i = 0; i < BOARD_LENGTH; i++ ) {
				for (int j = 0; j < BOARD_LENGTH; j++) {
					clone.cells[i][j] = this.cells[i][j].clone();
				}
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}


	// ============================= パッケージプライベートメソッド =============================

	void putCoin(Coin coin, int i, int j) throws OthelloDomainException {

		// note: なぜパッケージプライベートなのか。
		//  getCoin()とか、isValidMove()とかは、view層でも使う可能性があるからパブリックでいい。
		//  対して、putCoin()は破壊的な操作だからOthello以外に差せてはいけない操作。

		// エラーハンドリング
		if (coin == Coin.NONE) throw new IllegalArgumentException("The first argument must not be Coin.NONE");
		if (!(0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH)) throw new IllegalArgumentException("Both argument must be [0, " + BOARD_LENGTH +"] (Given i: " + i + ", j: " + j + ")");
		ArrayList<Cell> flippableCells = this.getFlippableCells(coin, i, j);
		if (this.getFlippableCells(coin, i, j).isEmpty()) throw OthelloDomainException.cannotPutCoin();

		// プレイヤーが選んだセルにコインを置いてから、ひっくり返せるやつをひっくりかえしていく。
		this.cells[i][j].putCoin(coin);
		for (Cell cell : flippableCells) cell.flip();
	}



	// ============================= プライベートメソッド =============================
	private ArrayList<Cell> getFlippableCells(Coin myCoin, int i, int j) throws OthelloDomainException {
		// note: なぜプライベートなのか？
		//  Cellをあまりmodelパッケージの外にだしたくないから。
		// note: なぜ返し値がHashMapではなくArrayListなのか？
		//  将来的に、順番にコインがひっくり返っていくという画面効果を実装するかもしれないから。

		// エラーハンドリング
		if (myCoin == Coin.NONE) throw new IllegalArgumentException("NONEはだめ");
		if (!(0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH)) throw new IllegalArgumentException("Both argument must be [0, " + BOARD_LENGTH +"] (Given i: " + i + ", j: " + j + ")");
		if (cells[i][j].hasCoin()) throw OthelloDomainException.alreadyExistsCoin();

		// 宣言
		ArrayList<Cell> flippableCells = new ArrayList<>(); // 最終的にひっくりかえせるセル
		Coin opponentCoin;                              // 相手のコイン
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloDomainException e) {
			throw new AssertionError(e); // review 使い方あってる?
		}

		// 上下左右斜めの8方向それぞれについて、ひっくりかえせるコインを探す
		for (int direction = 0; direction < BOARD_LENGTH; direction++) {

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
				if (0 <= i_dash && i_dash < BOARD_LENGTH && 0 <= j_dash && j_dash < BOARD_LENGTH) {
					cell = this.cells[i_dash][j_dash];
				} else {
					break;
				}


				if (cell.getCoin() == opponentCoin) {

					// ひっくりかえせるかもしれないので集合に追加
					cells.add(cell);
				} else if (cell.getCoin() == myCoin) {

					// 自分のコインで挟まったので、その方向のひっくりかえせるセルが確定する
					flippableCells.addAll(cells);
					break;
				} else if (cell.getCoin() == Coin.NONE){

					// コインがないセルがあったらそれまでにcellsに追加したセルはひっくりかえせないので次にdirectionに行く。
					break;
				}
			}
		}

		return flippableCells;
	}

	// ============================= デバッグ用 =============================
	public String format() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				Coin coin = this.getCoin(i, j);
				str.append(" ");
				str.append(coin);
				str.append(" ");
			}
			str.append("\n");
		}
		return str.toString();
	}


}