package ru.otus.otuskotlin.marketplace.mappers.jackson.exceptions

class UnknownRequestClass(classVal: Class<*>): RuntimeException("Class $classVal cannot be mapped")
