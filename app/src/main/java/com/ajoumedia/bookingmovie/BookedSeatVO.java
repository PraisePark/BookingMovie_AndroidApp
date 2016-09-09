package com.ajoumedia.bookingmovie;

import java.io.Serializable;

/**
 * SQLite Database를 편리하게 구현하기 위한 VO객체
 * bookedseat 테이블
 */
public class BookedSeatVO implements Serializable {
    
    int bs_seq;
    String bs_showdate, bs_theater, bs_screen, bs_inning, bs_seatrow, bs_seatcol, bs_booknumber;

    public int getBs_seq() {
        return bs_seq;
    }

    public void setBs_seq(int bs_seq) {
        this.bs_seq = bs_seq;
    }

    public String getBs_showdate() {
        return bs_showdate;
    }

    public void setBs_showdate(String bs_showdate) {
        this.bs_showdate = bs_showdate;
    }

    public String getBs_theater() {
        return bs_theater;
    }

    public void setBs_theater(String bs_theater) {
        this.bs_theater = bs_theater;
    }

    public String getBs_screen() {
        return bs_screen;
    }

    public void setBs_screen(String bs_screen) {
        this.bs_screen = bs_screen;
    }

    public String getBs_inning() {
        return bs_inning;
    }

    public void setBs_inning(String bs_inning) {
        this.bs_inning = bs_inning;
    }

    public String getBs_seatrow() {
        return bs_seatrow;
    }

    public void setBs_seatrow(String bs_seatrow) {
        this.bs_seatrow = bs_seatrow;
    }

    public String getBs_seatcol() {
        return bs_seatcol;
    }

    public void setBs_seatcol(String bs_seatcol) {
        this.bs_seatcol = bs_seatcol;
    }

    public String getBs_booknumber() {
        return bs_booknumber;
    }

    public void setBs_booknumber(String bs_booknumber) {
        this.bs_booknumber = bs_booknumber;
    }
}
