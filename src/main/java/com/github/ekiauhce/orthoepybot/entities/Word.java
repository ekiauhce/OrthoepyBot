package com.github.ekiauhce.orthoepybot.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "correct")
    private String correct;

    @Column(name = "wrong")
    private String wrong;

    @Column(name = "total_wrong_answers")
    @ColumnDefault("0")
    private Integer totalWrongAnswers;

    /**
     * String representation for /hardest command
     * @see com.github.ekiauhce.orthoepybot.utils.Utils#getTable(List, Function) 
     */
    @Transient
    public String forHardest() {
        return correct + " " +
                "[ <b>" + totalWrongAnswers + "</b> ]";
    }
}
