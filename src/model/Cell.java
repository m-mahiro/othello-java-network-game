package model;

// パッケージプライベート
class Cell implements Cloneable {

	private Coin coin;

	Cell () {
		this.coin = Coin.NONE;
	}

	public Coin getCoin() {
		return this.coin;
	}

	public void putCoin(Coin coin) throws OthelloModelException {
		if (this.hasCoin()) throw OthelloModelException.alreadyExistsCoin();
		this.coin = coin;
	}

	public boolean hasCoin() {
		return coin != Coin.NONE;
	}

	public void flip() throws OthelloModelException {
		this.coin = this.coin.getOpposite();
	}

	@Override
	public String toString() {
		switch (this.coin) {
			case WHITE: return "黒";
			case BLACK: return "白";
			case NONE: return " ";
			default: throw new AssertionError();
		}
	}

	@Override
	public Cell clone() {
		try {
			return (Cell) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
}

