package ru.otus.otuskotlin.easystory.mappers.jackson.exceptions

class UnknownRequestClass(classVal: Class<*>): RuntimeException("Class $classVal cannot be mapped")
