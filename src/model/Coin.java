package model;

public enum Coin implements Cloneable {
	BLACK('○'),
	WHITE('●'),
	NONE('-');

	private char marker;

	Coin(char marker) {
		this.marker = marker;
	}

	public Coin getOpposite() throws OthelloModelException {
		switch (this) {
			case BLACK: return WHITE;
			case WHITE: return BLACK;
			default: throw OthelloModelException.mustNotBeNONE();
		}
	}

	@Override
	public String toString() {
		return this.marker + "";
	}
}