/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import gameElements.Game;
import gui.MessageTransactionable;
import network.ServerConnecter;

/**
 *
 * @author Kadek Yuda
 */
public class AI extends LaboAI {
  private MessageTransactionable mr;
  private ServerConnecter connector;
  private int playerID;
  private String lastCommand;
  private MinimaxAgent minimaxAgent;
  private static final String AI_NAME = "YEEBot";
  private final static String MAGIC_WORDS = "205 PLAY ";

  public AI(Game game) {
    super(game);
  }

  @Override
  public void setOutputInterface(MessageTransactionable mr) {
    this.mr = mr;
  }

  @Override
  public void getNewMessage(String message) {
    System.out.println("[received]" + message);
    reciveMessage(message);
  }

  @Override
  public void setConnecter(ServerConnecter connecter) {
    this.connector = connecter;
  }

  @Override
  public void reciveMessage(String text) {
    messageHandler(text);
  }

  @Override
  public void addMessage(String text) {
    System.out.println("[log]" + text);
  }

  public void messageHandler(String text) {
    String[] parsedMessage = text.split(" ");
    if (parsedMessage[0].equals("100") && this.gameBoard.getGameState() == gameElements.Game.STATE_WAIT_PLAYER_CONNECTION) {
      System.out.println("101 NAME " + AI_NAME);
      sendMessage("101 NAME " + AI_NAME);
    } else if (parsedMessage[0].equals("102")) {
      this.playerID = Integer.decode(parsedMessage[2]);
      this.minimaxAgent = new MinimaxAgent(playerID);
      this.gameBoard.setPlayerName(Integer.decode(parsedMessage[2]), AI_NAME);
      this.gameBoard.setGameState(Game.STATE_WAIT_PLAYER_PLAY);
    } else if (parsedMessage[0].equals("200") || parsedMessage[0].equals("206")){
      parsedMessage = (parsedMessage[0].equals("200")) ? lastCommand.split(" ") : parsedMessage;
      String option = (parsedMessage.length) == 6 ? parsedMessage[5] : "";
      this.gameBoard.play(Integer.decode(parsedMessage[2]), parsedMessage[4], parsedMessage[3], option);
      System.out.println(this.gameBoard.getBoardInformation());
    } else if (parsedMessage[0].equals("204")) {
      // Greedy
      // String command = GreedyAgent.move(gameBoard, playerID);
      String command = MAGIC_WORDS + playerID + " " + minimaxAgent.pickMove(gameBoard, playerID);
      // Minimax
      sendMessage(command);
      lastCommand = command;
    } else if (parsedMessage[0].equals("207")) {
      this.gameBoard.changeNewSeason();
    } else if (parsedMessage[0].startsWith("4")) {
      String command = (this.gameBoard.getResourcesOf(playerID).hasWorkerOf("S")) ? "205 PLAY 0 S 1-1 M" : "205 PLAY 0 P 1-1 M";
      sendMessage(command);
      lastCommand = command;
    }
    else {
      mr.addMessage("[new] " + text);
    }
  }

  @Override
  public void sendMessage(String sendText) {
    mr.sendMessage(sendText);
  }
}
