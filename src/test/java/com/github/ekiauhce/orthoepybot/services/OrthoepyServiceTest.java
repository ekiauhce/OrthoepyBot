package com.github.ekiauhce.orthoepybot.services;

import com.github.ekiauhce.orthoepybot.entities.Mistake;
import com.github.ekiauhce.orthoepybot.entities.Player;
import com.github.ekiauhce.orthoepybot.entities.Word;
import com.github.ekiauhce.orthoepybot.repositories.MistakeRepository;
import com.github.ekiauhce.orthoepybot.repositories.PlayerRepository;
import com.github.ekiauhce.orthoepybot.repositories.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrthoepyServiceTest {
    @Mock
    PlayerRepository playerRepository;

    @Mock
    WordRepository wordRepository;

    @Mock
    MistakeRepository mistakeRepository;

    @InjectMocks
    OrthoepyService orthoepyService =  new OrthoepyServiceImpl();

    Player player;
    Word word;
    List<Player> leaderboard;
    Mistake mistake;
    List<Mistake> myMistakes;
    List<Word> hardestWords;

    @BeforeEach
    void setUp() {
        player = new Player(11, "Vasya", "vasya11");
        player.setScore(100);

        Player p1 = new Player(15, "Dima", "dddd15");
        p1.setHighScore(50);
        Player p2 = new Player(16, "Kolya", null);
        p2.setHighScore(67);
        Player p3 = new Player(17, "Egor", "eee17");
        p3.setHighScore(80);
        leaderboard = Arrays.asList(player, p3, p2, p1);

        word = new Word(10, "example_correct", "example_wrong", 20);
        Word w1 = new Word(14, "correct14", "wrong14", 4);
        Word w2 = new Word(15, "correct14", "wrong14", 8);
        hardestWords = Arrays.asList(word, w2, w1);

        mistake = new Mistake(player, word);
        mistake.setNumber(16);

        Mistake m1 = new Mistake(player, w1);
        m1.setNumber(3);

        Mistake m2 = new Mistake(player, w2);
        m2.setNumber(4);
        myMistakes = Arrays.asList(mistake, m2, m1);
    }

    @Test
    void getPlayer_returnsNewWhenPlayerDoesNotExists_True() {
        Mockito.when(playerRepository.findById(10)).thenReturn(Optional.empty());

        User user = new User(10, "Andrey", false);
        assertThat(orthoepyService.getPlayer(user)).isEqualTo(new Player(user));

        verify(playerRepository, times(1)).findById(10);
    }

    @Test
    void getPlayer_returnsActualWhenPlayerExistsInDatabase_True() {
        Mockito.when(playerRepository.findById(11)).thenReturn(Optional.of(player));

        // User may change first name in telegram
        User user = new User(11, "Vasiliy", false);

        assertThat(orthoepyService.getPlayer(user)).isEqualTo(player);

        verify(playerRepository, times(1)).findById(11);
    }

    @Test
    void getIsPracticing_returnsActualIsPracticingValue_True() {
        assertThat(orthoepyService.getIsPracticing(player)).isEqualTo(player.getIsPracticing());
    }

    @Test
    void setIsPracticingTrue_valueChanged_True() {
        assertThat(player.getIsPracticing()).isEqualTo(false);

        orthoepyService.setIsPracticingTrue(player);

        assertThat(player.getIsPracticing()).isEqualTo(true);
    }

    @Test
    void setIsPracticingFalse_valueChanged_True() {
        player.setIsPracticing(true);

        orthoepyService.setIsPracticingFalse(player);

        assertThat(player.getIsPracticing()).isEqualTo(false);
    }

    @Test
    void setRandomWord_valueChanged_True() {
        Mockito.when(wordRepository.getRandom()).thenReturn(word);

        assertThat(player.getWord()).isNull();

        orthoepyService.setRandomWord(player);

        assertThat(player.getWord()).isEqualTo(word);

        verify(wordRepository, times(1)).getRandom();
    }

    @Test
    void getShuffledWordForms_ReturnsListOfShuffledNames_True() {
        player.setWord(word);

        List<String> wordForms = orthoepyService.getShuffledWordForms(player);

        assertThat(wordForms)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(word.getCorrect(), word.getWrong()));
    }

    @Test
    void getCorrectForm_ReturnsValueOfCorrect_True() {
        player.setWord(word);

        assertThat(player.getWord().getCorrect()).isEqualTo("example_correct");

        assertThat(orthoepyService.getCorrectForm(player)).isEqualTo("example_correct");
    }

    @Test
    void getWrongForm_ReturnsValueOfWrong_True() {
        player.setWord(word);

        assertThat(player.getWord().getWrong()).isEqualTo("example_wrong");

        assertThat(orthoepyService.getWrongForm(player)).isEqualTo("example_wrong");
    }

    @Test
    void getScore_ReturnsValueOfScore_True() {
        assertThat(player.getScore()).isEqualTo(100);

        assertThat(orthoepyService.getScore(player)).isEqualTo(100);
    }

    @Test
    void increaseScore_ScoreIncreasedByOne_True() {
        Integer score = player.getScore();

        orthoepyService.increaseScore(player);

        assertThat(player.getScore().equals(score + 1)).isEqualTo(true);
    }

    @Test
    void resetScore_ScoreChangedToZero_True() {
        assertThat(player.getScore()).isEqualTo(100);

        orthoepyService.resetScore(player);

        assertThat(player.getScore().equals(0)).isEqualTo(true);
    }

    @Test
    void setNewHighScore_changedToScoreValue_True() {
        int score = player.getScore();

        assertThat(player.getHighScore()).isNotEqualTo(score);

        orthoepyService.setNewHighScore(player);

        assertThat(player.getHighScore().equals(score)).isEqualTo(true);
    }

    @Test
    void isNewHighScore_ScoreBiggerThanHighScore_True() {
        int score = player.getScore();
        int higScore = player.getHighScore();

        assertThat(score > higScore).isEqualTo(true);

        assertThat(orthoepyService.isNewHighScore(player)).isEqualTo(true);
    }

    @Test
    void isNewHighScore_ScoreLowerThanHighScore_False() {
        player.setHighScore(345);

        int score = player.getScore();
        int higScore = player.getHighScore();

        assertThat(score < higScore).isEqualTo(true);

        assertThat(orthoepyService.isNewHighScore(player)).isEqualTo(false);
    }

    @Test
    void isNewHighScore_ScoreEqualsHighScore_False() {
        player.setHighScore(100);

        int score = player.getScore();
        int higScore = player.getHighScore();

        assertThat(score == higScore).isEqualTo(true);

        assertThat(orthoepyService.isNewHighScore(player)).isEqualTo(false);
    }

    @Test
    void getLeaderboardText_isBlankForNonEmptyRepo_False() {
        Mockito.when(playerRepository.findTop10ByOrderByHighScoreDesc()).thenReturn(leaderboard);

        assertThat(orthoepyService.getLeaderboardText().isBlank()).isEqualTo(false);

        verify(playerRepository, times(1)).findTop10ByOrderByHighScoreDesc();
    }

    @Test
    void getLeaderboardText_isBlankForEmptyRepo_True() {
        Mockito.when(playerRepository.findTop10ByOrderByHighScoreDesc()).thenReturn(new ArrayList<>());

        assertThat(orthoepyService.getLeaderboardText().isBlank()).isEqualTo(true);

        verify(playerRepository, times(1)).findTop10ByOrderByHighScoreDesc();
    }

    @Test
    void getIndexInRating_isMinusOneForNonEmptyRepo_False() {
        Mockito.when(playerRepository.findAllByOrderByHighScoreDesc()).thenReturn(leaderboard);

        assertThat(orthoepyService.getIndexInRating(player).equals(-1)).isEqualTo(false);

        verify(playerRepository, times(1)).findAllByOrderByHighScoreDesc();
    }

    @Test
    void getIndexInRating_isMinusOneForEmptyRepo_True() {
        Mockito.when(playerRepository.findAllByOrderByHighScoreDesc()).thenReturn(new ArrayList<>());

        assertThat(orthoepyService.getIndexInRating(player)).isEqualTo(-1);

        verify(playerRepository, times(1)).findAllByOrderByHighScoreDesc();
    }

    @Test
    void increaseMistakeNumber_MistakeNumberIncreasedByOne_True() {
        int number = mistake.getNumber();

        Mockito.when(mistakeRepository.findByPlayerIdAndWordId(player.getId(), word.getId()))
                .thenReturn(Optional.of(mistake));

        player.setWord(word);
        orthoepyService.increaseMistakeNumber(player);

        assertThat(mistake.getNumber().equals(number + 1)).isEqualTo(true);

        verify(mistakeRepository, times(1)).findByPlayerIdAndWordId(player.getId(), word.getId());
    }

    @Test
    void increaseWordTotalWrongAnswers_totalWrongAnswersIncreasedByOne_True() {
        int totalWrongAnswers = word.getTotalWrongAnswers();
        player.setWord(word);

        orthoepyService.increaseWordTotalWrongAnswers(player);

        assertThat(word.getTotalWrongAnswers().equals(totalWrongAnswers + 1)).isEqualTo(true);
    }

    @Test
    void getMyMistakesText_isBlankForNonEmptyRepo_False() {
        Mockito.when(mistakeRepository.findTop10ByPlayerIdOrderByNumberDesc(player.getId()))
                .thenReturn(myMistakes);

        assertThat(orthoepyService.getMyMistakesText(player).isBlank()).isEqualTo(false);

        verify(mistakeRepository, times(1)).findTop10ByPlayerIdOrderByNumberDesc(player.getId());
    }

    @Test
    void getMyMistakesText_isBlankForEmptyRepo_True() {
        Mockito.when(mistakeRepository.findTop10ByPlayerIdOrderByNumberDesc(player.getId()))
                .thenReturn(new ArrayList<>());

        assertThat(orthoepyService.getMyMistakesText(player).isBlank()).isEqualTo(true);

        verify(mistakeRepository, times(1)).findTop10ByPlayerIdOrderByNumberDesc(player.getId());
    }

    @Test
    void getHardestWordsText_isBlankForNonEmptyRepo_False() {
        Mockito.when(wordRepository.findTop10ByOrderByTotalWrongAnswersDesc()).thenReturn(hardestWords);
        assertThat(orthoepyService.getHardestWordsText().isBlank()).isEqualTo(false);

        verify(wordRepository, times(1)).findTop10ByOrderByTotalWrongAnswersDesc();
    }

    @Test
    void getHardestWordsText_isBlankForEmptyRepo_True() {
        Mockito.when(wordRepository.findTop10ByOrderByTotalWrongAnswersDesc()).thenReturn(new ArrayList<>());
        assertThat(orthoepyService.getHardestWordsText().isBlank()).isEqualTo(true);

        verify(wordRepository, times(1)).findTop10ByOrderByTotalWrongAnswersDesc();
    }
}