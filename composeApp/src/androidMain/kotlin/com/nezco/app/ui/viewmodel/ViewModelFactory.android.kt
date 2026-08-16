package com.nezco.app.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nezco.app.data.repository.NezcoRepository
import com.nezco.app.data.local.MockDao

@Composable
actual fun provideNezcoViewModel(): NezcoViewModel {
    return viewModel {
        NezcoViewModel(NezcoRepository(MockDao()))
    }
}
