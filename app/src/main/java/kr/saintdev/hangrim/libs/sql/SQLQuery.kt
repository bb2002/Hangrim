package kr.saintdev.hangrim.libs.sql

object SQLQuery {
    val SHUFFLE_LOG = "CREATE TABLE hg_shuffle_log (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "word_korean TEXT," +
            "word_english TEXT," +
            "word_symbol TEXT," +
            "prop_category TEXT," +
            "prop_uuid TEXT)"

    val READ_SHUFFLE_LOG = "SELECT * FROM hg_shuffle_log"
}