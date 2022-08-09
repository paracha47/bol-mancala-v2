package com.bol.mancalagame.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.model.Winner;
import com.bol.mancalagame.repository.GameRepository;
import com.bol.mancalagame.ruleengine.GameRuleEngine;
import com.bol.mancalagame.ruleengine.GameRuleEngineImpl;
import com.bol.mancalagame.util.GameUtil;

@Service
public class SowServiceImpl implements SowService{

    private static final Logger logger = LoggerFactory.getLogger(SowServiceImpl.class);

    private final GameService gameService;
    private final GameRepository gameRepository;
    private final GameRuleEngine gameRuleEngine;

	public SowServiceImpl(GameService gameService, GameRepository gameRepository,GameRuleEngine gameRuleEngine) {
		this.gameService = gameService;
		this.gameRepository = gameRepository;
		this.gameRuleEngine = gameRuleEngine;
	}

	@Override
	public MancalaGame sow(String gameId, Integer requestPitIndex) {

		logger.info("Call gameSow =>() . GameId: " + gameId + "  , pit Index: " + requestPitIndex);
		if (requestPitIndex == null || requestPitIndex < 0 || requestPitIndex > Constants.LEFT_BIG_PIT_ID)
			throw new MancalaException("pit Index should be between 0 to 6 or 7 to 12");
		
		if (requestPitIndex == Constants.RIGHT_BIG_PIT_ID || requestPitIndex == Constants.LEFT_BIG_PIT_ID)
			throw new MancalaException("No movement on Big pits");

		MancalaGame game = gameService.getGameById(gameId);
        // No movement on Big pits
        
        // we need to check if request comes from the right player otherwise we do not sow the game. In other words,
        if (game.getPlayerTurn() == PlayerTurn.PLAYER_A && 
					        		requestPitIndex > Constants.RIGHT_BIG_PIT_ID ||
					                game.getPlayerTurn() == PlayerTurn.PLAYER_B && 
					                requestPitIndex < Constants.RIGHT_BIG_PIT_ID)
			throw new MancalaException("Invalid user Pit selection");
        
        sowRight(game, requestPitIndex);
        GameUtil.printAllPits(game);
        logger.info("Current Game Turn : {}", game.getPlayerTurn());

        return gameRepository.save(game);
	}
	
	private void sowRight(MancalaGame game, Integer requestPitIndex) {
        
		Integer currentPitIndex = requestPitIndex;
		List<MancalaPit> pits = game.getPits();
        int currentPitStones = pits.get(currentPitIndex).getStones();
       
        if(currentPitStones == Constants.EMPTY_STONE)
			throw new MancalaException("Empty Pit");
        
        pits.get(currentPitIndex).setStones(0);
        
        while(currentPitStones > 0) {
        	currentPitIndex++;
        	if(currentPitIndex == Constants.TOTAL_PITS)
        		currentPitIndex = 0;
    		if(!(game.getPlayerTurn() == PlayerTurn.PLAYER_A &&  currentPitIndex == Constants.LEFT_BIG_PIT_ID ||
    				game.getPlayerTurn() == PlayerTurn.PLAYER_B &&  currentPitIndex == Constants.RIGHT_BIG_PIT_ID)) {
    			
    			int stones = pits.get(currentPitIndex).getStones()+1;
    			pits.get(currentPitIndex).setStones(stones);
    			currentPitStones--;
    		}
        }
        
        GameRuleDto ruleDto = GameRuleDto.builder()
        					  .game(game).requestPitIndex(requestPitIndex)
        					  .currentPitIndex(currentPitIndex)
        					  .build();
        
        gameRuleEngine.processGameRules(ruleDto);
	}

}
