/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ai;

import gameElements.Game;
import gameElements.GameResources;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Game AI using Minimax Approach with Alpha Beta pruning for faster thinking time.
 * @author yudai
 */
public class MinimaxABAgent {
  private final static String[] PLACES_NAMES = {"1-1","2-1","2-2","3-1","3-2","3-3","3-4","4-5","5-1","5-2","5-3","5-4","5-5","6-1","6-2","6-3","7-1"};
  private final static String STUDENT = "S";
  private final static String PROFESSOR = "P";
  private final static int MAX_DEPTH = 8;
  private final int playerID;

  /**
   * Initialize Minimax Agent
   * @param playerID
   * @author Kadek Yuda
   */
  public MinimaxABAgent(int playerID) {
    this.playerID = playerID;
  }

  /**
   * Select next move to play based on game state
   * @param gameState state of the current game
   * @return String containing the next move. Must add "205 PLAY " with command.
   * @author Kadek Yuda
   */
  public String pickMove(Game gameState) {
    return minimaxAB(gameState, 0, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toString();
  }

  /**
   * Minimax algorithm with alpha and beta pruning.
   * @param gameState state of the current game
   * @param currentDepth current depth of search
   * @param isMax true if it's maximizer turn, false if it's minimizer turn
   * @param alpha alpha parameter for pruning
   * @param beta beta parameter for pruning
   * @return best move encapsulated by Move object
   * @author Kadek Yuda
   */
  public Move minimaxAB(Game gameState, int currentDepth, boolean isMax, Double alpha, Double beta) {
    int currentPlayer = (isMax) ? playerID : (playerID^1);
    ArrayList<Move> possibleMoves = getPossibleMoves(gameState, currentPlayer);
    Collections.shuffle(possibleMoves);
    if ((currentDepth == MAX_DEPTH) || gameState.getGameState() == Game.STATE_GAME_END) {
      Double value = evaluationFunction(gameState);
//      if (currentDepth == MAX_DEPTH) {
//        System.out.println("Max depth! value = " + value);
//      }
      return new Move(value);
    } else {
      Double bestValue = isMax ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      Move bestMove = new Move(new Double(0));
      for (Move move: possibleMoves) {
        Game currentGame = new Game(gameState);
        currentGame.play(currentPlayer, move.getPlace(), move.getWorker(), move.getOptions());
        Move childMove = minimaxAB(currentGame, currentDepth+1, !isMax, alpha, beta);
        if (isMax && childMove.getValue() > bestValue) {
          bestValue = childMove.getValue();
          bestMove = move;
          alpha = Double.max(alpha, bestValue);
          if (beta <= alpha) {
            break;
          }
        } else if (!isMax && bestValue > childMove.getValue()) {
          bestValue = childMove.getValue();
          bestMove = move;
          beta = Double.min(beta, bestValue);
          if (beta <= alpha) {
            break;
          }
        }
      }
      return bestMove;
    }
  }

  /**
   * Evaluation function used by this agent.
   * @param currentGameState currently running game state.
   * @return value of current game state in Double.
   * @author Kadek Yuda
   */
  public Double evaluationFunction(Game currentGameState) {
    // TODO: Check machines. If player have machine, add weight to money.
    // TODO: Add certain weight value to player flask and gear when machine has effect to them
    int playerScore = currentGameState.getScore()[playerID];
    int playerMoney = currentGameState.getResourcesOf(playerID).getCurrentMoney();
    int playerFlask = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(0);
    int playerGear = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(1);
    int playerDebt = currentGameState.getResourcesOf(playerID).getDebt();
    int opponentID = playerID^1;

    int opponentScore = currentGameState.getScore()[opponentID];
    int opponentMoney = currentGameState.getResourcesOf(opponentID).getCurrentMoney();
    int opponentFlask = currentGameState.getResourcesOf(opponentID).getCurrentResrchPoint(0);
    int opponentGear = currentGameState.getResourcesOf(opponentID).getCurrentResrchPoint(1);
    int opponentDebt = currentGameState.getResourcesOf(opponentID).getDebt();

    if (currentGameState.getGameState() == Game.STATE_GAME_END) {
      System.out.println("Reached terminal state!");
      return new Double((playerScore - opponentScore));
    } else {
      Double evalValue = new Double(0);
      evalValue += 10*(playerScore) - 3*(playerDebt);
      evalValue += 0.5*(playerMoney);
      evalValue += 2.5*(playerFlask);
      evalValue += 2.5*(playerGear);
      return evalValue;
    }
   }

    /**
   * Method used to get all the possible move.
   * @param gameState currently running game state.
   * @param playerID player ID
   * @return list of possible move.
   * @author Kadek Yuda
   */
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

  /**
   * Method used to get all available workers.
   * @param gameState currently running game state
   * @param playerID this player ID
   * @return Array of Strings indicating worker types.
   * @author Kadek Yuda
   */
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

  /**
   * Method to get possible options based on place to go.
   * @param place place the worker wants to go.
   * @return Array of options.
   * @author Kadek Yuda
   */
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
        return new String[] {"T03"};
      default:
        return new String[] {""};
    }
  }
}
