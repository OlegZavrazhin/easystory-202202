package ru.otus.otuskotlin.easystory.common.repository

interface IBlockRepository {
    suspend fun createBlock(request: DBBlockRequest): DBBlockResponse
    suspend fun readBlock(request: DBBlockIdRequest): DBBlockResponse
    suspend fun updateBlock(request: DBBlockRequest): DBBlockResponse
    suspend fun deleteBlock(request: DBBlockIdRequest): DBBlockResponse
    suspend fun searchBlock(request: DBBlockFilterRequest): DBBlocksResponse

    companion object {
        val NONE = object : IBlockRepository {
            override suspend fun createBlock(request: DBBlockRequest): DBBlockResponse {
                TODO("waiting for implementation")
            }

            override suspend fun readBlock(request: DBBlockIdRequest): DBBlockResponse {
                TODO("waiting for implementation")
            }

            override suspend fun updateBlock(request: DBBlockRequest): DBBlockResponse {
                TODO("waiting for implementation")
            }

            override suspend fun deleteBlock(request: DBBlockIdRequest): DBBlockResponse {
                TODO("waiting for implementation")
            }

            override suspend fun searchBlock(request: DBBlockFilterRequest): DBBlocksResponse {
                TODO("waiting for implementation")
            }
        }
    }
}