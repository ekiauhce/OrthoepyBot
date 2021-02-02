package com.github.ekiauhce.orthoepybot.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class MistakeId implements Serializable {
    @Column(name = "player_id")
    private Integer playerId;

    @Column(name = "word_id")
    private Integer wordId;

    public MistakeId(Integer playerId, Integer wordId) {
        this.playerId = playerId;
        this.wordId = wordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MistakeId mistakeId = (MistakeId) o;
        return Objects.equals(playerId, mistakeId.playerId) && Objects.equals(wordId, mistakeId.wordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, wordId);
    }
}
