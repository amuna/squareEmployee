package com.example.sqauretakehome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sqauretakehome.domain.Employee
import com.example.sqauretakehome.domain.util.UseCaseResponse
import com.example.sqauretakehome.presentation.EmployeeViewModel
import com.example.sqauretakehome.usecases.GetAllEmployeesUseCase
import io.mockk.*
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times

@OptIn(ExperimentalCoroutinesApi::class)
class EmployeeViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockGetAllEmployeesUseCase: GetAllEmployeesUseCase

    private lateinit var employeeViewModel: EmployeeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun viewModelInitialized_VerifyGetAllEmployeesUseCaseTriggered(): Unit = runBlocking {
        employeeViewModel = EmployeeViewModel(mockGetAllEmployeesUseCase)
        Mockito.verify(mockGetAllEmployeesUseCase, times(1)).execute()
    }

    @Test
    fun useCaseReturnError_VerifyErrorLiveDataObserved() = runBlocking {
        val response = UseCaseResponse.Error<List<Employee>>("error")
        doReturn(response).`when`(mockGetAllEmployeesUseCase).execute()

        employeeViewModel = EmployeeViewModel(mockGetAllEmployeesUseCase)

        assert(employeeViewModel.error.value == "error")
    }

    @Test
    fun useCaseReturnEmptyListSuccess_VerifyEmptyEmployeeListLiveDataObserved() = runBlocking {
        val response = UseCaseResponse.Success<List<Employee>>(emptyList())
        doReturn(response).`when`(mockGetAllEmployeesUseCase).execute()

        employeeViewModel = EmployeeViewModel(mockGetAllEmployeesUseCase)

        assert(employeeViewModel.emptyEmployeeList.value!!)
    }
}