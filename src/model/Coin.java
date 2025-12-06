package model;

public enum Coin implements Cloneable {
	BLACK {
		@Override
		public String toString() {
			return "○";
		}
	},
	WHITE {
		@Override
		public String toString() {
			return "●";
		}
	},
	NONE {
		@Override
		public String toString() {
			return " ";
		}
	};

	public Coin getOpposite() throws OthelloModelException {
		switch (this) {
			case BLACK: return WHITE;
			case WHITE: return BLACK;
			default: throw OthelloModelException.mustNotBeNONE();
		}
	}

}

