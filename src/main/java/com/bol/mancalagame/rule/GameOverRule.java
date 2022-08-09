package com.bol.mancalagame.rule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.model.Winner;
import com.bol.mancalagame.ruleengine.GameRule;
import com.bol.mancalagame.util.GameUtil;

@Component
public class GameOverRule implements GameRule{

	@Override
	public void process(GameRuleDto gameRuleDto) {
		
		MancalaGame game = gameRuleDto.getGame();
		
		int totalStoneOfPlayerA = GameUtil.getTotalStonesOfPlayer(game.getPits(), PlayerTurn.PLAYER_A);
		int totalStoneOfPlayerB = GameUtil.getTotalStonesOfPlayer(game.getPits(), PlayerTurn.PLAYER_B);
		
		if(totalStoneOfPlayerA > 0 && totalStoneOfPlayerB > 0) 
			return;
		
		List<MancalaPit> pits = game.getPits();
		
		for (int i = 0; i < 14; i++) {
			if (i == Constants.RIGHT_BIG_PIT_ID) {
				pits.get(i).setStones(pits.get(i).getStones() + totalStoneOfPlayerA);
			} else if (i == Constants.LEFT_BIG_PIT_ID) {
				pits.get(i).setStones(pits.get(i).getStones() + totalStoneOfPlayerB);
			}else {
				pits.get(i).setStones(0);
			}
		}
		
		Winner winner = null;
		
		if(pits.get(Constants.RIGHT_BIG_PIT_ID).getStones() > pits.get(Constants.LEFT_BIG_PIT_ID).getStones()) {
			winner = Winner.builder()
							.playerName(game.getPlayers().get(PlayerTurn.PLAYER_A.getTurn()).getName())
							.score(pits.get(Constants.RIGHT_BIG_PIT_ID).getStones())
							.message(game.getPlayers().get(PlayerTurn.PLAYER_A.getTurn()).getName()+" is the game winner")
							.build();
		} else if(pits.get(Constants.RIGHT_BIG_PIT_ID).getStones() == pits.get(Constants.LEFT_BIG_PIT_ID).getStones()) {
			winner = Winner.builder()
					.message("Game is draw")
					.build();
		} else {
			winner = Winner.builder()
					.playerName(game.getPlayers().get(PlayerTurn.PLAYER_B.getTurn()).getName())
					.message(game.getPlayers().get(PlayerTurn.PLAYER_B.getTurn()).getName()+" is the game winner")
					.score(pits.get(Constants.LEFT_BIG_PIT_ID).getStones())
					.build();
		}
		game.setWinner(winner);
	}
}
