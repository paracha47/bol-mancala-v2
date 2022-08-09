package com.bol.mancalagame.ruleengine;

import com.bol.mancalagame.dto.GameRuleDto;

public interface GameRuleEngine {

    void processGameRules(GameRuleDto gameRuleDto);
    
}