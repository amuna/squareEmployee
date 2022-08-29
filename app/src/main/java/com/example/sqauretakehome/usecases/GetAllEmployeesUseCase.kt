package com.example.sqauretakehome.usecases

import android.util.Log
import com.example.sqauretakehome.domain.Employee
import com.example.sqauretakehome.domain.util.UseCaseResponse
import com.example.sqauretakehome.domain.validateEmployee
import com.example.sqauretakehome.network.EmployeeService
import java.lang.Exception

class GetAllEmployeesUseCase(
    private val employeeService: EmployeeService
) {
    private val TAG = "GetAllEmployeesUseCase"

     suspend fun execute() : UseCaseResponse<List<Employee>> {
        try {
            val result = employeeService.getAllEmployees().employees

            if (result.isNullOrEmpty()) {
                Log.i(TAG, "empty list")
                return UseCaseResponse.Success(emptyList())
            }

            //if any employee has malformed data, invalidate the entire list
            result.forEach { employee ->
                if(!employee.validateEmployee()) {
                    Log.i(TAG, "list contained malformed employee. Response invalidated")
                    return UseCaseResponse.Success(emptyList())
                }
            }

            Log.i(TAG, "valid result: $result")
            return UseCaseResponse.Success(data = result)

        } catch (e: Exception) {
            val error = e.message ?: "Unknown Error"
            Log.e(TAG, "error: $error")
            return UseCaseResponse.Error(errorMsg = error)
        }
    }
}