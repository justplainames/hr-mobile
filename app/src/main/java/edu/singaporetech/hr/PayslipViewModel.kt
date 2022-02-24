package edu.singaporetech.firstapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PayslipViewModel(application: Application): AndroidViewModel(application) {

    val getLatest3: LiveData<List<Payslip>>
    val getAll: LiveData<List<Payslip>>
    val getLatestMth: LiveData<List<Payslip>>
    private var payslip_repository: PayslipRepository
    init{
        val payslipDao=PayslipRoomDatabase.getDatabase(application).payslip()
        payslip_repository= PayslipRepository(payslipDao)
        getLatest3=payslip_repository.getLatest3
        getAll=payslip_repository.getAll
        getLatestMth=payslip_repository.getLatestMth
    }

     fun insert(payslip: Payslip) = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            payslip_repository.insert(payslip)
        }
    }

}

