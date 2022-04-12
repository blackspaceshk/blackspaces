package ru.blackspaces.text

data class CheckFoundResult(
    val searchOrganizationInfo: SearchOrganizationInfo,
    val ratio: Int,
    val ratioLevel: RatioLevel
)
