/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import gameElements.Game;

/**
 *
 * @author yudai
 */
public class GreedyAgent {
  private final static int INVALID_MOVE = -1000;

  private final static String STUDENT = "S";
  private final static String PROFESSOR = "P";

  private final static String[] PLACES_NAMES = {"1-1","2-1","2-2","3-1","3-2","3-3","3-4","4-1","4-2","4-3","4-4","4-5","5-1","5-2","5-3","5-4","5-5","6-1","6-2","6-3","7-1"};

  private final static String MAGIC_WORDS = "205 PLAY ";

  public static int evaluateMove(Game currentGameState, int playerID, String place, String workerType, String option) {
    if (!currentGameState.canPutWorker(playerID, place, workerType, option)) {
      return INVALID_MOVE;
    } else {
      currentGameState.play(playerID, place, workerType, option);
      return Evaluator.evaluateGameState(currentGameState, playerID);
    }
  }

  public static String move(Game currentGameState, int playerID) {
    System.out.println("Possible Moves: ");
    for(Move moves: MinimaxAgent.getPossibleMoves(currentGameState, playerID)) {
      System.out.println(moves.toString());
    }
    System.out.println("==================================");
    String workerPlayed = currentGameState.getResourcesOf(playerID).hasWorkerOf(STUDENT) ? STUDENT : PROFESSOR;
    int[] values = new int[21];
    int maxValue = 0;
    String maxPlace = "";
    String maxOption = "";
    for(int i = 0; i < 21; i++){
      Game currentGame = new Game(currentGameState);
      String option = createOptions(currentGame, playerID, PLACES_NAMES[i]);
      values[i] = evaluateMove(currentGame, playerID, PLACES_NAMES[i], workerPlayed, option);
      if (values[i] > maxValue) {
        maxValue = values[i];
        maxPlace = PLACES_NAMES[i];
        maxOption = option;
      }
      System.out.println(PLACES_NAMES[i] + ": " + values[i]);
    }
    System.out.println("==================================");
    return MAGIC_WORDS + Integer.toString(playerID) + " " + workerPlayed + " " + maxPlace + " " + maxOption;
  }

  private static String createOptions(Game gameState, int playerID, String place) {
    switch (place) {
      case "1-1":
        return "F";
      case "2-1":
      case "3-4":
        return "F";
      case "6-2":
        if (gameState.getResourcesOf(playerID).getCurrentResrchPoint(0) == 0) {
          return "GG";
        } else if (gameState.getResourcesOf(playerID).getCurrentResrchPoint(1) == 0) {
          return "FF";
        } else {
          return "FG";
        }
      case "7-1":
        return "T00";
      default:
        return "";
    }
  }
}
