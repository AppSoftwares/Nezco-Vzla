package com.example.ui.viewmodel

import androidx.compose.runtime.Composable
import com.example.data.repository.NezcoRepository
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.MockDao

@Composable
actual fun provideNezcoViewModel(): NezcoViewModel {
    return viewModel { NezcoViewModel(NezcoRepository(MockDao())) }
}
