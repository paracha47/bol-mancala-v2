package com.bol.mancalagame.dto;

import java.util.List;

import com.bol.mancalagame.model.PlayerTurn;
import com.bol.mancalagame.model.Winner;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MancalaGameDto {

	private String id;
	private List<MancalaPitDto> pits;
    private PlayerTurn playerTurn;
    private List<PlayerDto> players;
    private WinnerDto winner;


}
