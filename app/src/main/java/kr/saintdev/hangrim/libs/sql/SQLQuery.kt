package kr.saintdev.hangrim.libs.sql

object SQLQuery {
    const val SHUFFLE_LOG = "CREATE TABLE hg_shuffle_log (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "word_korean TEXT," +
            "word_english TEXT," +
            "word_symbol TEXT," +
            "prop_category TEXT," +
            "prop_uuid TEXT)"

    const val READ_SHUFFLE_LOG = "SELECT * FROM hg_shuffle_log"

    const val MY_EXPR_LOG = "CREATE TABLE hg_myexpr_log (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "image_path TEXT," +
            "expr_uuid TEXT," +
            "expr_called TEXT," +
            "expr_prono TEXT)"

    const val READ_MY_EXPRESS_ALL_LOG = "SELECT * FROM hg_myexpr_log ORDER BY _id DESC"
    const val READ_MY_EXPRESS_LOG = "SELECT * FROM hg_myexpr_log WHERE expr_uuid = ?"
    const val WRITE_MY_EXPRESS_LOG = "INSERT INTO hg_myexpr_log (image_path, expr_uuid, expr_called, expr_prono) VALUES(?,?,?,?)"
    const val UPDATE_MY_EXPRESS_LOG = "UPDATE hg_myexpr_log SET expr_called = ?, expr_prono = ? WHERE expr_uuid = ?"
}