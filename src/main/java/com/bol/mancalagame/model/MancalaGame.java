package com.bol.mancalagame.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Document
@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MancalaGame {
	
	@Id
	private String id;
	private List<MancalaPit> pits;
    private PlayerTurn playerTurn;
    private List<Player> players;
    private Winner winner;
    
}
