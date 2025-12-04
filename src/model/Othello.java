package model;

import java.util.ArrayList;
import java.util.Stack;

public class Othello {

	private Board board;
	private final Coin me;
	private final Coin opponent;
	private final Stack<Board> history = new Stack<>();

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
		this.history.add(this.board.clone());
	}

	public void put(int i, int j) throws OthelloModelException {
		this.board.putCoin(me, i, j);
		this.history.push(this.board);
	}

	public void putOpponent(int i, int j) throws OthelloModelException {
		board.putCoin(opponent, i, j);
		this.history.push(this.board);
	}

	public void restart() {
		this.board = new Board();
		this.history.clear();
	}

	public void revert() {
		this.board = history.pop();
	}

}