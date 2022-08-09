package com.bol.mancalagame.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bol.mancalagame.model.MancalaGame;

@Repository
public interface GameRepository extends MongoRepository<MancalaGame, String> {

}
