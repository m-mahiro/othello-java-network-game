package model;

import java.util.ArrayList;

public class Othello {

	private Board board;
	private final Coin me;
	private final Coin opponent;
	private final ArrayList<Board> history = new ArrayList<>();

	public Othello(Coin myCoin) {
		this.me = myCoin;
		Coin opponentCoin = null;
		try {
			opponentCoin = myCoin.getOpposite();
		} catch (OthelloModelException e) {
			assert false;
		}
		this.opponent = opponentCoin;
		this.board = new Board();
	}

	public void put(int i, int j) throws OthelloModelException {
		board.putCoin(me, i, j);
	}

	public void putOpponent(int i, int j) throws OthelloModelException {
		board.putCoin(opponent, i, j);
	}

	public void restart() {

	}

	public void revert() {

	}

}