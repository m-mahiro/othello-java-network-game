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

	public void setCoin(Coin coin) throws OthelloModelException {
		if (this.hasCoin()) throw OthelloModelException.alreadyExistsCoin();
		this.coin = coin;
	}

	public boolean hasCoin() {
		return coin != Coin.NONE;
	}

	public void flip() throws OthelloModelException {
		this.coin = this.coin.getOpposite();
	}

}

