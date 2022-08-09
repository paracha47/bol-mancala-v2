package com.bol.mancalagame.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bol.mancalagame.dto.MancalaGameDto;
import com.bol.mancalagame.dto.MancalaPitDto;
import com.bol.mancalagame.dto.PlayerDto;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.model.MancalaPit;
import com.bol.mancalagame.model.Player;
import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.repository.GameRepository;
import com.bol.mancalagame.ruleengine.GameRuleEngine;
import com.bol.mancalagame.service.GameServiceImpl;
import com.bol.mancalagame.service.SowServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
@Import({GameServiceImpl.class,SowServiceImpl.class})
public class GameControllerIntegrationTests {
	
	@Value("${number.of.stones.per.pit}")
	private int numberOfStonesPerPit;

	@Value("${number.of.pits.per.player}")
	private int numberOfPitsPerPlayer;

	@Value("${game.board.size}")
	private int gameBoardSize;
	
	@MockBean
	GameRepository gameRepository;
	
	@MockBean
	GameRuleEngine gameRuleEngine;
	
	@Autowired
	MockMvc mockMvc;

	MancalaGame game;

	ObjectMapper mapper = new ObjectMapper();
	
	String firstPlayer = "Daniel";
	String secondPlayer = "peterson";
	
	@BeforeEach
	void setUp() {
		
		List<MancalaPit> pits = new ArrayList<MancalaPit>();
		for (int i = 0; i < gameBoardSize; i++) {
			if (i == 6 || i == 13) {
				pits.add(new MancalaPit(i,0));
			} else {
				pits.add(new MancalaPit(i, numberOfStonesPerPit));
			}
		}
		
		game = MancalaGame.builder()
                .id("game-test-1")
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
    public void testGameCreation() throws Exception {
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("firstPlayerName", firstPlayer);
        queryParams.add("secondPlayerName", secondPlayer);
        
        this.mockMvc.perform(post("/mancala/create")
        		.queryParams(queryParams))        
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }
	
	@Test
    void TestGetGameById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/mancala/{gameId}", "game-test-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("game-test-1"));
    }	
	
	@Test
    void TestSow() throws Exception {
		
		List<MancalaPitDto> pits = Arrays.asList(
				new MancalaPitDto(0, 0),
				new MancalaPitDto(1, 7),
				new MancalaPitDto(2, 7),
				new MancalaPitDto(3, 7),
				new MancalaPitDto(4, 7),
				new MancalaPitDto(5, 7),
				new MancalaPitDto(6, 1),
				new MancalaPitDto(7, 6),
				new MancalaPitDto(8, 6),
				new MancalaPitDto(9,  6),
				new MancalaPitDto(10, 6),
				new MancalaPitDto(11, 6),
				new MancalaPitDto(12, 6),
				new MancalaPitDto(13, 0));
		
		MancalaGameDto game = MancalaGameDto.builder()
                .id("game-test-1")
                .pits(pits)
                .players(Arrays.asList(
                		PlayerDto.builder().name(firstPlayer).build(),
                		PlayerDto.builder().name(secondPlayer).build()))
                .playerTurn(PlayerTurn.PLAYER_A)
                .build();
		
        this.mockMvc.perform(post("/mancala/game-test-1/pits/0")
                        .accept(MediaType.APPLICATION_JSON))
				        .andDo(print())
				        .andExpect(status().isOk())
				        .andExpect(content().json(mapper.writeValueAsString(game)));

    }	
}
