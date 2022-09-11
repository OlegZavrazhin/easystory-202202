package ru.otus.otuskotlin.easystory.repository.inmemory

import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.repository.test.RepoBlockSearchTest

class BlockRepoInMemorySearchTest: RepoBlockSearchTest() {
    override val repo: IBlockRepository = BlockRepoInMemory(initObjects = initObjects)
}