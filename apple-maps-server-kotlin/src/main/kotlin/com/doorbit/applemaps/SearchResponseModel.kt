package com.doorbit.applemaps

data class SearchResponse(
    val displayMapRegion: DisplayMapRegion,
    val results: List<Place> = emptyList()
)