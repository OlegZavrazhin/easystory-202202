package ru.otus.otuskotlin.easystory.common.models

import ru.otus.otuskotlin.easystory.common.repository.IBlockRepository

data class ESSettings(
    val  repoStub: IBlockRepository = IBlockRepository.NONE,
    val  repoTest: IBlockRepository = IBlockRepository.NONE,
    val  repoProd: IBlockRepository = IBlockRepository.NONE,
)
