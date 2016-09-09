package com.ajoumedia.bookingmovie;

import java.io.Serializable;

/**
 * SQLite Database를 편리하게 구현하기 위한 VO객체
 * ticket 테이블
 */
public class TicketVO implements Serializable {

    int t_seq;
    String t_showdate, t_theater, t_screen, t_inning, t_seatstr, t_booknumber, t_review;

    public int getT_seq() {
        return t_seq;
    }

    public void setT_seq(int t_seq) {
        this.t_seq = t_seq;
    }

    public String getT_showdate() {
        return t_showdate;
    }

    public void setT_showdate(String t_showdate) {
        this.t_showdate = t_showdate;
    }

    public String getT_theater() {
        return t_theater;
    }

    public void setT_theater(String t_theater) {
        this.t_theater = t_theater;
    }

    public String getT_screen() {
        return t_screen;
    }

    public void setT_screen(String t_screen) {
        this.t_screen = t_screen;
    }

    public String getT_inning() {
        return t_inning;
    }

    public void setT_inning(String t_inning) {
        this.t_inning = t_inning;
    }

    public String getT_seatstr() {
        return t_seatstr;
    }

    public void setT_seatstr(String t_seatstr) {
        this.t_seatstr = t_seatstr;
    }

    public String getT_booknumber() {
        return t_booknumber;
    }

    public void setT_booknumber(String t_booknumber) {
        this.t_booknumber = t_booknumber;
    }

    public String getT_review() {
        return t_review;
    }

    public void setT_review(String t_review) {
        this.t_review = t_review;
    }
}
