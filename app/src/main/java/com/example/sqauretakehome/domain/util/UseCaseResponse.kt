package com.example.sqauretakehome.domain.util

sealed class UseCaseResponse<T>(val data: T? = null, val errorMsg: String? = null) {

    /**
     * UseCaseResponse.Success if api call was successful.
     * If returned list was empty or malformed, Success is still called
     */
    class Success<T>(data: T) : UseCaseResponse<T>(data = data)

    /**
     * UseCaseResponse.Error if there was an error with the api call.
     */
    class Error<T>(errorMsg: String) : UseCaseResponse<T>(errorMsg = errorMsg)
}

enum class EmployeeListState {
    LOADING,
    EMPTY_LIST,
    VALID_LIST,
    ERROR
}