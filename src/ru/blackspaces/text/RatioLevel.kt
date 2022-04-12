package ru.blackspaces.text

enum class RatioLevel {
    STRONG,
    LIKELY,
    NEED_REVIEW;

    companion object {
        fun intToLevel(ratio: Int): RatioLevel? {
            return if (ratio > 70) {
                STRONG
            } else if (ratio > 50) {
                LIKELY
            } else if (ratio > 30) {
                NEED_REVIEW
            } else {
                null
            }
        }

        fun isAboveThreshold(ratio: Int): Boolean {
            return ratio > 30
        }
    }
}
