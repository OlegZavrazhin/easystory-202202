package ru.otus.otuskotlin.easystory.business.logic

import easystory.stubs.Story
import ru.otus.otuskotlin.easystory.common.EasyStoryContext
import ru.otus.otuskotlin.easystory.common.models.*
import ru.otus.otuskotlin.easystory.cor.chain
import ru.otus.otuskotlin.easystory.cor.rootChain
import ru.otus.otuskotlin.easystory.cor.worker

class BlockProcessor {
    suspend fun exec(context: EasyStoryContext) = BusinessChain.exec(context)

    companion object {
        private val BusinessChain = rootChain<EasyStoryContext> {
            worker {
                title = "Start processing"
                on { state == CORState.NONE }
                handle { state = CORState.RUNNING }
            }

//            CREATION CHAIN START
            chain {
                title = "create block"
                on { process == ESProcess.CREATE && state == CORState.RUNNING }

                chain {
                    title = "Stubs create"
                    on { workMode == ESWorkMode.STUB && state == CORState.RUNNING }

                    worker {
                        title = "Success block creation stub"
                        on { stubCase == ESStubs.SUCCESS && state == CORState.RUNNING }
                        handle {
                            state = CORState.FINISHING

                            blockResponse = Story.getBlock {
                                id = ESBlockId("created block stub")
                            }

                        }
                    }

                    stubBadTitle("Title error during create")

                    noSuchStub()
                }

                chain {
                    title = "Validate request during create"

                    copyToValidationField()

                    checkEmptyTitle()

                    checkEmptyContent()

                    completeValidation()

                }
            }
//            CREATION CHAIN END
//            READ BLOCK START
            chain {
                title = "read block"
                on { process == ESProcess.READ && state == CORState.RUNNING }

                chain {
                    title = "read stubs"
                    on { workMode == ESWorkMode.STUB && state == CORState.RUNNING }

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

            }
//            READ BLOCK END
//            UPDATE BLOCK START
            chain {
                title = "update block"
                on { process == ESProcess.UPDATE && state == CORState.RUNNING }

                chain {
                    title = "Stubs update"
                    on { workMode  == ESWorkMode.STUB && state == CORState.RUNNING }

                    stubSuccess("Success block stub during update")
                    stubBadId("bad id stub during update")
                    stubBadTitle("Title error during update")

                    noSuchStub()
                }

                chain {
                    title = "Validate request during read"

                    copyToValidationField()

                    checkEmptyId()
                    checkEmptyTitle()
                    checkEmptyContent()

                    completeValidation()

                }

            }

//            DELETE CHAIN START
            chain {
                title = "delete block"
                on { process == ESProcess.DELETE && state == CORState.RUNNING }

                chain {
                    title = "Stubs delete"
                    on { workMode == ESWorkMode.STUB && state == CORState.RUNNING }

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
            }
//            DELETE CHAIN END
//            SEARCH CHAIN START
            chain {
                title = "search blocks"
                on { process == ESProcess.SEARCH && state == CORState.RUNNING }

                chain {
                    title = "Stubs search"
                    on { workMode  == ESWorkMode.STUB && state == CORState.RUNNING }

                    worker {
                        title = "Success block stub during search"
                        on { stubCase == ESStubs.SUCCESS && state == CORState.RUNNING }

                        handle {
                            state = CORState.FINISHING

                            blocksResponse = Story.getBlocks()
                        }

                    }

                    noSuchStub()

                }

                chain {
                    completeValidation()
                }
            }
//            SEARCH CHAIN END
        }.build()
    }
}