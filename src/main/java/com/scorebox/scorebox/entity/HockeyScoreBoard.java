package com.scorebox.scorebox.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "hockey")
@Data
@NoArgsConstructor
public class HockeyScoreBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String homeName;

    @Column
    private String awayName;

    @Column
    private int homeScore;

    @Column
    private int awayScore;

    @Column
    private int currentTime;

    @Column
    private int maxTime;

    @Column
    private int period;


//  ----------homePenalty---------------
//    @Column
//    private String homePenaltyNumber1;
//    private String homePenaltyNumber2;
//    private String homePenaltyNumber3;
//
//    @Column
//    private int homePenaltyTime1;
//    private int homePenaltyTime2;
//    private int homePenaltyTime3;

//  ----------awayPenalty---------------
//    @Column
//    private String awayPenaltyNumber1;
//    private String awayPenaltyNumber2;
//    private String awayPenaltyNumber3;
//
//    @Column
//    private int awayPenaltyTime1;
//    private int awayPenaltyTime2;
//    private int awayPenaltyTime3;

    public HockeyScoreBoard(String homeName, String awayName, int currentTime, int maxTime) {
        this.homeName = homeName;
        this.awayName = awayName;
        this.currentTime = currentTime;
        this.maxTime = maxTime;
        period = 1;
    }
}
