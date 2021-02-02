package com.github.ekiauhce.orthoepybot.repositories;

import com.github.ekiauhce.orthoepybot.entities.Word;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WordRepositoryTest {

    @Autowired
    WordRepository wordRepository;

    @BeforeAll
    static void setUp(@Autowired WordRepository wr) {
        for (int i = 10; i < 30; i++) {
            Word word = wr.findById(i).orElseThrow();
            word.setTotalWrongAnswers(i);
            wr.save(word);
        }
    }

    @Test
    void getRandom_allWordsContainsRandom_True() {
        List<Word> words = wordRepository.findAll();
        assertThat(words).contains(wordRepository.getRandom());
    }

    @Test
    void findTop10ByOrderByTotalWrongAnswersDesc_sizeIs10_True() {
        List<Word> words = wordRepository.findTop10ByOrderByTotalWrongAnswersDesc();
        assertThat(words.size()).isEqualTo(10);
    }

    @Test
    void findTop10ByOrderByTotalWrongAnswersDesc_firstTotalWrongAnswersIs29_True() {
        List<Word> words = wordRepository.findTop10ByOrderByTotalWrongAnswersDesc();
        assertThat(words.get(0).getId()).isEqualTo(29);
    }

    @Test
    void findTop10ByOrderByTotalWrongAnswersDesc_lastTotalWrongAnswersIs20_True() {
        List<Word> words = wordRepository.findTop10ByOrderByTotalWrongAnswersDesc();
        assertThat(words.get(words.size() - 1).getId()).isEqualTo(20);
    }
}