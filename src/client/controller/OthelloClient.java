package client.controller;


import client.network.MessageClient;

public class OthelloClient {
	public static void main(String[] args) {
		MessageClient messageClient = new MessageClient("m-mahiro");
		messageClient.start(); // todo: この操作はコンストラクタに内包すべき?
	}


}