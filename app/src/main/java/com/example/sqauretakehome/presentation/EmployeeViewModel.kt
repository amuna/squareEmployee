package com.example.sqauretakehome.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sqauretakehome.domain.Employee
import com.example.sqauretakehome.domain.util.UseCaseResponse
import com.example.sqauretakehome.usecases.GetAllEmployeesUseCase
import kotlinx.coroutines.launch

class EmployeeViewModel(
    private val getAllEmployeesUseCase: GetAllEmployeesUseCase
) : ViewModel() {

    val employeeList : MutableLiveData<List<Employee>> = MutableLiveData()
    val emptyEmployeeList : MutableLiveData<Boolean> = MutableLiveData()
    val error : MutableLiveData<String> = MutableLiveData()

    init {
        getEmployees()
    }

    fun getEmployees() {
        viewModelScope.launch {
            when(val response = getAllEmployeesUseCase.execute()) {
                is UseCaseResponse.Success -> {
                    if(response.data!!.isEmpty()) {
                        emptyEmployeeList.postValue(true)
                    } else {
                        employeeList.postValue(response.data!!)
                    }
                }

                is UseCaseResponse.Error -> {
                    error.postValue(response.errorMsg!!)
                }
            }
        }
    }
}