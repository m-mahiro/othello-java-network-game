package model;

// パッケージプライベート
// review: パッケージプライベートにするぐらいだったら、Boardからしか使われていないのだからBoardのインナークラスにしたらどうなの?
//  スパゲッティにならないのであれば、ファイルが長くなっても特に問題じゃないはず。
//  なぜクラスを作るのかというと、外部から再利用してもらうため。再利用されないクラスは中にしまってしまえ的な。
class Cell implements Cloneable {

	private Coin coin;

	Cell () {
		this.coin = Coin.NONE;
	}

	public Coin getCoin() {
		return this.coin;
	}

	public void putCoin(Coin coin) throws OthelloDomainException {
		if (this.hasCoin()) throw OthelloDomainException.alreadyExistsCoin();
		this.coin = coin;
	}

	public boolean hasCoin() {
		return coin != Coin.NONE;
	}

	public void flip() throws OthelloDomainException {
		this.coin = this.coin.getOpposite();
	}

	@Override
	public String toString() {
		return this.coin.toString();
	}

	@Override
	public Cell clone() {
		try {
			Cell clone = (Cell) super.clone();
			clone.coin = this.coin;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}
}

