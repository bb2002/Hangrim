package kr.saintdev.hangrim.views.activities.list

object CategoryItem {
    enum class ID(val key: String, val id: Int) {
        OBJECT("Object", 0),
        MY_OWN("MyOwn", 1),
        TALK("Expression", 2),
        KITCHEN("Kitchen", 3),
        PEOPLE("People", 4),
        TIME("Time", 5),
        NUMBER("Number", 6),
        BODY("Body", 7),
        PLACE("Place", 8),
        ANIMAL("Animal", 9),
        ETC("ETC.", 10),
        ANY("Any", 11)
    }

    enum class Source {
        SERVER, DB
    }

    /**
     * @Date 01.20
     */
    fun toCategoryID(str: String) =
        when(str) {
            ID.OBJECT.key   -> ID.OBJECT
            ID.ANIMAL.key   -> ID.ANIMAL
            ID.BODY.key     -> ID.BODY
            ID.ETC.key      -> ID.ETC
            ID.TIME.key     -> ID.TIME
            ID.KITCHEN.key  -> ID.KITCHEN
            ID.MY_OWN.key   -> ID.MY_OWN
            ID.NUMBER.key   -> ID.NUMBER
            ID.PEOPLE.key   -> ID.PEOPLE
            ID.PLACE.key    -> ID.PLACE
            ID.TALK.key     -> ID.TALK
            ID.ANY.key      -> ID.ANY
            else            -> ID.ANY
        }

    fun toCategoryID(idx: Int) =
        when(idx) {
            ID.OBJECT.id   -> ID.OBJECT
            ID.ANIMAL.id   -> ID.ANIMAL
            ID.BODY.id     -> ID.BODY
            ID.ETC.id      -> ID.ETC
            ID.TIME.id     -> ID.TIME
            ID.KITCHEN.id  -> ID.KITCHEN
            ID.MY_OWN.id   -> ID.MY_OWN
            ID.NUMBER.id   -> ID.NUMBER
            ID.PEOPLE.id   -> ID.PEOPLE
            ID.PLACE.id    -> ID.PLACE
            ID.TALK.id     -> ID.TALK
            ID.ANY.id      -> ID.ANY
            else           -> ID.ANY
        }
}