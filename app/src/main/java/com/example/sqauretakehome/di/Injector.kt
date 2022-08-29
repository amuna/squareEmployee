package com.example.sqauretakehome.di

import com.example.sqauretakehome.presentation.EmployeeViewModel
import com.example.sqauretakehome.network.EmployeeService
import com.example.sqauretakehome.usecases.GetAllEmployeesUseCase
import com.google.gson.GsonBuilder
import org.kodein.di.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injector {

    private val networkModule = DI.Module("network") {
        bind<EmployeeService>() with singleton {
            Retrofit.Builder()
                .baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
                .create(EmployeeService::class.java)
        }
    }

    private val usecasesModule = DI.Module("usecases") {
        bind { singleton { GetAllEmployeesUseCase(instance()) }}
    }

    private val viewModelModel = DI.Module("viewmodel") {
        bind { singleton { EmployeeViewModel(instance()) }}

    }

    private val diContainer = DI { importAll(networkModule, usecasesModule, viewModelModel)}
    val di: DI = diContainer
}