package com.bol.mancalagame.controller;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bol.mancalagame.constants.Constants;
import com.bol.mancalagame.dto.MancalaGameDto;
import com.bol.mancalagame.exception.MancalaException;
import com.bol.mancalagame.mapper.MancalaGameMapper;
import com.bol.mancalagame.model.MancalaGame;
import com.bol.mancalagame.service.GameService;
import com.bol.mancalagame.service.SowService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/mancala")
public class GameController {

	private static final Logger logger = LoggerFactory.getLogger(GameController.class);

	private final GameService gameService;
	
	private final SowService sowService;

	public GameController(GameService mancalaGameService, SowService gameSowService) {
		this.gameService = mancalaGameService;
		this.sowService = gameSowService;
	}

	@PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create game", notes = "create the new game")
    public ResponseEntity<MancalaGameDto> createGame(@RequestParam @NotNull String firstPlayerName,
                                                  @RequestParam @NotNull String secondPlayerName) {
	    MancalaGame game = gameService.createGame(firstPlayerName, secondPlayerName);
	    logger.info("mancala game created : {}", game);
	    
	    return new ResponseEntity<>(MancalaGameMapper.toDto(game), HttpStatus.CREATED);
    }
	
	@GetMapping("/{gameId}")
    @ApiOperation(value = "getGameById", notes = "Get Game by id")
    public ResponseEntity<MancalaGameDto> getGameById(@PathVariable String gameId) {
    	
    	MancalaGame game = gameService.getGameById(gameId);
        return new ResponseEntity<>(MancalaGameMapper.toDto(game), HttpStatus.OK);
    }
	
	@PostMapping("/{gameId}/pits/{pitIndex}")
    @ApiOperation(value = "sow", notes = "Pay the game")
	public ResponseEntity<MancalaGameDto> sow(@PathVariable String gameId, 
										   @PathVariable Integer pitIndex) {

		MancalaGame game = sowService.sow(gameId, pitIndex);
		return new ResponseEntity<>(MancalaGameMapper.toDto(game), HttpStatus.OK);
	}


}

