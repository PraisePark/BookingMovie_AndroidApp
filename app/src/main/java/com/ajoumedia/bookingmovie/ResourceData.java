package com.ajoumedia.bookingmovie;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 영화, 영화관, 이미지 등의 resource data들을 받아오기 위해서 따로 class생성
 */
public class ResourceData {

    // 영화별 스틸 이미지 Drawable 배열
    public static Integer[] avengers_still =    {   R.drawable.avengers_youtube,
            R.drawable.avengers_still_01, R.drawable.avengers_still_02,
            R.drawable.avengers_still_03, R.drawable.avengers_still_04,
            R.drawable.avengers_still_05    };
    public static Integer[] home_still =        {   R.drawable.home_youtube,
            R.drawable.home_still_01, R.drawable.home_still_02,
            R.drawable.home_still_03, R.drawable.home_still_04,
            R.drawable.home_still_05        };
    public static Integer[] jangsoo_still =     {   R.drawable.jangsoo_youtube,
            R.drawable.jangsoo_still_01, R.drawable.jangsoo_still_02,
            R.drawable.jangsoo_still_03, R.drawable.jangsoo_still_04,
            R.drawable.jangsoo_still_05     };
    public static Integer[] chinatown_still =   {   R.drawable.chinatown_youtube,
            R.drawable.chinatown_still_01, R.drawable.chinatown_still_02,
            R.drawable.chinatown_still_03, R.drawable.chinatown_still_04,
            R.drawable.chinatown_still_05   };
    public static Integer[] kingsman_still =    {   R.drawable.kingsman_youtube,
            R.drawable.kingsman_still_01, R.drawable.kingsman_still_02,
            R.drawable.kingsman_still_03, R.drawable.kingsman_still_04,
            R.drawable.kingsman_still_05    };
    public static Integer[] dinotime_still =    {   R.drawable.dinotime_youtube,
            R.drawable.dinotime_still_01, R.drawable.dinotime_still_02,
            R.drawable.dinotime_still_03, R.drawable.dinotime_still_04,
            R.drawable.dinotime_still_05    };
    public static Integer[] twenty_still =      {   R.drawable.twenty_youtube,
            R.drawable.twenty_still_01, R.drawable.twenty_still_02,
            R.drawable.twenty_still_03, R.drawable.twenty_still_04,
            R.drawable.twenty_still_05      };
    public static Integer[] whiplash_still =    {   R.drawable.whiplash_youtube,
            R.drawable.whiplash_still_01, R.drawable.whiplash_still_02,
            R.drawable.whiplash_still_03, R.drawable.whiplash_still_04,
            R.drawable.whiplash_still_05    };
    public static Integer[] spy_still =         {   R.drawable.spy_youtube,
            R.drawable.spy_still_01, R.drawable.spy_still_02,
            R.drawable.spy_still_03, R.drawable.spy_still_04,
            R.drawable.spy_still_05         };
    public static Integer[] lovetaste_still =   {   R.drawable.lovetaste_youtube,
            R.drawable.lovetaste_still_01, R.drawable.lovetaste_still_02,
            R.drawable.lovetaste_still_03, R.drawable.lovetaste_still_04,
            R.drawable.lovetaste_still_05   };

    // 영화 리스트 받아가는 메서드
    // 각 영화는 HashMap<String, Object> 객체로, resource id값을 value로 가진다.
    // ArrayList로 영화 리스트를 구현한다.
    public ArrayList<HashMap<String, Object>> getMovieList() {
        ArrayList<HashMap<String, Object>> movieList = new ArrayList<>();
        HashMap<String, Object> movie;

        movie = new HashMap<>();
        movie.put("movie_title", R.string.avengers_title);
        movie.put("movie_title_e", R.string.avengers_title_e);
        movie.put("movie_genre", R.string.avengers_genre);
        movie.put("movie_country", R.string.avengers_country);
        movie.put("movie_time", R.string.avengers_time);
        movie.put("movie_releasedate", R.string.avengers_releasedate);
        movie.put("movie_director", R.string.avengers_director);
        movie.put("movie_actors", R.string.avengers_actors);
        movie.put("movie_level", R.string.avengers_level);
        movie.put("movie_synopsis", R.string.avengers_synopsis);
        movie.put("movie_audiencegrade", R.string.avengers_audiencegrade);
        movie.put("movie_youtube", R.string.avengers_youtube);
        movie.put("movie_poster", R.drawable.avengers_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.home_title);
        movie.put("movie_title_e", R.string.home_title_e);
        movie.put("movie_genre", R.string.home_genre);
        movie.put("movie_country", R.string.home_country);
        movie.put("movie_time", R.string.home_time);
        movie.put("movie_releasedate", R.string.home_releasedate);
        movie.put("movie_director", R.string.home_director);
        movie.put("movie_actors", R.string.home_actors);
        movie.put("movie_level", R.string.home_level);
        movie.put("movie_synopsis", R.string.home_synopsis);
        movie.put("movie_audiencegrade", R.string.home_audiencegrade);
        movie.put("movie_youtube", R.string.home_youtube);
        movie.put("movie_poster", R.drawable.home_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.jangsoo_title);
        movie.put("movie_title_e", R.string.jangsoo_title_e);
        movie.put("movie_genre", R.string.jangsoo_genre);
        movie.put("movie_country", R.string.jangsoo_country);
        movie.put("movie_time", R.string.jangsoo_time);
        movie.put("movie_releasedate", R.string.jangsoo_releasedate);
        movie.put("movie_director", R.string.jangsoo_director);
        movie.put("movie_actors", R.string.jangsoo_actors);
        movie.put("movie_level", R.string.jangsoo_level);
        movie.put("movie_synopsis", R.string.jangsoo_synopsis);
        movie.put("movie_audiencegrade", R.string.jangsoo_audiencegrade);
        movie.put("movie_youtube", R.string.jangsoo_youtube);
        movie.put("movie_poster", R.drawable.jangsoo_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.chinatown_title);
        movie.put("movie_title_e", R.string.chinatown_title_e);
        movie.put("movie_genre", R.string.chinatown_genre);
        movie.put("movie_country", R.string.chinatown_country);
        movie.put("movie_time", R.string.chinatown_time);
        movie.put("movie_releasedate", R.string.chinatown_releasedate);
        movie.put("movie_director", R.string.chinatown_director);
        movie.put("movie_actors", R.string.chinatown_actors);
        movie.put("movie_level", R.string.chinatown_level);
        movie.put("movie_synopsis", R.string.chinatown_synopsis);
        movie.put("movie_audiencegrade", R.string.chinatown_audiencegrade);
        movie.put("movie_youtube", R.string.chinatown_youtube);
        movie.put("movie_poster", R.drawable.chinatown_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.kingsman_title);
        movie.put("movie_title_e", R.string.kingsman_title_e);
        movie.put("movie_genre", R.string.kingsman_genre);
        movie.put("movie_country", R.string.kingsman_country);
        movie.put("movie_time", R.string.kingsman_time);
        movie.put("movie_releasedate", R.string.kingsman_releasedate);
        movie.put("movie_director", R.string.kingsman_director);
        movie.put("movie_actors", R.string.kingsman_actors);
        movie.put("movie_level", R.string.kingsman_level);
        movie.put("movie_synopsis", R.string.kingsman_synopsis);
        movie.put("movie_audiencegrade", R.string.kingsman_audiencegrade);
        movie.put("movie_youtube", R.string.kingsman_youtube);
        movie.put("movie_poster", R.drawable.kingsman_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.dinotime_title);
        movie.put("movie_title_e", R.string.dinotime_title_e);
        movie.put("movie_genre", R.string.dinotime_genre);
        movie.put("movie_country", R.string.dinotime_country);
        movie.put("movie_time", R.string.dinotime_time);
        movie.put("movie_releasedate", R.string.dinotime_releasedate);
        movie.put("movie_director", R.string.dinotime_director);
        movie.put("movie_actors", R.string.dinotime_actors);
        movie.put("movie_level", R.string.dinotime_level);
        movie.put("movie_synopsis", R.string.dinotime_synopsis);
        movie.put("movie_audiencegrade", R.string.dinotime_audiencegrade);
        movie.put("movie_youtube", R.string.dinotime_youtube);
        movie.put("movie_poster", R.drawable.dinotime_350);
        movieList.add(movie);


        movie = new HashMap<>();
        movie.put("movie_title", R.string.twenty_title);
        movie.put("movie_title_e", R.string.twenty_title_e);
        movie.put("movie_genre", R.string.twenty_genre);
        movie.put("movie_country", R.string.twenty_country);
        movie.put("movie_time", R.string.twenty_time);
        movie.put("movie_releasedate", R.string.twenty_releasedate);
        movie.put("movie_director", R.string.twenty_director);
        movie.put("movie_actors", R.string.twenty_actors);
        movie.put("movie_level", R.string.twenty_level);
        movie.put("movie_synopsis", R.string.twenty_synopsis);
        movie.put("movie_audiencegrade", R.string.twenty_audiencegrade);
        movie.put("movie_youtube", R.string.twenty_youtube);
        movie.put("movie_poster", R.drawable.twenty_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.whiplash_title);
        movie.put("movie_title_e", R.string.whiplash_title_e);
        movie.put("movie_genre", R.string.whiplash_genre);
        movie.put("movie_country", R.string.whiplash_country);
        movie.put("movie_time", R.string.whiplash_time);
        movie.put("movie_releasedate", R.string.whiplash_releasedate);
        movie.put("movie_director", R.string.whiplash_director);
        movie.put("movie_actors", R.string.whiplash_actors);
        movie.put("movie_level", R.string.whiplash_level);
        movie.put("movie_synopsis", R.string.whiplash_synopsis);
        movie.put("movie_audiencegrade", R.string.whiplash_audiencegrade);
        movie.put("movie_youtube", R.string.whiplash_youtube);
        movie.put("movie_poster", R.drawable.whiplash_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.spy_title);
        movie.put("movie_title_e", R.string.spy_title_e);
        movie.put("movie_genre", R.string.spy_genre);
        movie.put("movie_country", R.string.spy_country);
        movie.put("movie_time", R.string.spy_time);
        movie.put("movie_releasedate", R.string.spy_releasedate);
        movie.put("movie_director", R.string.spy_director);
        movie.put("movie_actors", R.string.spy_actors);
        movie.put("movie_level", R.string.spy_level);
        movie.put("movie_synopsis", R.string.spy_synopsis);
        movie.put("movie_audiencegrade", R.string.spy_audiencegrade);
        movie.put("movie_youtube", R.string.spy_youtube);
        movie.put("movie_poster", R.drawable.spy_350);
        movieList.add(movie);

        movie = new HashMap<>();
        movie.put("movie_title", R.string.lovetaste_title);
        movie.put("movie_title_e", R.string.lovetaste_title_e);
        movie.put("movie_genre", R.string.lovetaste_genre);
        movie.put("movie_country", R.string.lovetaste_country);
        movie.put("movie_time", R.string.lovetaste_time);
        movie.put("movie_releasedate", R.string.lovetaste_releasedate);
        movie.put("movie_director", R.string.lovetaste_director);
        movie.put("movie_actors", R.string.lovetaste_actors);
        movie.put("movie_level", R.string.lovetaste_level);
        movie.put("movie_synopsis", R.string.lovetaste_synopsis);
        movie.put("movie_audiencegrade", R.string.lovetaste_audiencegrade);
        movie.put("movie_youtube", R.string.lovetaste_youtube);
        movie.put("movie_poster", R.drawable.lovetaste_350);
        movieList.add(movie);

        return movieList;
    }

    // 영화관 리스트 받아가는 메서드
    // 각 영화관은 HashMap<String, Object> 객체로, resource id값을 value로 가진다.
    // ArrayList로 영화관 리스트를 구현한다.
    public ArrayList<HashMap<String, Object>> getTheaterList() {
        ArrayList<HashMap<String, Object>> theaterList = new ArrayList<>();
        HashMap<String, Object> theater;

        theater = new HashMap<>();
        theater.put("theater_name", R.string.theater01_name);
        theater.put("theater_lat", R.string.theater01_lat);
        theater.put("theater_lon", R.string.theater01_lon);
        theater.put("theater_address", R.string.theater01_address);
        theater.put("theater_tel", R.string.theater01_tel);
        theaterList.add(theater);

        theater = new HashMap<>();
        theater.put("theater_name", R.string.theater02_name);
        theater.put("theater_lat", R.string.theater02_lat);
        theater.put("theater_lon", R.string.theater02_lon);
        theater.put("theater_address", R.string.theater02_address);
        theater.put("theater_tel", R.string.theater02_tel);
        theaterList.add(theater);

        theater = new HashMap<>();
        theater.put("theater_name", R.string.theater03_name);
        theater.put("theater_lat", R.string.theater03_lat);
        theater.put("theater_lon", R.string.theater03_lon);
        theater.put("theater_address", R.string.theater03_address);
        theater.put("theater_tel", R.string.theater03_tel);
        theaterList.add(theater);

        theater = new HashMap<>();
        theater.put("theater_name", R.string.theater04_name);
        theater.put("theater_lat", R.string.theater04_lat);
        theater.put("theater_lon", R.string.theater04_lon);
        theater.put("theater_address", R.string.theater04_address);
        theater.put("theater_tel", R.string.theater04_tel);
        theaterList.add(theater);

        return theaterList;
    }

}
