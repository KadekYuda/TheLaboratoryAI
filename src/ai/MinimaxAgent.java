/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import gameElements.Game;
import gameElements.GameResources;
import java.util.ArrayList;

/**
 *
 * @author yudai
 */
public class MinimaxAgent {
  private final static String[] PLACES_NAMES = {"1-1","2-1","2-2","3-1","3-2","3-3","3-4","4-1","4-2","4-3","4-4","4-5","5-1","5-2","5-3","5-4","5-5","6-1","6-2","6-3","7-1"};
  private final static String STUDENT = "S";
  private final static String PROFESSOR = "P";
  private final static int MAX_DEPTH = 1;
  private int playerID;

  public MinimaxAgent(int playerID){
    this.playerID = playerID;
  }

  public Double evaluationFunction(Game currentGameState) {
    // TODO: Tune parameters
    int playerScore = currentGameState.getScore()[playerID];
    int playerMoney = currentGameState.getResourcesOf(playerID).getCurrentMoney();
    int playerFlask = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(0);
    int playerGear = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(1);

    int opponentID = playerID^1;

    int opponentScore = currentGameState.getScore()[opponentID];
    int opponentMoney = currentGameState.getResourcesOf(opponentID).getCurrentMoney();
    int opponentFlask = currentGameState.getResourcesOf(opponentID).getCurrentResrchPoint(0);
    int opponentGear = currentGameState.getResourcesOf(opponentID).getCurrentResrchPoint(1);

    return new Double(100*(playerScore-opponentScore) + (playerMoney-opponentMoney) + (playerFlask-opponentFlask) + (playerGear-opponentGear));
  }

  public String pickMove(Game gameState, int playerID) {
    return minimax(gameState, 0, true).toString();
  }

  public Move minimax(Game gameState, int currentDepth, boolean isMax) {
    int currentPlayer = (isMax) ? playerID : (playerID^1);
    ArrayList<Move> possibleMoves = getPossibleMoves(gameState, currentPlayer);
    if ((currentDepth == MAX_DEPTH) || possibleMoves.isEmpty()) {
      return new Move(evaluationFunction(gameState));
    } else {
      Double bestValue = isMax ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      Move bestMove = new Move(new Double(0));
      for (Move move: possibleMoves) {
        Game currentGame = new Game(gameState);
        currentGame.play(currentPlayer, move.getPlace(), move.getWorker(), move.getOptions());
        Move childMove = minimax(currentGame, currentDepth+1, !isMax);

        if (isMax && childMove.getValue() > bestValue) {
          bestValue = childMove.getValue();
          bestMove = move;
        } else if (!isMax && bestValue > childMove.getValue()) {
          bestValue = childMove.getValue();
          bestMove = move;
        }
      }
      return bestMove;
    }
  }

  public static ArrayList<Move> getPossibleMoves(Game gameState, int playerID) {
    ArrayList<Move> possibleMoves = new ArrayList<>();
    String[] availableWorkers = getAvailableWorkers(gameState, playerID);
    for (String place: PLACES_NAMES) {
      for (String worker: availableWorkers) {
        for (String options: getPossibleOptions(place)) {
          if (gameState.canPutWorker(playerID, place, worker, options)) possibleMoves.add(new Move(worker, place, options));
        }
      }
    }
    return possibleMoves;
  }

  private static String[] getAvailableWorkers(Game gameState, int playerID) {
    GameResources gameResources = gameState.getResourcesOf(playerID);
    ArrayList<String> availableWorkers = new ArrayList<>();
    if (gameResources.hasWorkerOf(STUDENT)){
        availableWorkers.add(STUDENT);
    }
    if (gameResources.hasWorkerOf(PROFESSOR)) {
      availableWorkers.add(PROFESSOR);
    }
    return availableWorkers.toArray(new String[availableWorkers.size()]);
  }

  private static String[] getPossibleOptions(String place) {
    switch (place) {
      case "1-1":
        return new String[] {"F", "G", "M"};
      case "2-1":
      case "3-4":
        return new String[] {"F", "G"};
      case "6-2":
        return new String[] {"FF", "FG", "GG"};
      case "7-1":
        return new String[] {"T00", "T01", "T02", "T03", "T04", "T05"};
      default:
        return new String[] {""};
    }
  }
}
