package kr.saintdev.hangrim.modules.retrofit

data class HangrimWord (
    val word_korean: String,
    val word_english: String,
    val word_symbol: String,
    val prop_category: String,
    val prop_uuid: String )

data class MyExpressWord (
    val imagePath: String,
    val uuid: String,
    val called: String,
    val prono: String
)

data class VerifyReamin(
    val is_remain: Boolean
)