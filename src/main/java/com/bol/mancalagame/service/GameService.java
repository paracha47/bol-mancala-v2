package com.bol.mancalagame.service;

import com.bol.mancalagame.model.MancalaGame;

public interface GameService {

	MancalaGame createGame(String firstPlayer, String secondPlayer);
    MancalaGame getGameById(String gameId);

}
