package domain;

public class OthelloDomainException extends Exception {
	public OthelloDomainException(String message) {
		super(message);
	}

	// hack: とりあえず引数全部なしで。

	public static OthelloDomainException alreadyExistsCoin() {
		return new OthelloDomainException("A Coin has already exited");
	}

	public static OthelloDomainException mustNotBeNONE() {
		return new OthelloDomainException("The coin must not be NONE here.");
	}

	public static OthelloDomainException cannotPutCoin() {
		return new OthelloDomainException("Cannot put a coin there");
	}

	public static OthelloDomainException noSuchCell() {
		return new OthelloDomainException("No such cell");
	}

	public static OthelloDomainException invalidNoneCoin() {
		return new OthelloDomainException("NONE coin is invalid");
	}

	public static OthelloDomainException isNotFinished() {return new OthelloDomainException("The Othello game haven't finished");}

}

