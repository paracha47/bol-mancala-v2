package com.bol.mancalagame.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CheckLastStoneInEmptyPitRuleTests.class)
public class CheckLastStoneInEmptyPitRuleTests {

	@MockBean
	CheckLastStoneInEmptyPitRule checkLastStoneInEmptyPitRule;
	
	String firstPlayer = "Daniel";
	String secondPlayer = "peterson";
	
	@BeforeEach
	void setUp() {
		checkLastStoneInEmptyPitRule = new CheckLastStoneInEmptyPitRule();
	}
	
	@Test
	void testLastStoneInEmptyPitForPlayerA() {
		List<MancalaPit> pits = Arrays.asList(
				new MancalaPit(0, 3),
				new MancalaPit(1, 0),
				new MancalaPit(2, 1),
				new MancalaPit(3, 1),
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
	
		MancalaGame game = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_A)
	            .build();
		
		GameRuleDto ruleDto = GameRuleDto.builder()
				  .game(game).requestPitIndex(1)
				  .currentPitIndex(3)
				  .build();
		checkLastStoneInEmptyPitRule.process(ruleDto);
	
		assertEquals(19, ruleDto.getGame().getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(3, ruleDto.getGame().getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
	
	@Test
	void testLastStoneInEmptyPitForPlayerB() {
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
				new MancalaPit(10, 0),
				new MancalaPit(11, 1),
				new MancalaPit(12, 4),
				new MancalaPit(13, 6));
	
		MancalaGame game = MancalaGame.builder()
	            .id("test-1")
	            .pits(pits)
	            .players(Arrays.asList(
	            		Player.builder().name(firstPlayer).build(),
	            		Player.builder().name(secondPlayer).build()))
	            .playerTurn(PlayerTurn.PLAYER_B)
	            .build();
		
		GameRuleDto ruleDto = GameRuleDto.builder()
				  .game(game).requestPitIndex(10)
				  .currentPitIndex(11)
				  .build();
		checkLastStoneInEmptyPitRule.process(ruleDto);
	
		assertEquals(2, ruleDto.getGame().getPits().get(Constants.RIGHT_BIG_PIT_ID).getStones());
		assertEquals(10, ruleDto.getGame().getPits().get(Constants.LEFT_BIG_PIT_ID).getStones());
	}
}
