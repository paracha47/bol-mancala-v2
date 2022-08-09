package com.bol.mancalagame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.model.Winner;
import com.bol.mancalagame.repository.GameRepository;
import com.bol.mancalagame.ruleengine.GameRuleEngine;
import com.bol.mancalagame.ruleengine.GameRuleEngineImpl;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SowService.class)
@Import({GameServiceImpl.class})
public class SowServiceTests {

	@Value("${number.of.stones.per.pit}")
	private int numberOfStonesPerPit;

	@Value("${number.of.pits.per.player}")
	private int numberOfPitsPerPlayer;

	@Value("${game.board.size}")
	private int gameBoardSize;
	
	@MockBean
	GameService gameService;
	
	@MockBean
	SowService sowService;
	
	@MockBean
	GameRepository gameRepository;
	
	@MockBean
    GameRuleEngine gameRuleEngine;
	
	String firstPlayer = "Daniel";
	String secondPlayer = "peterson";
	
	@BeforeEach
	void setUp() {
		
		sowService = new SowServiceImpl(gameService,gameRepository,new GameRuleEngineImpl());

		List<MancalaPit> pits = new ArrayList<MancalaPit>();
		for (int i = 0; i < gameBoardSize; i++) {
			if (i == 6 || i == 13) {
				pits.add(new MancalaPit(i,0));
			} else {
				pits.add(new MancalaPit(i, numberOfStonesPerPit));
			}
		}
		
		MancalaGame game = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_A)
                .build();
		
		when(gameService.getGameById(anyString())).thenReturn(game);
		when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(MancalaGame.class))).
                thenAnswer((invocation) -> invocation.getArguments()[0]);
	}
	
	@Test
	void testFirstSowSuccessfully() {
		MancalaGame game = sowService.sow("test-1", 0);
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 7),
				new MancalaPit(2, 7),
				new MancalaPit(3, 7),
				new MancalaPit(4, 7),
				new MancalaPit(5, 7),
				new MancalaPit(6, 1),
				new MancalaPit(7, 6),
				new MancalaPit(8, 6),
				new MancalaPit(9,  6),
				new MancalaPit(10, 6),
				new MancalaPit(11, 6),
				new MancalaPit(12, 6),
				new MancalaPit(13, 0));

		MancalaGame expectedGame = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_A)
                .build();
		
		assertEquals(expectedGame.getPits().get(0).getStones(), game.getPits().get(0).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPlayerTurn().getTurn(), game.getPlayerTurn().getTurn());
		
    }
	
	@Test
	void testThirdSowSuccessfully() {
		MancalaGame game = sowService.sow("test-1", 3);
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 6),
				new MancalaPit(1, 6),
				new MancalaPit(2, 6),
				new MancalaPit(3, 0),
				new MancalaPit(4, 7),
				new MancalaPit(5, 7),
				new MancalaPit(6, 1),
				new MancalaPit(7, 7),
				new MancalaPit(8, 7),
				new MancalaPit(9,  7),
				new MancalaPit(10, 6),
				new MancalaPit(11, 6),
				new MancalaPit(12, 6),
				new MancalaPit(13, 0));

		MancalaGame expectedGame = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_B)
                .build();
		
		assertEquals(expectedGame.getPits().get(0).getStones(), game.getPits().get(0).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPlayerTurn().getTurn(), game.getPlayerTurn().getTurn());
		
    }
	
	@Test
	void testSecondPlayerMove() {
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 6),
				new MancalaPit(1, 6),
				new MancalaPit(2, 0),
				new MancalaPit(3, 7),
				new MancalaPit(4, 7),
				new MancalaPit(5, 7),
				new MancalaPit(6, 1),
				new MancalaPit(7, 7),
				new MancalaPit(8, 7),
				new MancalaPit(9,  6),
				new MancalaPit(10, 6),
				new MancalaPit(11, 6),
				new MancalaPit(12, 6),
				new MancalaPit(13, 0));
		
		MancalaGame FirstPlayerGame = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_B)
                .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		MancalaGame secondPlayerDame = sowService.sow("test-1", 10);

		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 7),
				new MancalaPit(1, 7),
				new MancalaPit(2, 7),
				new MancalaPit(3, 7),
				new MancalaPit(4, 7),
				new MancalaPit(5, 7),
				new MancalaPit(6, 1),
				new MancalaPit(7, 7),
				new MancalaPit(8, 7),
				new MancalaPit(9,  6),
				new MancalaPit(10, 0),
				new MancalaPit(11, 7),
				new MancalaPit(12, 7),
				new MancalaPit(13, 1));

		MancalaGame expectedGame = MancalaGame.builder()
                .id("test-1")
                .pits(secondPlayerPits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_A)
                .build();
		
		assertEquals(expectedGame.getPits().get(0).getStones(), secondPlayerDame.getPits().get(0).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPlayerTurn().getTurn(), secondPlayerDame.getPlayerTurn().getTurn());
		
    }
	
	@Test
	void testLastStoneInBigPitPlayerA() {
		MancalaGame game = sowService.sow("test-1", 0);
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 7),
				new MancalaPit(2, 7),
				new MancalaPit(3, 7),
				new MancalaPit(4, 7),
				new MancalaPit(5, 7),
				new MancalaPit(6, 1),
				new MancalaPit(7, 6),
				new MancalaPit(8, 6),
				new MancalaPit(9, 6),
				new MancalaPit(10, 6),
				new MancalaPit(11, 6),
				new MancalaPit(12, 6),
				new MancalaPit(13, 0));

		MancalaGame expectedGame = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_A)
                .build();
		
		assertEquals(expectedGame.getPits().get(0).getStones(), game.getPits().get(0).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPlayerTurn().getTurn(), game.getPlayerTurn().getTurn());
		
    }
	
	@Test
	void testLastStoneInBigPitPlayerB() {
		
		List<MancalaPit> firstPits = Arrays.asList(
				new MancalaPit(0, 6),
				new MancalaPit(1, 6),
				new MancalaPit(2, 6),
				new MancalaPit(3, 6),
				new MancalaPit(4, 6),
				new MancalaPit(5, 6),
				new MancalaPit(6, 0),
				new MancalaPit(7, 6),
				new MancalaPit(8, 6),
				new MancalaPit(9, 6),
				new MancalaPit(10, 6),
				new MancalaPit(11, 6),
				new MancalaPit(12, 6),
				new MancalaPit(13, 0));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
                .id("test-1")
                .pits(firstPits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_B)
                .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);

		MancalaGame game = sowService.sow("test-1", 7);
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 6),
				new MancalaPit(1, 6),
				new MancalaPit(2, 6),
				new MancalaPit(3, 6),
				new MancalaPit(4, 6),
				new MancalaPit(5, 6),
				new MancalaPit(6, 0),
				new MancalaPit(7, 0),
				new MancalaPit(8, 7),
				new MancalaPit(9, 7),
				new MancalaPit(10, 7),
				new MancalaPit(11, 7),
				new MancalaPit(12, 7),
				new MancalaPit(13, 1));

		MancalaGame expectedGame = MancalaGame.builder()
                .id("test-1")
                .pits(pits)
                .players(Arrays.asList(
                		Player.builder().name(firstPlayer).build(),
                		Player.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_B)
                .build();
		
		assertEquals(expectedGame.getPits().get(0).getStones(), game.getPits().get(0).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), game.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPlayerTurn().getTurn(), game.getPlayerTurn().getTurn());
		
    }
	
	@Test
	void testLastStoneInEmptyPitForPlayerA() {
			List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 3),
				new MancalaPit(1, 2),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 12),
				new MancalaPit(6, 7),
				new MancalaPit(7, 0),
				new MancalaPit(8, 12),
				new MancalaPit(9, 11),
				new MancalaPit(10, 1),
				new MancalaPit(11, 12),
				new MancalaPit(12, 9),
				new MancalaPit(13, 3));
		
		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		MancalaGame secondPlayerDame = sowService.sow("test-1", 1);
	
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 3),
				new MancalaPit(1, 0),
				new MancalaPit(2, 1),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 12),
				new MancalaPit(6, 19),
				new MancalaPit(7, 0),
				new MancalaPit(8, 12),
				new MancalaPit(9, 11),
				new MancalaPit(10, 1),
				new MancalaPit(11, 12),
				new MancalaPit(12, 9),
				new MancalaPit(13, 3));
	
		MancalaGame expectedGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(secondPlayerPits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		
		assertEquals(expectedGame.getPits().get(3).getStones(), secondPlayerDame.getPits().get(3).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
	void testLastLastStoneInEmptyPitForPlayerB() {
			List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 3),
				new MancalaPit(2, 16),
				new MancalaPit(3, 12),
				new MancalaPit(4, 10),
				new MancalaPit(5, 10),
				new MancalaPit(6, 2),
				new MancalaPit(7, 0),
				new MancalaPit(8, 8),
				new MancalaPit(9, 0),
				new MancalaPit(10, 1),
				new MancalaPit(11, 0),
				new MancalaPit(12, 4),
				new MancalaPit(13, 6));
		
		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		MancalaGame secondPlayerDame = sowService.sow("test-1", 10);
	
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 16),
				new MancalaPit(3, 12),
				new MancalaPit(4, 10),
				new MancalaPit(5, 10),
				new MancalaPit(6, 2),
				new MancalaPit(7, 0),
				new MancalaPit(8, 8),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 4),
				new MancalaPit(13, 10));
	
		MancalaGame expectedGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(secondPlayerPits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		
		assertEquals(expectedGame.getPits().get(12).getStones(), secondPlayerDame.getPits().get(12).getStones());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	
	@Test
	void testGameOver() {
			List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 1),
				new MancalaPit(6, 49),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 1),
				new MancalaPit(11, 0),
				new MancalaPit(12, 4),
				new MancalaPit(13, 17));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 0),
				new MancalaPit(6, 50),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 0),
				new MancalaPit(13, 22));
		
		MancalaGame expectedGame = MancalaGame.builder()
				.id("test-1")
				.pits(secondPlayerPits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_A)
				.winner(Winner.builder().playerName(firstPlayer).score(50).build())
				.build();
				
		MancalaGame secondPlayerDame = sowService.sow("test-1", 5);
		assertEquals(expectedGame.getWinner().getPlayerName(), secondPlayerDame.getWinner().getPlayerName());
		assertEquals(expectedGame.getWinner().getScore(), secondPlayerDame.getWinner().getScore());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
	void testGameOverAndWonPlayerB() {
			List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 1),
				new MancalaPit(4, 0),
				new MancalaPit(5, 4),
				new MancalaPit(6, 17),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 1),
				new MancalaPit(13, 49));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 0),
				new MancalaPit(6, 22),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 0),
				new MancalaPit(13, 50));
		
		MancalaGame expectedGame = MancalaGame.builder()
				.id("test-1")
				.pits(secondPlayerPits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_B)
				.winner(Winner.builder().playerName(secondPlayer).score(50).build())
				.build();
				
		MancalaGame secondPlayerDame = sowService.sow("test-1", 12);
		assertEquals(expectedGame.getWinner().getPlayerName(), secondPlayerDame.getWinner().getPlayerName());
		assertEquals(expectedGame.getWinner().getScore(), secondPlayerDame.getWinner().getScore());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
	void testGameDraw() {
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 1),
				new MancalaPit(4, 0),
				new MancalaPit(5, 3),
				new MancalaPit(6, 32),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 1),
				new MancalaPit(13, 35));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 0),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 0),
				new MancalaPit(6, 36),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 0),
				new MancalaPit(13, 36));
		
		MancalaGame expectedGame = MancalaGame.builder()
				.id("test-1")
				.pits(secondPlayerPits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_B)
				.winner(Winner.builder()
						.message("Game is draw")
						.playerName(secondPlayer).score(36).build())
				.build();
				
		MancalaGame secondPlayerDame = sowService.sow("test-1", 12);
		assertEquals(expectedGame.getWinner().getMessage(), secondPlayerDame.getWinner().getMessage());
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
    void testWrongIndexInput() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",-1),
                "pit Index should be between 0 to 6 or 7 to 12"
        );
        assertTrue(exception.getMessage().contains("pit Index should be between 0 to 6 or 7 to 12"));
    }
	
	@Test
    void testNullIndexInput() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",null),
                "pit Index should be between 0 to 6 or 7 to 12"
        );
        assertTrue(exception.getMessage().contains("pit Index should be between 0 to 6 or 7 to 12"));
    }
	
	@Test
    void testGreaterIndexInputFromTotalPits() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",15),
                "pit Index should be between 0 to 6 or 7 to 12"
        );
        assertTrue(exception.getMessage().contains("pit Index should be between 0 to 6 or 7 to 12"));
    }
	
	@Test
    void testBigPitPLayerBIndexInput() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",13),
                "No movement on Big pits"
        );
        assertTrue(exception.getMessage().contains("No movement on Big pits"));
    }
	
	@Test
    void testBigPitPLayerAIndexInput() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",6),
                "No movement on Big pits"
        );
        assertTrue(exception.getMessage().contains("No movement on Big pits"));
    }
	
	@Test
    void testEmptyPitIndexInput() {
		
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 5),
				new MancalaPit(1, 4),
				new MancalaPit(2, 3),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 0),
				new MancalaPit(6, 50),
				new MancalaPit(7, 0),
				new MancalaPit(8, 0),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 0),
				new MancalaPit(12, 0),
				new MancalaPit(13, 22));
		
		MancalaGame game = MancalaGame.builder()
				.id("test-1")
				.pits(pits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_A)
				.winner(Winner.builder().playerName(firstPlayer).score(50).build())
				.build();

		when(gameService.getGameById(anyString())).thenReturn(game);

        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",3),
                "Empty Pit"
        );
        assertTrue(exception.getMessage().contains("Empty Pit"));
    }
	
	@Test
    void testInvalidUserPitSelectionByPlayerA() {
        MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",10),
                "Invalid user Pit selection"
        );
        assertTrue(exception.getMessage().contains("Invalid user Pit selection"));
    }
	
	@Test
	void testInvalidUserPitSelectionByPlayerB() {
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 0),
				new MancalaPit(1, 3),
				new MancalaPit(2, 16),
				new MancalaPit(3, 12),
				new MancalaPit(4, 10),
				new MancalaPit(5, 10),
				new MancalaPit(6, 2),
				new MancalaPit(7, 0),
				new MancalaPit(8, 8),
				new MancalaPit(9, 0),
				new MancalaPit(10, 1),
				new MancalaPit(11, 0),
				new MancalaPit(12, 4),
				new MancalaPit(13, 6));
		
		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> sowService.sow("test-1",1),
                "Invalid user Pit selection"
        );
        assertTrue(exception.getMessage().contains("Invalid user Pit selection"));
	}
	
	@Test
	void testPlayerAOppositeBigPitIgnore() {
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 4),
				new MancalaPit(1, 1),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 9),
				new MancalaPit(6, 16),
				new MancalaPit(7, 6),
				new MancalaPit(8, 9),
				new MancalaPit(9, 1),
				new MancalaPit(10, 1),
				new MancalaPit(11, 0),
				new MancalaPit(12, 4),
				new MancalaPit(13, 21));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 5),
				new MancalaPit(1, 2),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 0),
				new MancalaPit(5, 0),
				new MancalaPit(6, 17),
				new MancalaPit(7, 7),
				new MancalaPit(8, 10),
				new MancalaPit(9, 2),
				new MancalaPit(10, 2),
				new MancalaPit(11, 1),
				new MancalaPit(12, 5),
				new MancalaPit(13, 21));
		
		MancalaGame expectedGame = MancalaGame.builder()
				.id("test-1")
				.pits(secondPlayerPits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_B)
				.build();
				
		MancalaGame secondPlayerDame = sowService.sow("test-1", 5);
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
	void testPlayerBOppositeBigPitIgnore() {
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 8),
				new MancalaPit(1, 4),
				new MancalaPit(2, 0),
				new MancalaPit(3, 0),
				new MancalaPit(4, 1),
				new MancalaPit(5, 2),
				new MancalaPit(6, 17),
				new MancalaPit(7, 0),
				new MancalaPit(8, 1),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 5),
				new MancalaPit(12, 9),
				new MancalaPit(13, 25));

		MancalaGame FirstPlayerGame = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		when(gameService.getGameById(anyString())).thenReturn(FirstPlayerGame);
		
		List<MancalaPit> secondPlayerPits = Arrays.asList(
				new MancalaPit(0, 9),
				new MancalaPit(1, 5),
				new MancalaPit(2, 1),
				new MancalaPit(3, 1),
				new MancalaPit(4, 2),
				new MancalaPit(5, 3),
				new MancalaPit(6, 17),
				new MancalaPit(7, 1),
				new MancalaPit(8, 2),
				new MancalaPit(9, 0),
				new MancalaPit(10, 0),
				new MancalaPit(11, 5),
				new MancalaPit(12, 0),
				new MancalaPit(13, 26));
		
		MancalaGame expectedGame = MancalaGame.builder()
				.id("test-1")
				.pits(secondPlayerPits)
				.players(Arrays.asList(
						Player.builder().name(firstPlayer).build(),
						Player.builder().name(secondPlayer).build()))
				.playerTurn(PlayerTurn.PLAYER_A)
				.build();
				
		MancalaGame secondPlayerDame = sowService.sow("test-1", 12);
		assertEquals(expectedGame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(expectedGame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones(), secondPlayerDame.getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
}
