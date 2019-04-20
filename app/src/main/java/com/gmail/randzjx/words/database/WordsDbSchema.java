package com.gmail.randzjx.words.database;

public class WordsDbSchema {
    public static final class WordsTable {
        public static final String WORDS_TABLE = "words";

        public static final class Columns {
            public static final String WORD_ID = "word_id";
            public static final String FIRST_DESCRIPTION = "fist_d";
            public static final String SECOND_DESCRIPTION = "second_d";
            public static final String DATE = "date";
            public static final String TRAIN_NUM = "train_num";
            public static final String GUESSED = "guessed";
        }

    }
}
