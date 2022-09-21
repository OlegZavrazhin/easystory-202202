package ru.otus.otuskotlin.easystory.business.logic.general

import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.helpers.errorAdministration
import ru.otus.otuskotlin.easystory.common.helpers.fail
import ru.otus.otuskotlin.easystory.common.models.ESWorkMode
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.cor.ICorChainDsl
import ru.otus.otuskotlin.easystory.cor.worker

fun ICorChainDsl<EasyStoryContext>.initRepo(title: String) = worker {
    this.title = title
    handle {
        blockRepo = when (workMode) {
            ESWorkMode.TEST  -> settings.repoTest
            ESWorkMode.STUB -> IBlockRepository.NONE
            else -> settings.repoProd
        }
        if (workMode != ESWorkMode.STUB && blockRepo == IBlockRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is not configured for chosen workmode ($workMode). " +
                        "Please, contact the administrator staff"
            )
        )
    }
}