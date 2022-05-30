package ru.otus.otuskotlin.easystory.mappers.jackson.exceptions

import ru.otus.otuskotlin.easystory.common.models.ESProcess

class UnknownESProcess(proc: ESProcess) : Throwable("Unknown process $proc")
