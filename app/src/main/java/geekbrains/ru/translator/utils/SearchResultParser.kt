package geekbrains.ru.translator.utils

import geekbrains.ru.model.data.AppState
import geekbrains.ru.model.data.dto.SearchResultDto

import geekbrains.ru.model.data.userdata.Meaning
import geekbrains.ru.model.data.userdata.TranslatedMeaning

fun mapSearchResultToResult(searchResults: List<SearchResultDto>): List<geekbrains.ru.model.data.userdata.DataModel> {
    return searchResults.map { searchResult ->
        var meanings: List<Meaning> = listOf()
        searchResult.meanings?.let {
            //Check for null for HistoryScreen
            meanings = it.map { meaningsDto ->
                Meaning(
                    TranslatedMeaning(meaningsDto?.translation?.translation ?: ""),
                    meaningsDto?.imageUrl ?: ""
                )
            }
        }
        geekbrains.ru.model.data.userdata.DataModel(searchResult.text ?: "", meanings)
    }
}

fun parseOnlineSearchResults(data: AppState): AppState {
    return AppState.Success(mapResult(data, true))
}

private fun mapResult(
    data: AppState,
    isOnline: Boolean
): List<geekbrains.ru.model.data.userdata.DataModel> {
    val newSearchResults = arrayListOf<geekbrains.ru.model.data.userdata.DataModel>()
    when (data) {
        is AppState.Success -> {
            getSuccessResultData(data, isOnline, newSearchResults)
        }
    }
    return newSearchResults
}

private fun getSuccessResultData(
    data: AppState.Success,
    isOnline: Boolean,
    newSearchDataModels: ArrayList<geekbrains.ru.model.data.userdata.DataModel>
) {
    val searchDataModels: List<geekbrains.ru.model.data.userdata.DataModel> = data.data as List<geekbrains.ru.model.data.userdata.DataModel>
    if (searchDataModels.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in searchDataModels) {
                parseOnlineResult(searchResult, newSearchDataModels)
            }
        } else {
            for (searchResult in searchDataModels) {
                newSearchDataModels.add(
                    geekbrains.ru.model.data.userdata.DataModel(
                        searchResult.text,
                        arrayListOf()
                    )
                )
            }
        }
    }
}

private fun parseOnlineResult(
    searchDataModel: geekbrains.ru.model.data.userdata.DataModel,
    newSearchDataModels: ArrayList<geekbrains.ru.model.data.userdata.DataModel>
) {
    if (searchDataModel.text.isNotBlank() && searchDataModel.meanings.isNotEmpty()) {
        val newMeanings = arrayListOf<Meaning>()
        newMeanings.addAll(searchDataModel.meanings.filter { it.translatedMeaning.translatedMeaning.isNotBlank() })
        if (newMeanings.isNotEmpty()) {
            newSearchDataModels.add(
                geekbrains.ru.model.data.userdata.DataModel(
                    searchDataModel.text,
                    newMeanings
                )
            )
        }
    }
}

fun convertMeaningsToSingleString(meanings: List<Meaning>): String {
    var meaningsSeparatedByComma = String()
    for ((index, meaning) in meanings.withIndex()) {
        meaningsSeparatedByComma += if (index + 1 != meanings.size) {
            String.format("%s%s", meaning.translatedMeaning.translatedMeaning, ", ")
        } else {
            meaning.translatedMeaning.translatedMeaning
        }
    }
    return meaningsSeparatedByComma
}