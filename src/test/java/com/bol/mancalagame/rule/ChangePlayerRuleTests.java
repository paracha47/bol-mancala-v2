package com.bol.mancalagame.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.service.GameService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChangePlayerRuleTests.class)
public class ChangePlayerRuleTests {
	
	@MockBean
	ChangePlayerRule changePlayerRule;

	@MockBean
	GameService gameService;
	
	String firstPlayer = "Daniel";
	String secondPlayer = "peterson";
	
	@BeforeEach
	void setUp() {
		changePlayerRule = new ChangePlayerRule();
	}
	
	@Test
	void testChangePlayerAtoPlayerB() {
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
				.playerTurn(PlayerTurn.PLAYER_B)
				.build();
		
		GameRuleDto ruleDto = GameRuleDto.builder()
				  .game(expectedGame).
				  requestPitIndex(1)
				  .currentPitIndex(3)
				  .build();
		
		changePlayerRule.process(ruleDto);
				
		assertEquals(expectedGame.getPlayerTurn().getTurn(), ruleDto.getGame().getPlayerTurn().getTurn());
	}
	
	@Test
	void testChangePlayerBtoPlayerA() {
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
				.build();
		
		GameRuleDto ruleDto = GameRuleDto.builder()
				  .game(expectedGame).
				  requestPitIndex(1)
				  .currentPitIndex(3)
				  .build();
		
		changePlayerRule.process(ruleDto);
				
		assertEquals(expectedGame.getPlayerTurn().getTurn(), ruleDto.getGame().getPlayerTurn().getTurn());
	}
}
