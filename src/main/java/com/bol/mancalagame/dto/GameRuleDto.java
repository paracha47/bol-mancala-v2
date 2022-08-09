package com.bol.mancalagame.dto;

import com.bol.mancalagame.model.MancalaGame;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GameRuleDto {

    private MancalaGame game;
    private Integer requestPitIndex;
    private Integer currentPitIndex;
    
}