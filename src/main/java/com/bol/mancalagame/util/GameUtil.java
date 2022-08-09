package com.bol.mancalagame.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.PlayerTurn;

public class GameUtil {
	
    private static final Logger logger = LoggerFactory.getLogger(GameUtil.class);

    public static void printAllPits(MancalaGame game) {
		
		List<MancalaPit> pits = game.getPits();
		StringBuilder print = new StringBuilder();
		
		print.append("Player Turn "+ game.getPlayerTurn().getTurn());
		logger.info(print.toString());
		print = new StringBuilder();
		
		print.append(pits.get(13).getStones() + " | " )
								 .append(pits.get(12).getStones() + " ")
								 .append(pits.get(11).getStones() + " ")
								 .append(pits.get(10).getStones() + " ")
								 .append(pits.get(9).getStones() + " ")
								 .append(pits.get(8).getStones() + " ")
								 .append(pits.get(7).getStones() + " ");
		
		logger.info(print.toString());
		
		print = new StringBuilder();
		print.append(pits.get(0).getStones() + " ")
								 .append(pits.get(1).getStones() + " ")
								 .append(pits.get(2).getStones() + " ")
								 .append(pits.get(3).getStones() + " ")
								 .append(pits.get(4).getStones() + " ")
								 .append(pits.get(5).getStones() + " ")
								 .append(" | "+pits.get(6).getStones());

		logger.info("    "+print);

	} 
    
    public static int getTotalStonesOfPlayer(List<MancalaPit> pits, PlayerTurn playerTurn){
        if(playerTurn == PlayerTurn.PLAYER_A){
            return pits.stream()
                    .filter(p -> p.getId() < 6)
                    .mapToInt(MancalaPit::getStones)
                    .sum();
        }else{
            return pits.stream()
                    .filter(p -> p.getId() > 6 && p.getId() < 13)
                    .mapToInt(MancalaPit::getStones)
                    .sum();
        }
    }

}
