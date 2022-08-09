package com.bol.mancalagame.mapper;

import org.modelmapper.ModelMapper;

import com.bol.mancalagame.dto.MancalaGameDto;
import com.bol.mancalagame.model.MancalaGame;

public class MancalaGameMapper {

	private static ModelMapper modelMapper ;
	
	private MancalaGameMapper() {}
	
	public static ModelMapper getInstance() 
	{
		if(modelMapper == null) {
			synchronized (MancalaGameMapper.class){
				if(modelMapper == null) {
					modelMapper = new ModelMapper();
				}
			}
		}
		return modelMapper;
	}
	
	public static MancalaGameDto toDto(MancalaGame mancalaGame) {
		
		return MancalaGameMapper.getInstance().map(mancalaGame, MancalaGameDto.class);
	} 
}
