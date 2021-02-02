package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Mistake;
import com.github.ekiauhce.orthoepybot.entities.MistakeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MistakeRepository extends JpaRepository<Mistake, MistakeId> {
    List<Mistake> findTop10ByPlayerIdOrderByNumberDesc(Integer playerId);
}
