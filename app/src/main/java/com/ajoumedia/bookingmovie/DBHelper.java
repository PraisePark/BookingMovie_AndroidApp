package com.ajoumedia.bookingmovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * SQLite Database - SQLiteOpenHelper
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookingMovieDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BOOKEDSEAT = "bookedseat"; // 테이블명 bookedseat
    private static final String KEY_BS_SEQ = "bs_seq"; // bookedseat 테이블 Primary Key
    private static final String KEY_BS_SHOWDATE = "bs_showdate"; // 상영일 ex.2015-06-06
    private static final String KEY_BS_THEATER = "bs_theater"; // 영화관명
    private static final String KEY_BS_SCREEN = "bs_screen"; // 1~10관
    private static final String KEY_BS_INNING = "bs_inning"; // 회차
    private static final String KEY_BS_SEATROW = "bs_seatrow"; // ABC...J
    private static final String KEY_BS_SEATCOL = "bs_seatcol"; // 123...10
    private static final String KEY_BS_BOOKNUMBER = "bs_booknumber"; // 예매번호

    private static final String TABLE_TICKET = "ticket"; // 테이블명 ticket
    private static final String KEY_T_SEQ = "t_seq"; // ticket 테이블 Primary Key
    private static final String KEY_T_SHOWDATE = "t_showdate"; // 상영일 ex.2015-06-06
    private static final String KEY_T_THEATER = "t_theater"; // 영화관명
    private static final String KEY_T_SCREEN = "t_screen"; // 1~10관
    private static final String KEY_T_INNING = "t_inning"; // 회차
    private static final String KEY_T_SEATSTR = "t_seatstr"; // 좌석번호 A1, A2, A3...
    private static final String KEY_T_BOOKNUMBER = "t_booknumber"; // 예매번호
    private static final String KEY_T_REVIEW = "t_review"; // 영화 다이어리 - 리뷰, 감상평


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL("CREATE TABLE " + TABLE_BOOKEDSEAT + "("
                + KEY_BS_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BS_SHOWDATE + " TEXT NOT NULL, "
                + KEY_BS_THEATER + " TEXT NOT NULL, "
                + KEY_BS_SCREEN + " TEXT NOT NULL, "
                + KEY_BS_INNING + " TEXT NOT NULL, "
                + KEY_BS_SEATROW + " TEXT NOT NULL, "
                + KEY_BS_SEATCOL + " TEXT NOT NULL, "
                + KEY_BS_BOOKNUMBER + " TEXT NOT NULL)");
        db.execSQL("CREATE TABLE " + TABLE_TICKET + "("
                + KEY_T_SEQ + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_T_SHOWDATE + " TEXT NOT NULL, "
                + KEY_T_THEATER + " TEXT NOT NULL, "
                + KEY_T_SCREEN + " TEXT NOT NULL, "
                + KEY_T_INNING + " TEXT NOT NULL, "
                + KEY_T_SEATSTR + " TEXT NOT NULL, "
                + KEY_T_BOOKNUMBER + " TEXT NOT NULL, "
                + KEY_T_REVIEW + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKEDSEAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        onCreate(db);
    }

    // BookedSeat 테이블 insert
    public void insertBookedSeat(BookedSeatVO vo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BS_SHOWDATE, vo.getBs_showdate());
        values.put(KEY_BS_THEATER, vo.getBs_theater());
        values.put(KEY_BS_SCREEN, vo.getBs_screen());
        values.put(KEY_BS_INNING, vo.getBs_inning());
        values.put(KEY_BS_SEATROW, vo.getBs_seatrow());
        values.put(KEY_BS_SEATCOL, vo.getBs_seatcol());
        values.put(KEY_BS_BOOKNUMBER, vo.getBs_booknumber());

        db.insert(TABLE_BOOKEDSEAT, null, values);
        db.close();
    }

    // BookedSeat 테이블 - 잔여좌석 반환하는 메서드
    public int getCountOfRemainderSeat(BookedSeatVO vo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + TABLE_BOOKEDSEAT
                + " WHERE " + KEY_BS_SHOWDATE + "='" + vo.getBs_showdate() + "' AND "
                + KEY_BS_THEATER + "='" + vo.getBs_theater() + "' AND "
                + KEY_BS_SCREEN + "='" + vo.getBs_screen() + "' AND "
                + KEY_BS_INNING + "='" + vo.getBs_inning() + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int remainder = 100 - cursor.getCount();
        cursor.close();

        return remainder;
    }

    // 해당 좌석이 예매되었는지 확인하는 메서드
    public boolean checkIsBookedSeat(BookedSeatVO vo) {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + TABLE_BOOKEDSEAT
                + " WHERE " + KEY_BS_SHOWDATE + "='" + vo.getBs_showdate() + "' AND "
                + KEY_BS_THEATER + "='" + vo.getBs_theater() + "' AND "
                + KEY_BS_SCREEN + "='" + vo.getBs_screen() + "' AND "
                + KEY_BS_INNING + "='" + vo.getBs_inning() + "' AND "
                + KEY_BS_SEATROW + "='" + vo.getBs_seatrow() + "' AND "
                + KEY_BS_SEATCOL + "='" + vo.getBs_seatcol() + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    // BookedSeat 테이블 전체 조회
    public ArrayList<BookedSeatVO> getAllBookedSeatList() {
        ArrayList<BookedSeatVO> bookedSeatList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BOOKEDSEAT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                BookedSeatVO vo = new BookedSeatVO();
                vo.setBs_seq(Integer.parseInt(cursor.getString(0)));
                vo.setBs_showdate(cursor.getString(1));
                vo.setBs_theater(cursor.getString(2));
                vo.setBs_screen(cursor.getString(3));
                vo.setBs_inning(cursor.getString(4));
                vo.setBs_seatrow(cursor.getString(5));
                vo.setBs_seatcol(cursor.getString(6));
                vo.setBs_booknumber(cursor.getString(7));
                bookedSeatList.add(vo);
            } while (cursor.moveToNext());
        }
        return bookedSeatList;
    }

    // Ticket 테이블 insert
    public void insertTicket(TicketVO vo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_T_SHOWDATE, vo.getT_showdate());
        values.put(KEY_T_THEATER, vo.getT_theater());
        values.put(KEY_T_SCREEN, vo.getT_screen());
        values.put(KEY_T_INNING, vo.getT_inning());
        values.put(KEY_T_SEATSTR, vo.getT_seatstr());
        values.put(KEY_T_BOOKNUMBER, vo.getT_booknumber());

        db.insert(TABLE_TICKET, null, values);
        db.close();
    }

    // Ticket 테이블 영화 다이어리 작성 및 수정하는 메서드
    public String updateReview(TicketVO vo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_T_REVIEW, vo.getT_review());

        db.update(TABLE_TICKET, values, KEY_T_BOOKNUMBER + " = ?", new String[]{vo.getT_booknumber()});
        db.close();

        return vo.getT_review();
    }

    // Ticket 테이블 전체 조회
    public ArrayList<TicketVO> getAllTicketList() {
        ArrayList<TicketVO> ticketList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TICKET + " ORDER BY " + KEY_T_SEQ + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                TicketVO vo = new TicketVO();
                vo.setT_seq(Integer.parseInt(cursor.getString(0)));
                vo.setT_showdate(cursor.getString(1));
                vo.setT_theater(cursor.getString(2));
                vo.setT_screen(cursor.getString(3));
                vo.setT_inning(cursor.getString(4));
                vo.setT_seatstr(cursor.getString(5));
                vo.setT_booknumber(cursor.getString(6));
                vo.setT_review(cursor.getString(7));
                ticketList.add(vo);
            } while (cursor.moveToNext());
        }
        return ticketList;
    }

    // 예매 취소 시 - BookedSeat, Ticket 두개의 테이블 동시에 delete
    public void deleteBookedSeatAndTicket(TicketVO vo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TICKET, KEY_T_BOOKNUMBER + " = ?", new String[] { vo.getT_booknumber() });
        db.delete(TABLE_BOOKEDSEAT, KEY_BS_BOOKNUMBER + " = ?", new String[] { vo.getT_booknumber() });
        db.close();
    }
}
