package com.github.ekiauhce.orthoepybot.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "is_practicing", nullable = false)
    private Boolean isPracticing = false;

    @Column(name = "score", nullable = false)
    private Integer score = 0;

    @Column(name = "high_score", nullable = false)
    private Integer highScore = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "word_id")
    private Word word;

    public Player(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.userName = user.getUserName();
    }

    public Player(Integer id, String firstName, String userName) {
        this.id = id;
        this.firstName = firstName;
        this.userName = userName;
    }

    /**
     * String representation for /leaderboard command
     * @see com.github.ekiauhce.orthoepybot.utils.Utils#getTable(List, Function)
     */
    @Transient
    public String forLeaderboard() {
        return firstName + " " +
                (userName == null ? "" : "(@" + userName + ") ") +
                "[ <b>" + highScore + "</b> ]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player otherPlayer = (Player) o;
        return Objects.equals(id, otherPlayer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}