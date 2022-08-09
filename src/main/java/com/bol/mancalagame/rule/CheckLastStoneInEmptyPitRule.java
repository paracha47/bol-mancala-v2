package com.bol.mancalagame.rule;

import java.util.List;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.ruleengine.GameRule;

public class CheckLastStoneInEmptyPitRule implements GameRule {

	@Override
	public void process(GameRuleDto gameRuleDto) {

		MancalaGame game = gameRuleDto.getGame();
		List<MancalaPit> pits = gameRuleDto.getGame().getPits();

		if(pits.get(gameRuleDto.getCurrentPitIndex()).getStones() != 1 
				|| gameRuleDto.getCurrentPitIndex() == Constants.RIGHT_BIG_PIT_ID 
				|| gameRuleDto.getCurrentPitIndex() == Constants.LEFT_BIG_PIT_ID ) 
			return;
		
		int oppositeIndex = Constants.TOTAL_PLAYABLE_PITS - gameRuleDto.getCurrentPitIndex();
		
        MancalaPit oppositePit = pits.get(oppositeIndex);
        
        if(oppositePit.getStones() == 0)
        	return;
        
        if(game.getPlayerTurn().getTurn() == 0 && gameRuleDto.getCurrentPitIndex() < Constants.RIGHT_BIG_PIT_ID){
        	pits.get(Constants.RIGHT_BIG_PIT_ID).setStones(
					        		    pits.get(gameRuleDto.getCurrentPitIndex()).getStones() + oppositePit.getStones() +
					        		    pits.get(Constants.RIGHT_BIG_PIT_ID).getStones());
        	pits.get(oppositePit.getId()).setStones(0);
        	pits.get(gameRuleDto.getCurrentPitIndex()).setStones(0);
        
        } else if (game.getPlayerTurn().getTurn() == 1 && gameRuleDto.getCurrentPitIndex() > Constants.RIGHT_BIG_PIT_ID) {
        	pits.get(Constants.LEFT_BIG_PIT_ID).setStones(
					        			 pits.get(gameRuleDto.getCurrentPitIndex()).getStones() + oppositePit.getStones() +
					        			 pits.get(Constants.LEFT_BIG_PIT_ID).getStones());
        	pits.get(oppositePit.getId()).setStones(0);
        	pits.get(gameRuleDto.getCurrentPitIndex()).setStones(0);
        	
		}
	}

}
