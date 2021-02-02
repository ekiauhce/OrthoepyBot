package com.github.ekiauhce.orthoepybot.services;

import com.github.ekiauhce.orthoepybot.entities.Player;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface OrthoepyService {

    /**
     * Entry point for OrthoepyService consumers.
     * If Player with id = user.id exists in database, returns Player entity from database.
     * Otherwise, returns new Player based on user
     * @see User
     * @see Player
     * @param user User object from telegrambots lib
     * @return Player entity
     */
    Player getPlayer(User user);

    /**
     * Saves player to database
     * @see Player
     * @param player Player entity
     */
    void savePlayer(Player player);

    /**
     * Returns player's practice state
     * @see Player
     * @param player Player entity
     */
    Boolean getIsPracticing(Player player);

    /**
     * Changes player's practice state to true
     * @see Player
     * @param player Player entity
     */
    void setIsPracticingTrue(Player player);

    /**
     * Changes player's practice state to false
     * @see Player
     * @param player Player entity
     */
    void setIsPracticingFalse(Player player);

    /**
     * Sets random word to player
     * @see Player
     * @param player Player entity
     */
    void setRandomWord(Player player);

    /**
     * Returns shuffled list consisting of two strings: correct and wrong forms of word
     * @see Player
     * @param player Player entity
     */
    List<String> getShuffledWordForms(Player player);

    /**
     * Returns correct form of player's actual word
     * @see Player
     * @param player Player entity
     */
    String getCorrectForm(Player player);

    /**
     * Returns wrong form of player's actual word
     * @see Player
     * @param player Player entity
     */
    String getWrongForm(Player player);

    /**
     * Returns player's score
     * @see Player
     * @param player Player entity
     */
    Integer getScore(Player player);

    /**
     * Increases player's score by one
     * @see Player
     * @param player Player entity
     */
    void increaseScore(Player player);

    /**
     * Sets player's score to zero
     * @see Player
     * @param player Player entity
     */
    void resetScore(Player player);

    /**
     * Writes player's score to high score
     * @see Player
     * @param player Player entity
     */
    void setNewHighScore(Player player);


    /**
     * Returns is player's score bigger than high score
     * @see Player
     * @param player Player
     */
    Boolean isNewHighScore(Player player);

    /**
     * Returns table of leaders in practice mode
     */
    String getLeaderboardText();

    /**
     * Returns index of player in global rating for practice mode
     * @see Player
     * @param player Player entity
     */
    Integer getIndexInRating(Player player);

    /**
     * Increases number of player's mistakes in specific word
     * @see Player
     * @param player Player entity
     */
    void increaseMistakeNumber(Player player);

    /**
     * Increases number of wrong answers of all players for specific word
     * @see Player
     * @param player Player entity
     */
    void increaseWordTotalWrongAnswers(Player player);


    /**
     * Returns table of mistakes for specific player
     * @see Player
     * @param player Player entity
     */
    String getMyMistakesText(Player player);

    /**
     * Returns table of hardest words
     */
    String getHardestWordsText();

    /**
     * Short cut for
     * {@link #setIsPracticingTrue(Player)} and
     * {@link #setRandomWord(Player)}
     */
    void beginPractice(Player player);

    /**
     * Short cut for
     * {@link #setIsPracticingFalse(Player)},
     * {@link #increaseMistakeNumber(Player)}
     * and {@link #increaseWordTotalWrongAnswers(Player)}
     */
    void endPractice(Player player);

    /**
     * Short cut for
     * if {@link #isNewHighScore(Player)}, than
     * {@link #setNewHighScore(Player)} and
     * {@link #resetScore(Player)}
     */
    void summarizeScore(Player player);
}
