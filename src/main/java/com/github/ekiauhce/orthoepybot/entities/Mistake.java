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
@Table(name = "mistake")
public class Mistake {
    @EmbeddedId
    private MistakeId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("playerId")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("wordId")
    private Word word;

    @Column(name = "number", nullable = false)
    private Integer number = 0;

    public Mistake(Player player, Word word) {
        this.id = new MistakeId(player.getId(), word.getId());
        this.player = player;
        this.word = word;
    }

    public Mistake(Player player, Word word, Integer number) {
        this.id = new MistakeId(player.getId(), word.getId());
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
                " \\[ *" + number + "* ]";
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