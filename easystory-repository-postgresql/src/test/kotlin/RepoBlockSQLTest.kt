package ru.otus.otuskotlin.easystory.repository.postgresql

import ru.otus.otuskotlin.easystory.common.models.ESBlockLock
import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository
import ru.otus.otuskotlin.easystory.repository.test.*

class BlockRepoSQLCreateTest: RepoBlockCreateTest() {
    override val repo: IBlockRepository = SqlTestCompanion.repoUnderTestContainer(initObjects = initObjects)
}

class BlockRepoSQLDeleteTest: RepoBlockDeleteTest() {
    override val repo: IBlockRepository = SqlTestCompanion.repoUnderTestContainer(initObjects = initObjects)
}

class BlockRepoSQLReadTest: RepoBlockReadTest() {
    override val repo: IBlockRepository = SqlTestCompanion.repoUnderTestContainer(initObjects = initObjects)
}

class BlockRepoSQLSearchTest: RepoBlockSearchTest() {
    override val repo: IBlockRepository = SqlTestCompanion.repoUnderTestContainer(initObjects = initObjects)
}

class BlockRepoSQLUpdateTest: RepoBlockUpdateTest() {
    // expected:<ESBlockLock(id=0000-1111-2222-3334)> but was:<ESBlockLock(id=0000-1111-2222-3333)>
    override val repo: IBlockRepository = SqlTestCompanion.repoUnderTestContainer(initObjects = initObjects, ESBlockLock("0000-1111-2222-3334"))
}