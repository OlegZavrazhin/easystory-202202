package ru.otus.otuskotlin.easystory.repository.inmemory

import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.repository.test.RepoBlockDeleteTest

class BlockRepoInMemoryDeleteTest: RepoBlockDeleteTest() {
    override val repo: IBlockRepository = BlockRepoInMemory(initObjects = initObjects)
}