package com.bol.mancalagame.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService{

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    @Value("${number.of.stones.per.pit}")
    private int numberOfStonesPerPit;
    
    @Value("${number.of.pits.per.player}")
    private int numberOfPitsPerPlayer;
    
    @Value("${game.board.size}")
    private int gameBoardSize;
    
    private final GameRepository gameRepository;

    @Autowired
	public GameServiceImpl(GameRepository mancalaGameRepository) {
				this.gameRepository = mancalaGameRepository;
	}
    
	public GameServiceImpl(int numberOfStonesPerPit, int numberOfPitsPerPlayer, int gameBoardSize,
			GameRepository mancalaGameRepository) {
		this.numberOfStonesPerPit = numberOfStonesPerPit;
		this.numberOfPitsPerPlayer = numberOfPitsPerPlayer;
		this.gameBoardSize = gameBoardSize;
		this.gameRepository = mancalaGameRepository;
	}

	@Override
	public MancalaGame createGame(String firstPlayer, String secondPlayer) {
		
		logger.info("----- start creating mancala game-----");
	    List<Player> players = Arrays.asList(
                this.createPlayer(firstPlayer),
                this.createPlayer(secondPlayer));
	    
	    List<MancalaPit> pits = createPits(numberOfStonesPerPit, gameBoardSize);
		MancalaGame mancalaGame = MancalaGame.builder()
					                .pits(pits)
					                .players(players)
					                .playerTurn(PlayerTurn.PLAYER_A)
					                .build();
		
        gameRepository.save(mancalaGame);
        
		logger.info("---- Game created successfully -> ()",mancalaGame);

        return mancalaGame;
	}
	
	private Player createPlayer(String playerName) {
        logger.info("create mancala player : {}", playerName);
        return Player.builder()
                .name(playerName)
                .build();
    }
	
	private List<MancalaPit> createPits(Integer pitStones,int gameBoardSize) {
		List<MancalaPit> pits = new ArrayList<MancalaPit>();
		for (int i = 0; i < gameBoardSize; i++) {
			if (i == 6 || i == 13) {
				pits.add(new MancalaPit(i,0));
			} else {
				pits.add(new MancalaPit(i, pitStones));
			}
		}
		return pits;
	}
	
	@Override
	public MancalaGame getGameById(String gameId) {
		Optional<MancalaGame> mancalaGame = gameRepository.findById(gameId);
        if (mancalaGame.isEmpty())
            throw new MancalaException("Game Not Found =>() " + gameId);

        return mancalaGame.get();
	}

}
