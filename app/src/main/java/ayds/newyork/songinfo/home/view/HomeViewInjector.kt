package ayds.newyork.songinfo.home.view

import ayds.newyork.songinfo.home.controller.HomeControllerInjector
import ayds.newyork.songinfo.home.model.HomeModelInjector

object HomeViewInjector {

    val songDateStrategyFactory: SongDateStrategyFactory = SongDateStrategyFactoryImpl
    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl()

    fun init(homeView: HomeView) {
        HomeModelInjector.initHomeModel(homeView)
        HomeControllerInjector.onViewStarted(homeView)
    }
}