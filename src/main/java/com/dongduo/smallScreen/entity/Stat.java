package com.dongduo.smallScreen.entity;

import javax.persistence.*;

@Entity
@Table(name="stat")
public class Stat {
    @Id
    @Column(name = "STAT_DATE")
    private int statDate;

    @Column(name = "IN_COUNT")
    private int inCount;

    @Column(name = "OUT_COUNT")
    private int outCount;

    public Stat() {
    }

    public Stat(int statDate, int inCount, int outCount) {
        this.statDate = statDate;
        this.inCount = inCount;
        this.outCount = outCount;
    }

    public int getStatDate() {
        return statDate;
    }

    public void setStatDate(int statDate) {
        this.statDate = statDate;
    }

    public int getInCount() {
        return inCount;
    }

    public void setInCount(int inCount) {
        this.inCount = inCount;
    }

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }
}
