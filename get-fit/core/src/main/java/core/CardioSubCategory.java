package core;

public enum CardioSubCategory implements Subcategory {
        /**
         * Short exercises.
         */
        SHORT,
        /**
         * Long exercises.
         */
        LONG,
        /**
         * High intensity exercises.
         */
        HIGHINTENSITY,
        /**
         * Low intensity exercises.
         */
        LOWINTENSITY;

        @Override
        public Subcategory getValueOf(final String name) {
            return CardioSubCategory.valueOf(name);
        }
}
