package com.fftools.roomdatabase.di

import com.fftools.roomdatabase.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(noteDao = get()) }
}