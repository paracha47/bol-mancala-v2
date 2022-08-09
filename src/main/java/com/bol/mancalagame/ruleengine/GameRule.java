package com.bol.mancalagame.ruleengine;

import com.bol.mancalagame.dto.GameRuleDto;

public interface GameRule {
    
	void process(GameRuleDto gameRuleDto);

}