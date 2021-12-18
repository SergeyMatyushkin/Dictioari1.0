package geekbrains.ru.translator.di

import androidx.room.Room
import geekbrains.ru.history.view.history.HistoryActivity
import geekbrains.ru.history.view.history.HistoryInteractor
import geekbrains.ru.history.view.history.HistoryViewModel
import geekbrains.ru.model.data.dto.SearchResultDto

import geekbrains.ru.model.room.HistoryDataBase
import geekbrains.ru.repository.*
import geekbrains.ru.translator.view.main.MainActivity
import geekbrains.ru.translator.view.main.MainInteractor
import geekbrains.ru.translator.view.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }
    single { get<HistoryDataBase>().historyDao() }
    single<Repository<List<SearchResultDto>>> { RepositoryImplementation(RetrofitImplementation()) }
    single<RepositoryLocal<List<SearchResultDto>>> {
        RepositoryImplementationLocal(RoomDataBaseImplementation(get()))
    }
}

val mainScreen = module {
    scope(named<MainActivity>()) {
        scoped { MainInteractor(get(), get()) }
        viewModel { MainViewModel(get()) }
    }
}

val historyScreen = module {
    scope(named<HistoryActivity>()) {
        scoped { HistoryInteractor(get(), get()) }
        viewModel { HistoryViewModel(get()) }
    }
}
