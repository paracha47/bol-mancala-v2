package com.bol.mancalagame.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.repository.GameRepository;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameService.class)
public class GameServiceTests {

	@Value("${number.of.stones.per.pit}")
	private int numberOfStonesPerPit;

	@Value("${number.of.pits.per.player}")
	private int numberOfPitsPerPlayer;

	@Value("${game.board.size}")
	private int gameBoardSize;
	
	GameService gameService;
	
	@MockBean
	GameRepository gameRepository;
	
	String firstPlayer = "Daniel";
	String secondPlayer = "peterson";
	
	@BeforeEach
	void setUp() {
		gameService = new GameServiceImpl(numberOfStonesPerPit, numberOfPitsPerPlayer, gameBoardSize, gameRepository);

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
		
		when(gameRepository.findById(anyString())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(MancalaGame.class))).
                thenAnswer((invocation) -> invocation.getArguments()[0]);
	}
	
	@Test
	void testSavedGameSuccessfully() {
		MancalaGame game = gameService.createGame(firstPlayer, secondPlayer);
		assertGame(game);
	}
	
	@Test
	void testGameFindById() {
		MancalaGame game = gameService.getGameById("test-1");
		assertGame(game);
	}

	private void assertGame(MancalaGame game) {
		System.out.println(game.toString());
		assertThat(game).isNotNull();
		assertThat(game.getPlayers()).isNotNull();
		assertEquals(2, game.getPlayers().size());
		assertEquals(PlayerTurn.PLAYER_A, game.getPlayerTurn());
		assertEquals(firstPlayer, game.getPlayers().get(0).getName());
		assertEquals(secondPlayer, game.getPlayers().get(1).getName());
		assertEquals(gameBoardSize, game.getPits().size());
		for (int i = 0; i < 14; i++) {
			if (i == 6 || i == 13) {
				assertEquals(0, game.getPits().get(i).getStones());
			} else {
				assertEquals(numberOfStonesPerPit, game.getPits().get(i).getStones());
			}
		}
	}
	
	@Test
    void testGameNotFound() {
        
		when(gameRepository.findById("Dummy")).thenReturn(Optional.empty());

		MancalaException exception = assertThrows(
        		MancalaException.class,
                () -> gameService.getGameById("Dummy"),
                "Game Not Found =>() Dummy"
        );
        
        assertTrue(exception.getMessage().contains("Game Not Found =>() Dummy"));
    }
	
}
