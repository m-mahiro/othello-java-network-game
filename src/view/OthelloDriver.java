package view;

import model.Coin;
import model.Othello;

import java.util.Scanner;

public class OthelloDriver {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Othello othello = new Othello(Coin.BLACK);
		System.out.println(othello);

		System.out.println("まずはあなたのターン");
		for (int tern = 0; true; tern++) {
			int i = sc.nextInt();
			if (i == -1) break;
			int j = sc.nextInt();

			if (tern % 2 == 0) {
				try {
					othello.put(i, j);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("次は相手のターン");
			} else {
				try {
					othello.put(i, j);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("次は相手のターン");
			}
		}
	}

}
