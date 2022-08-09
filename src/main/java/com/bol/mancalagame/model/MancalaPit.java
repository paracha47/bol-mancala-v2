package com.bol.mancalagame.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MancalaPit implements Serializable{

	private Integer id;
	private Integer stones;

	@JsonIgnore
	public Boolean isEmpty() {
		return this.stones == 0;
	}

}
