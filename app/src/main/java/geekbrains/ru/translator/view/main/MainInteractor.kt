package geekbrains.ru.translator.view.main

import geekbrains.ru.core.viewmodel.Interactor
import geekbrains.ru.model.data.AppState
import geekbrains.ru.model.data.dto.SearchResultDto
import geekbrains.ru.repository.Repository
import geekbrains.ru.repository.RepositoryLocal
import geekbrains.ru.translator.utils.mapSearchResultToResult

class MainInteractor(
    private val repositoryRemote: Repository<List<SearchResultDto>>,
    private val repositoryLocal: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        val appState: AppState
        if (fromRemoteSource) {
            appState = AppState.Success(mapSearchResultToResult(repositoryRemote.getData(word)))
            repositoryLocal.saveToDB(appState)
        } else {
            appState = AppState.Success(mapSearchResultToResult(repositoryLocal.getData(word)))
        }
        return appState
    }
}
