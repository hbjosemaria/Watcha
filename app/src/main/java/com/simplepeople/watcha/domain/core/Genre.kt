package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.R

enum class Genre(val id: Int, val title: Int) {
    ACTION(28, R.string.genre_action),
    ADVENTURE(12, R.string.genre_adventure),
    ANIMATION(16, R.string.genre_animation),
    COMEDY(35, R.string.genre_comedy),
    CRIME(80, R.string.genre_crime),
    DOCUMENTARY(99, R.string.genre_documentary),
    DRAMA(18, R.string.genre_drama),
    FAMILY(10751, R.string.genre_family),
    FANTASY(14, R.string.genre_fantasy),
    HISTORY(36, R.string.genre_history),
    HORROR(27, R.string.genre_horror),
    MUSIC(10402, R.string.genre_music),
    MYSTERY(9648, R.string.genre_mystery),
    ROMANCE(10749, R.string.genre_romance),
    SCIENCE_FICTION(878, R.string.genre_science_fiction),
    TV_MOVIE(10770, R.string.genre_tv_movie),
    THRILLER(53, R.string.genre_thriller),
    WAR(10752, R.string.genre_war),
    WESTERN(37, R.string.genre_western),
    NOT_SPECIFIED(-1, R.string.genre_not_specified)
}