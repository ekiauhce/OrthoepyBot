package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WordRepository extends JpaRepository<Word, Integer> {
    @Query(value = "SELECT * FROM word ORDER BY random() LIMIT 1", nativeQuery = true)
    Word getRandom();

    List<Word> findTop10ByOrderByTotalWrongAnswersDesc();
}
