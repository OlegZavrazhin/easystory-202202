package ru.otus.otuskotlin.easystory.business.logic

import ru.otus.otuskotlin.easystory.business.logic.general.initRepo
import ru.otus.otuskotlin.easystory.business.logic.general.initStatus
import ru.otus.otuskotlin.easystory.business.logic.general.operation
import ru.otus.otuskotlin.easystory.business.logic.general.prepareResult
import ru.otus.otuskotlin.easystory.business.logic.repo.*
import ru.otus.otuskotlin.easystory.business.logic.stubs.*
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.cor.chain
import ru.otus.otuskotlin.easystory.cor.rootChain
import ru.otus.otuskotlin.easystory.cor.worker

class BlockProcessor(private val settings: ESSettings = ESSettings()) {
    suspend fun exec(context: EasyStoryContext) =
        BusinessChain.exec(context.apply { settings = this@BlockProcessor.settings })

    companion object {
        private val BusinessChain = rootChain<EasyStoryContext> {

            initStatus("Start processing")
            initRepo("Init repository")

//            CREATION CHAIN START
            operation("create block", ESProcess.CREATE) {

                stubs("Stubs create") {
                    stubCreateSuccess("Success block creation stub")
                    stubBadTitle("Title error during create stub")
                    noSuchStub()
                }

                chain {
                    title = "Validate request during create"

                    copyToValidationField()
                    checkEmptyTitle()
                    checkEmptyContent()
                    completeValidation()
                }

                chain {
                    title = "Saving to DB"

                    repoPrepareCreate("Preparing block  to save")
                    repoCreate("Inserting into DB")
                }
                prepareResult("create prepare result")

            }
//            CREATION CHAIN END
//            READ BLOCK START
            operation("read block", ESProcess.READ) {
                stubs("read stubs") {
                    stubSuccess("read success stub")

                    stubBadId("read bad id stub")

                    noSuchStub()
                }

                chain {
                    title = "Validate request during read"

                    copyToValidationField()

                    checkEmptyId()

                    completeValidation()

                }

                chain {
                    title = "Get from DB"
                    repoRead("Read from DB")
                    worker {
                        title = "Preparing response for read"
                        on { state == CORState.RUNNING }
                        handle { blockRepoDone = blockRepoRead }
                    }
                }

                prepareResult("prepare result")
            }
//            READ BLOCK END
//            UPDATE BLOCK START
            operation("update block", ESProcess.UPDATE) {
                stubs("Stubs update") {
                    stubSuccess("Success block stub during update")
                    stubBadId("bad id stub during update")
                    stubBadTitle("Title error during update")

                    noSuchStub()
                }

                chain {
                    title = "Validate request during update"

                    copyToValidationField()

                    checkEmptyId()
                    checkEmptyTitle()
                    checkEmptyContent()

                    completeValidation()

                }

                chain {
                    title = "Update in DB"
                    repoRead("Read from DB during update")
                    repoCheckReadLock("Check lock")
                    repoPrepareUpdate("Prepare update")
                    repoUpdate("Update repo with block")
                }
                prepareResult("prepare result")
            }
//            DELETE CHAIN START
            operation("delete block", ESProcess.DELETE) {
                stubs("Stubs delete") {
                    stubSuccess("Success block stub during delete")
                    stubBadId("bad id stub during delete")

                    noSuchStub()
                }

                chain {
                    title = "Validate request during delete"

                    copyToValidationField()

                    checkEmptyId()

                    completeValidation()
                }

                chain {
                    title = "Delete in DB"
                    repoRead("Read from DB during delete")
                    repoCheckReadLock("Check lock")
                    repoPrepareDelete("Prepare delete")
                    repoDelete("Delete in DB")
                }
                prepareResult("prepare result")

            }
//            DELETE CHAIN END
//            SEARCH CHAIN START
            operation("search blocks", ESProcess.SEARCH) {
                stubs("Stubs search") {
                    stubSearchSuccess("stub search success")
                    noSuchStub()
                }

                chain {
                    completeValidation()
                }

                repoSearch("Search in DB")
                prepareResult("prepare result")
            }
//            SEARCH CHAIN END
        }.build()
    }
}