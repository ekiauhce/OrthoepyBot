package com.github.ekiauhce.orthoepybot.services;


import com.github.ekiauhce.orthoepybot.entities.Mistake;
import com.github.ekiauhce.orthoepybot.entities.Player;
import com.github.ekiauhce.orthoepybot.entities.Word;
import com.github.ekiauhce.orthoepybot.repositories.MistakeRepository;
import com.github.ekiauhce.orthoepybot.repositories.PlayerRepository;
import com.github.ekiauhce.orthoepybot.repositories.WordRepository;
import com.github.ekiauhce.orthoepybot.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Service
public class OrthoepyServiceImpl implements OrthoepyService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private MistakeRepository mistakeRepository;

    @Override
    public Player getPlayer(User user) {
        return playerRepository.findById(user.getId())
                .orElse(new Player(user));
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public Boolean getIsPracticing(Player player) {
        return player.getIsPracticing();
    }

    @Override
    public void setIsPracticingTrue(Player player) {
        player.setIsPracticing(true);
    }

    @Override
    public void setIsPracticingFalse(Player player) {
        player.setIsPracticing(false);
    }

    @Override
    public void setRandomWord(Player player) {
        player.setWord(wordRepository.getRandom());
    }

    @Override
    public List<String> getShuffledWordForms(Player player) {
        Word word = player.getWord();
        List<String> wordForms = Arrays.asList(word.getCorrect(), word.getWrong());
        Collections.shuffle(wordForms);
        return wordForms;
    }

    @Override
    public String getCorrectForm(Player player) {
        return player.getWord().getCorrect();
    }

    @Override
    public String getWrongForm(Player player) {
        return player.getWord().getWrong();
    }

    @Override
    public Integer getScore(Player player) {
        return player.getScore();
    }

    @Override
    public void increaseScore(Player player) {
        player.setScore(player.getScore() + 1);
    }

    @Override
    public void resetScore(Player player) {
        player.setScore(0);
    }

    @Override
    public void setNewHighScore(Player player) {
        player.setHighScore(player.getScore());
    }

    @Override
    public Boolean isNewHighScore(Player player) {
        return player.getScore().compareTo(player.getHighScore()) > 0;
    }

    /**
     * @see PlayerRepository#findTop10ByOrderByHighScoreDesc()
     * @see Utils#getTable(List, Function)
     */
    @Override
    public String getLeaderboardText() {
        List<Player> leaderboard = playerRepository.findTop10ByOrderByHighScoreDesc();
        return Utils.getTable(leaderboard, Player::forLeaderboard);
    }


    /**
     * May return -1, if /start or /practice has never been called by user
     * @see PlayerRepository#findAllByOrderByHighScoreDesc()
     */
    @Override
    public Integer getIndexInRating(Player player) {
        List<Player> rating = playerRepository.findAllByOrderByHighScoreDesc();
        return rating.indexOf(player);
    }

    @Override
    public void increaseMistakeNumber(Player player) {
        Mistake mistake = mistakeRepository
                .findByPlayerIdAndWordId(player.getId(), player.getWord().getId())
                .orElse(new Mistake(player, player.getWord()));
        mistake.setNumber(mistake.getNumber() + 1);
        mistakeRepository.save(mistake);
    }

    @Override
    public void increaseWordTotalWrongAnswers(Player player) {
        Word word = player.getWord();
        word.setTotalWrongAnswers(word.getTotalWrongAnswers() + 1);
        wordRepository.save(word);
    }


    /**
     * @see MistakeRepository#findTop10ByPlayerIdOrderByNumberDesc(Integer)
     * @see Utils#getTable(List, Function)
     */
    @Override
    public String getMyMistakesText(Player player) {
        List<Mistake> mistakes = mistakeRepository.findTop10ByPlayerIdOrderByNumberDesc(player.getId());
        return Utils.getTable(mistakes, Mistake::forMyMistakes);
    }

    /**
     * @see WordRepository#findTop10ByOrderByTotalWrongAnswersDesc()
     * @see Utils#getTable(List, Function)
     */
    @Override
    public String getHardestWordsText() {
        List<Word> words = wordRepository.findTop10ByOrderByTotalWrongAnswersDesc();
        return Utils.getTable(words, Word::forHardest);
    }

    @Override
    public void beginPractice(Player player) {
        setIsPracticingTrue(player);
        setRandomWord(player);
    }

    @Override
    public void endPractice(Player player) {
        setIsPracticingFalse(player);
        increaseMistakeNumber(player);
        increaseWordTotalWrongAnswers(player);
    }

    @Override
    public void summarizeScore(Player player) {
        if (isNewHighScore(player)) {
            setNewHighScore(player);
        }
        resetScore(player);
    }

    @Override
    public Long getPlayersNumber() {
        return playerRepository.count();
    }

    @Override
    public Integer getTotalMistakesNumber() {
        return mistakeRepository.findAll().stream()
                .mapToInt(Mistake::getNumber)
                .sum();
    }
}
