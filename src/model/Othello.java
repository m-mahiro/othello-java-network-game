package model;

import java.util.Arrays;
import java.util.Stack;

public class Othello {

	public final Coin myCoin;
	public final Coin opponentCoin;

	private Board board;
	private final Stack<Board> history = new Stack<>();
	private final int BOARD_LENGTH = 8;

	public Othello(Coin myCoin) {
		this.myCoin = myCoin;
		Coin opponentCoin = null;
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			assert false;
		}
		this.opponentCoin = opponentCoin;
		this.board = new Board(BOARD_LENGTH);
		this.history.add(this.board.clone());
	}

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

	public boolean hasCandidate(Coin myCoin) {
		boolean[][] candidates = this.board.getCandidates(myCoin);
		for (boolean[] row : candidates) {
			for (boolean flag : row) {
				if (flag) {
					return true;
				}
			}
		}
		return false;

	}

	public boolean isFinish() {
		boolean me = this.hasCandidate(this.myCoin);
		boolean opponent = this.hasCandidate(this.opponentCoin);
		return !me && !opponent;
	}

	public void put(int i, int j) throws OthelloModelException {
		if (!(0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH)) {
			throw new IllegalArgumentException("Both argument must be [0," + BOARD_LENGTH + "] (Given i: " + i + ", j: " + j + ")");
		}
		this.board.putCoin(myCoin, i, j);
		this.history.push(this.board);
	}

	public void putOpponent(int i, int j) throws OthelloModelException {
		if (!(0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH)) {
			throw new IllegalArgumentException("Both argument must be [0," + BOARD_LENGTH + "] (Given i: " + i + ", j: " + j + ")");
		}
		board.putCoin(opponentCoin, i, j);
		this.history.push(this.board);
	}

	public void restart() {
		this.board = new Board(BOARD_LENGTH);
		this.history.clear();
	}

	public void revert() {
		this.board = history.pop();
	}

	public String format(Coin myCoin) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				Coin coin = this.board.getCoin(i, j);
				switch (coin) {
					case BLACK: str.append(" ").append(Coin.BLACK).append(" "); break;
					case WHITE: str.append(" ").append(Coin.WHITE).append(" "); break;
					case NONE: {
						boolean isFlippable = this.board.isAbleToPut(myCoin, i, j);
						if (isFlippable) {
							int number = i * BOARD_LENGTH + j;
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