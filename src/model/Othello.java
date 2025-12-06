package model;

import java.util.Stack;

public class Othello {

	public final Coin myCoin;
	public final Coin opponentCoin;

	private Board board;
	private final Stack<Board> history = new Stack<>();

	public Othello(Coin myCoin) {
		this.myCoin = myCoin;
		Coin opponentCoin = null;
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			assert false;
		}
		this.opponentCoin = opponentCoin;
		this.board = new Board();
		this.history.add(this.board.clone());
	}


	// ============================= ボードの状態を変化させる系のメソッド =============================

	public void put(int i, int j) throws OthelloModelException {
		if (!(0 <= i && i < Board.BOARD_LENGTH && 0 <= j && j < Board.BOARD_LENGTH)) {
			throw new IllegalArgumentException("Both argument must be [0," + Board.BOARD_LENGTH + "] (Given i: " + i + ", j: " + j + ")");
		}
		this.board.putCoin(myCoin, i, j);
		this.history.push(this.board);
	}

	public void putOpponent(int i, int j) throws OthelloModelException {
		if (!(0 <= i && i < Board.BOARD_LENGTH && 0 <= j && j < Board.BOARD_LENGTH)) {
			throw new IllegalArgumentException("Both argument must be [0," + Board.BOARD_LENGTH + "] (Given i: " + i + ", j: " + j + ")");
		}
		board.putCoin(opponentCoin, i, j);
		this.history.push(this.board);
	}

	public void restart() {
		this.board = new Board();
		this.history.clear();
	}

	public void revert() {
		this.board = history.pop();
	}


	// ============================= ボードの状態を確認する系のメソッド =============================

	public Coin getWinner() throws OthelloModelException {

		if (!isFinish()) throw OthelloModelException.isNotFinished();
		int black = 0;
		int white = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Coin coin = this.board.getCoin(i, j);
				switch (coin) {
					case WHITE: white++;
					case BLACK: black++;
				}
			}
		}

		if (white > black) {
			return Coin.WHITE;
		} else if (black > white) {
			return Coin.BLACK;
		} else {
			return Coin.NONE;
		}
	}

	public boolean hasNoCandidate() {
		for (int i = 0; i < Board.BOARD_LENGTH; i++) {
			for (int j = 0; j < Board.BOARD_LENGTH; j++) {
				boolean isValid = this.board.isValidMove(this.myCoin, i, j);
				if (isValid) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean hasNoCandidateForOpponent() {
		for (int i = 0; i < Board.BOARD_LENGTH; i++) {
			for (int j = 0; j < Board.BOARD_LENGTH; j++) {
				boolean isValid = this.board.isValidMove(this.opponentCoin, i, j);
				if (isValid) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isValidMove(int i, int j) {
		return this.board.isValidMove(myCoin, i, j);
	}

	public boolean isValidMoveByOpponent(int i, int j) {
		return this.board.isValidMove(opponentCoin, i, j);
	}

	public boolean isFinish() {
		return this.hasNoCandidate() && this.hasNoCandidateForOpponent();
	}


	// ============================= デバッグ用 =============================

	public String format(Coin myCoin) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < Board.BOARD_LENGTH; i++) {
			for (int j = 0; j < Board.BOARD_LENGTH; j++) {
				Coin coin = this.board.getCoin(i, j);
				switch (coin) {
					case BLACK: str.append(" ").append(Coin.BLACK).append(" "); break;
					case WHITE: str.append(" ").append(Coin.WHITE).append(" "); break;
					case NONE: {
						boolean isFlippable = this.board.isValidMove(myCoin, i, j);
						if (isFlippable) {
							int number = i * Board.BOARD_LENGTH + j;
							str.append(String.format("%2d ", number));
						} else {
							str.append(" - ");
						}
					}
				}
			}
			str.append("\n");
		}
		return str.toString();
	}
}