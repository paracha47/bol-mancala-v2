package com.bol.mancalagame.ruleengine;

import org.springframework.stereotype.Component;

import com.bol.mancalagame.dto.GameRuleDto;
import com.bol.mancalagame.rule.ChangePlayerRule;
import com.bol.mancalagame.rule.CheckLastStoneInEmptyPitRule;
import com.bol.mancalagame.rule.GameOverRule;

@Component
public class GameRuleEngineImpl implements GameRuleEngine {

	@Override
	public void processGameRules(GameRuleDto gameRuleDto) {

		new CheckLastStoneInEmptyPitRule().process(gameRuleDto);
		new GameOverRule().process(gameRuleDto);
		new ChangePlayerRule().process(gameRuleDto);
	}

}
