package com.bol.mancalagame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WinnerDto {
	
	private String playerName;
	private Integer score;
	private String message;
	
	
}
