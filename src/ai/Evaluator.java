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
public class Evaluator {
  public static int evaluateGameState(Game currentGameState, int playerID) {
    int score = currentGameState.getScore()[playerID];
    int money = currentGameState.getResourcesOf(playerID).getCurrentMoney();
    int flask = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(0);
    int gear = currentGameState.getResourcesOf(playerID).getCurrentResrchPoint(1);
    int debt = currentGameState.getResourcesOf(playerID).getDebt();
    // TODO: Tune parameters
    return 10*score + money + 3*flask + 2*gear - 30*debt;
  }
}
