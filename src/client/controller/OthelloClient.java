package client.controller;


import client.network.MessageClientThread;

public class OthelloClient {
	public static void main(String[] args) {
		MessageClientThread messageClientThread = new MessageClientThread("m-mahiro");
		messageClientThread.start(); // todo: この操作はコンストラクタに内包すべき?
	}


}