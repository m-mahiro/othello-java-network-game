package domain;

public enum Coin implements Cloneable {
	BLACK('○'),
	WHITE('●'),
	NONE('-');

	private char marker;

	Coin(char marker) {
		this.marker = marker;
	}

	public Coin getOpposite() throws OthelloDomainException {
		switch (this) {
			case BLACK: return WHITE;
			case WHITE: return BLACK;
			default: throw OthelloDomainException.mustNotBeNONE();
		}
	}

	@Override
	public String toString() {
		return this.marker + "";
	}
}