package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;

    @BeforeAll
    static void setUp(@Autowired PlayerRepository pr) {
        List<Player> players = Arrays.asList(
                new Player(10, "Vasya", "vasya10").setHighScore(100),
                new Player(11, "Petya", null),
                new Player(12,"asd", "asddsa").setHighScore(13),
                new Player(13, "qwerty", "qwerty13").setHighScore(45),
                new Player(14, "Nikita", "nikita").setHighScore(3),
                new Player(15, "Andrey", "and444").setHighScore(111),
                new Player(16, "Gleb", "ggglll"),
                new Player(17, "jenya", "jna").setHighScore(66),
                new Player(18, "vova", "vvfvfv").setHighScore(80),
                new Player(19, "Nikita", "nnnn").setHighScore(3),
                new Player(20, "Masha", "mmmm").setHighScore(73),
                new Player(21, "Sasha", "sashok").setHighScore(3),
                new Player(22, "Vitya", "vitek").setHighScore(24),
                new Player(23, "Dasha", "kruto").setHighScore(47),
                new Player(24, "Привет", "world").setHighScore(15)
        );
        pr.saveAll(players);
    }


    @Test
    public void findTop10ByOrderByHighScoreDesc_sizeIs10_True() {
        List<Player> players = playerRepository.findTop10ByOrderByHighScoreDesc();
        assertThat(players.size()).isEqualTo(10);
    }

    @Test
    public void findTop10ByOrderByHighScoreDesc_firstHighScoreIs111_True() {
        List<Player> players = playerRepository.findTop10ByOrderByHighScoreDesc();
        assertThat(players.get(0).getHighScore()).isEqualTo(111);
    }

    @Test
    public void findTop10ByOrderByHighScoreDesc_lastHighScoreIs13_True() {
        List<Player> players = playerRepository.findTop10ByOrderByHighScoreDesc();
        assertThat(players.get(players.size() - 1).getHighScore()).isEqualTo(13);
    }

    @Test
    public void findAllByOrderByHighScoreDesc_sizeIs15_True() {
        List<Player> players = playerRepository.findAllByOrderByHighScoreDesc();
        assertThat(players.size()).isEqualTo(15);
    }

    @Test
    public void findAllByOrderByHighScoreDesc_firstHighScoreIs111_True() {
        List<Player> players = playerRepository.findAllByOrderByHighScoreDesc();
        assertThat(players.get(0).getHighScore()).isEqualTo(111);
    }

    @Test
    public void findAllByOrderByHighScoreDesc_lastHighScoreIs0_True() {
        List<Player> players = playerRepository.findAllByOrderByHighScoreDesc();
        assertThat(players.get(players.size() - 1).getHighScore()).isEqualTo(0);
    }
}