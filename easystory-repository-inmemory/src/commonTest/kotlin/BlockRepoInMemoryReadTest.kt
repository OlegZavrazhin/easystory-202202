package ru.otus.otuskotlin.easystory.repository.inmemory

import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.repository.test.RepoBlockReadTest

class BlockRepoInMemoryReadTest: RepoBlockReadTest() {
    override val repo: IBlockRepository = BlockRepoInMemory(initObjects = initObjects)
}