package com.bol.mancalagame.rule;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.ruleengine.GameRule;

public class ChangePlayerRule implements GameRule{

	@Override
	public void process(GameRuleDto gameRuleDto) {
		
		MancalaGame game = gameRuleDto.getGame();
		
		if(game.getWinner() !=null)
			return;
		
		if(game.getPlayerTurn().getTurn() == 0 &&  gameRuleDto.getCurrentPitIndex() == Constants.RIGHT_BIG_PIT_ID)
        	return;
        
        if(game.getPlayerTurn().getTurn() == 1 && gameRuleDto.getCurrentPitIndex() == Constants.LEFT_BIG_PIT_ID)
        	return;
        
		if (game.getPlayerTurn().getTurn() == 0) {
        	game.setPlayerTurn(PlayerTurn.PLAYER_B);
		}else {
			game.setPlayerTurn(PlayerTurn.PLAYER_A);
		}
	}

	
}
