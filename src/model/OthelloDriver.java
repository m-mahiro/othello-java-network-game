package model;

import java.util.Scanner;

public class OthelloDriver {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Othello othello = new Othello(Coin.BLACK); // " + Coin.BLACK + "

		for (int tern = 0; tern < 64; tern++) {

			System.out.println("\n\n");

			if (tern % 2 == 0) {
				if (!othello.hasCandidate(othello.myCoin)) {
					System.out.println("あなた(" + Coin.BLACK + ")のおける場所はありません。");
					continue;
				}
				System.out.println(othello.format(othello.myCoin));
				System.out.print("あなた(" + Coin.BLACK + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				try {
					othello.put(i, j);
				} catch (OthelloModelException e) {
					e.printStackTrace();
					tern--;
					continue;
				}

			} else {
				if (!othello.hasCandidate(othello.opponentCoin)) {
					System.out.println("あいて(" + Coin.WHITE + ")のおける場所はありません。");
					continue;
				}

				System.out.println(othello.format(othello.opponentCoin));
				System.out.print("あいて(" + Coin.WHITE + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				try {
					othello.putOpponent(i, j);
				} catch (OthelloModelException e) {
					e.printStackTrace();
					tern--;
					continue;
				}
			}

			if (othello.isFinish()) {

				Coin winner = null;
				try {
					winner = othello.getWinner();
				} catch (OthelloModelException e) {
					assert false;
				}

				if (winner == Coin.BLACK) {
					System.out.println(Coin.BLACK + "の勝ち");
				} else if (winner == Coin.WHITE) {
					System.out.println(Coin.WHITE + "の勝ち");
				} else {
					System.out.println("引き分け");
				}
				break;
			}
		}

	}

}
