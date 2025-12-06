package model;

import java.util.Scanner;

public class OthelloDriver {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Othello othello = new Othello(Coin.BLACK);

		for (int tern = 0; tern < 64; tern++) {

			System.out.println("\n\n");

			if (tern % 2 == 0) {
				if (othello.hasNoCandidate()) {
					System.out.println("あなた(" + othello.myCoin + ")のおける場所はありません。");
					continue;
				}
				System.out.println(othello.format(othello.myCoin));
				System.out.print("あなた(" + othello.myCoin + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				if (!othello.isValidMove(i, j)) {
					System.out.println("その場所には置けません。");
					tern--;
					continue;
				}

				try {
					othello.put(i, j);
				} catch (OthelloModelException e) {
					e.printStackTrace();
					tern--;
					continue;
				}

			} else {
				if (othello.hasNoCandidateForOpponent()) {
					System.out.println("あいて(" + othello.opponentCoin + ")のおける場所はありません。");
					continue;
				}

				System.out.println(othello.format(othello.opponentCoin));
				System.out.print("あいて(" + othello.opponentCoin + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				if (!othello.isValidMoveByOpponent(i, j)) {
					System.out.println("その場所にコインはおけません。");
				}
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

				if (winner == othello.opponentCoin) {
					System.out.println(othello.opponentCoin + "の勝ち");
				} else if (winner == othello.myCoin) {
					System.out.println(othello.myCoin + "の勝ち");
				} else {
					System.out.println("引き分け");
				}
				break;
			}
		}

	}

}
