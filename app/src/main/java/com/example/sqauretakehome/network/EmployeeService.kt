package com.example.sqauretakehome.network


import com.example.sqauretakehome.domain.Employees
import retrofit2.http.GET

interface EmployeeService {

    /**
     * Returns an Employees object with valid List of Employee
     */
    @GET("employees.json")
    suspend fun getAllEmployees() : Employees

    /**
     * Returns an Employees object with an empty list
     */
    @GET("employees_empty.json")
    suspend fun getEmptyList() : Employees

    /**
     * Returns an Employees object with a list of malformed Employee
     */
    @GET("employees_malformed.json")
    suspend fun getMalformedList() : Employees
}