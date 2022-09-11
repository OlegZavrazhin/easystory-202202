package ru.otus.otuskotlin.easystory.repository.inmemory

import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.repository.test.RepoBlockUpdateTest

class BlockRepoInMemoryUpdateTest: RepoBlockUpdateTest() {
    override val repo: IBlockRepository = BlockRepoInMemory(initObjects = initObjects, randomUUID = { newLock.asString() })
}