package model;

import java.util.Scanner;

public class OthelloDriver {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Othello othello = new Othello(Coin.BLACK);

		for (int tern = 0; true; tern++) {

			System.out.println("\n\n");
			System.out.println("n or p");
			String command = sc.next();
			switch (command) {
				case "n": break;
				case "p":
					othello.revert();
					tern -= 2;
					continue;
			}

			if (tern % 2 == 0) {
				System.out.println(othello.format(othello.myCoin));
				boolean hasNoCandidates = othello.hasNoCandidate();
				System.out.println(hasNoCandidates);
				if (hasNoCandidates) {
					System.out.println("あなた(" + othello.myCoin + ")のおける場所はありません。");
					continue;
				}
				System.out.print("あなた(" + othello.myCoin + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				if (!(0 <= i && i < Board.BOARD_LENGTH && 0 <= j && j < Board.BOARD_LENGTH)) {
					System.out.println("不適切な入力");
					tern--;
					continue;
				}
				if (!othello.isValidMove(i, j)) {
					System.out.println("その場所には置けません。");
					tern--;
					continue;
				}

				try {
					othello.put(i, j);
				} catch (OthelloDomainException e) {
					e.printStackTrace();
					tern--;
					continue;
				}

			} else {
				System.out.println(othello.format(othello.opponentCoin));
				boolean hasNoCandidates = othello.hasNoCandidateForOpponent();
				System.out.println(hasNoCandidates);
				if (hasNoCandidates) {
					System.out.println("あいて(" + othello.opponentCoin + ")のおける場所はありません。");
					continue;
				}

				System.out.print("あいて(" + othello.opponentCoin + ")の番です: ");
				int n = sc.nextInt();
				int i = n / 8; // HACK: 本当は定数を使うべき
				int j = n % 8;
				if (!(0 <= i && i < Board.BOARD_LENGTH && 0 <= j && j < Board.BOARD_LENGTH)) {
					System.out.println("不適切な入力");
					tern--;
					continue;
				}
				if (!othello.isValidMoveByOpponent(i, j)) {
					System.out.println("その場所にコインはおけません。");
				}
				try {
					othello.putOpponent(i, j);
				} catch (OthelloDomainException e) {
					e.printStackTrace();
					tern--;
					continue;
				}
			}

			if (othello.isFinish()) {
				System.out.println(othello.format(Coin.BLACK)); // 引数は何でもよかった

				Coin winner = null;
				try {
					winner = othello.getWinner();
				} catch (OthelloDomainException e) {
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
