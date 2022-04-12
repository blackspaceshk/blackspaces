package ru.blackspaces.text

import me.xdrop.fuzzywuzzy.FuzzySearch

class FuzzyWuzzyTextChecker(private val dictionary: Collection<SearchOrganizationInfo>) : TextChecker {
    override fun checkText(text: String): Collection<CheckFoundResult> {
        return dictionary.mapNotNull {
            listOf(
                listOf(FuzzySearch.tokenSortPartialRatio(text, it.name)),
                it.synonyms.map { synonym -> FuzzySearch.tokenSortPartialRatio(text, synonym) },
                it.synonyms.map { uri -> FuzzySearch.tokenSortPartialRatio(text, uri) }
            )
                .flatten()
                .maxOrNull()
                ?.takeIf { maxRatio -> RatioLevel.isAboveThreshold(maxRatio) }
                ?.let { maxRatio -> CheckFoundResult(it, maxRatio, RatioLevel.intToLevel(maxRatio)!!) }
        }
    }
}