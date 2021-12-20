package geekbrains.ru.history.view.history

import geekbrains.ru.core.viewmodel.Interactor
import geekbrains.ru.history.mapSearchResultToResult
import geekbrains.ru.model.data.AppState
import geekbrains.ru.model.data.dto.SearchResultDto
import geekbrains.ru.repository.Repository
import geekbrains.ru.repository.RepositoryLocal

class HistoryInteractor(
    private val repositoryRemote: Repository<List<SearchResultDto>>,
    private val repositoryLocal: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {
        return AppState.Success(
            mapSearchResultToResult(
                if (fromRemoteSource) {
                    repositoryRemote
                } else {
                    repositoryLocal
                }.getData(word)
            )
        )
    }
}
