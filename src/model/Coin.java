package model;

public enum Coin implements Cloneable {
	BLACK,
	WHITE,
	NONE;

	public Coin getOpposite() throws OthelloModelException {
		switch (this) {
			case BLACK: return WHITE;
			case WHITE: return BLACK;
			default: throw OthelloModelException.noCoin();
		}
	}

}

