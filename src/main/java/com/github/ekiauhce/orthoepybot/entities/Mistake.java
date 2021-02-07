package com.github.ekiauhce.orthoepybot.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(
        name = "mistake",
        uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "word_id"})
)
public class Mistake {
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "word_id")
    private Word word;

    @Column(name = "number", nullable = false)
    private Integer number = 0;

    public Mistake(Player player, Word word) {
        this.player = player;
        this.word = word;
    }

    public Mistake(Player player, Word word, Integer number) {
        this.player = player;
        this.word = word;
        this.number = number;
    }

    /**
     * String representation for /mistakes command
     * @see com.github.ekiauhce.orthoepybot.utils.Utils#getTable(List, Function)
     */
    @Transient
    public String forMyMistakes() {
        return word.getCorrect() +
                " [ <b>" + number + "</b> ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mistake mistake = (Mistake) o;
        return Objects.equals(player, mistake.player) && Objects.equals(word, mistake.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, word);
    }
}