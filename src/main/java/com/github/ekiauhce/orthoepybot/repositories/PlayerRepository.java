package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findTop10ByOrderByHighScoreDesc();
    List<Player> findAllByOrderByHighScoreDesc();
}
