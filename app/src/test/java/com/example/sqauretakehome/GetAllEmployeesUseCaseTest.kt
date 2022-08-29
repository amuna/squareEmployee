package com.example.sqauretakehome

import android.util.Log
import com.example.sqauretakehome.domain.Employee
import com.example.sqauretakehome.domain.Employees
import com.example.sqauretakehome.domain.util.UseCaseResponse
import com.example.sqauretakehome.network.EmployeeService
import com.example.sqauretakehome.usecases.GetAllEmployeesUseCase
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn

class GetAllEmployeesUseCaseTest {

    @Mock
    private lateinit var employeeService: EmployeeService

    private lateinit var getAllEmployeesUseCase: GetAllEmployeesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getAllEmployeesUseCase = GetAllEmployeesUseCase(employeeService)

        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    fun emptyListReturnedFromService_VerifyCallSuccessAndEmptyListData() = runBlocking {
        val response = Employees(emptyList())

        doReturn(response).`when`(employeeService).getAllEmployees()

        val result = getAllEmployeesUseCase.execute()

        assert(result is UseCaseResponse.Success)
        assert(result.data!!.isEmpty())
    }

    @Test
    fun malformedListReturnedFromService_VerifyCallSuccessAndEmptyListData() = runBlocking {
        val e = Employee("", "","","","","","","","")
        val response = Employees(listOf(e))

        doReturn(response).`when`(employeeService).getAllEmployees()

        val result = getAllEmployeesUseCase.execute()

        assert(result is UseCaseResponse.Success)
        assert(result.data!!.isEmpty())
    }

    @Test
    fun validListReturnedFromService_VerifyCallSuccessAndListSize() = runBlocking {
        val e = Employee("123", "Ahmed","232","asf@gmail","hi",
            "sfasf","asdf","asdf","asdf")
        val response = Employees(listOf(e))

        doReturn(response).`when`(employeeService).getAllEmployees()

        val result = getAllEmployeesUseCase.execute()

        assert(result is UseCaseResponse.Success)
        assert(result.data!!.size == 1)
    }


    @Test
    fun errorReturnedFromService_VerifyCallErrorAndErrorMessage() = runBlocking {
        val exception = Exception()

        doReturn(exception).`when`(employeeService).getAllEmployees()

        val result = getAllEmployeesUseCase.execute()

        assert(result is UseCaseResponse.Error)
        assert(!result.errorMsg.isNullOrEmpty())
    }
}