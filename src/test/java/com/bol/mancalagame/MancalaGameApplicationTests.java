package com.bol.mancalagame;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bol.mancalagame.controller.GameController;


@SpringBootTest
class MancalaGameApplicationTests {

	@Autowired
	GameController gameController;
	
	@Test
	void contextLoads() {
		assertThat(gameController).isNotNull();

	}

}
