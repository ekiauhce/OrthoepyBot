package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Mistake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MistakeRepository extends JpaRepository<Mistake, Integer> {
    Optional<Mistake> findByPlayerIdAndWordId(Integer playerId, Integer wordId);
    List<Mistake> findTop10ByPlayerIdOrderByNumberDesc(Integer playerId);
}
