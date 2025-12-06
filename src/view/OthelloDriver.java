package view;

import model.Coin;
import model.Othello;
import model.OthelloModelException;

import java.util.Scanner;

public class OthelloDriver {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Othello othello = new Othello(Coin.BLACK); // ●

		for (int tern = 0; tern < 64; tern++) {
			System.out.println(othello.format());

			if (tern % 2 == 0) {
				System.out.print("●の番です: ");
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
				System.out.print("○の番です: ");
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
					System.out.println("●の勝ち");
				} else if (winner == Coin.WHITE) {
					System.out.println("○の勝ち");
				} else {
					System.out.println("引き分け");
				}
				break;
			}
		}

	}

}
