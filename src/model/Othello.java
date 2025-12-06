package model;

import java.util.Arrays;
import java.util.Stack;

public class Othello {

	private Board board;
	private final Coin me;
	private final Coin opponent;
	private final Stack<Board> history = new Stack<>();
	private final int BOARD_LENGTH = 8;

	public Othello(Coin myCoin) {
		this.me = myCoin;
		Coin opponentCoin = null;
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			assert false;
		}
		this.opponent = opponentCoin;
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

	public boolean isFinish() {
		boolean[][] myCandidates = this.board.getCandidates(me);
		boolean[][] opponentCandidates = this.board.getCandidates(opponent);
		boolean me = Arrays.stream(myCandidates).allMatch(booleans -> false);
		boolean opponent = Arrays.stream(opponentCandidates).allMatch(booleans -> false);
		return me && opponent;
	}

	public void put(int i, int j) throws OthelloModelException {
		if (0 <= i && i < BOARD_LENGTH && 0 <= j && j < BOARD_LENGTH) {
			this.board.putCoin(me, i, j);
			this.history.push(this.board);
		} else {
			throw new IllegalArgumentException("Both argument must be [0," + BOARD_LENGTH + "] (Given i: " + i + ", j: " + j + ")");
		}
	}

	public void putOpponent(int i, int j) throws OthelloModelException {
		board.putCoin(opponent, i, j);
		this.history.push(this.board);
	}

	public void restart() {
		this.board = new Board(BOARD_LENGTH);
		this.history.clear();
	}

	public void revert() {
		this.board = history.pop();
	}

	public String format() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < BOARD_LENGTH; i++) {
			for (int j = 0; j < BOARD_LENGTH; j++) {
				Coin coin = this.board.getCoin(i, j);
				switch (coin) {
					case BLACK: str.append("  ●"); break;
					case WHITE: str.append("  ○"); break;
					case NONE: {
						boolean isFlippable = this.board.isAbleToPut(this.me, i, j);
						if (isFlippable) {
							int number = i * BOARD_LENGTH + j;
							str.append(String.format("%3d", number));
						} else {
							str.append("   ");
						}
					}
				}
			}
			str.append("\n");
		}
		return str.toString();
	}
}