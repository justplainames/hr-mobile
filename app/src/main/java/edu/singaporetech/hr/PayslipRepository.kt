package edu.singaporetech.hr
import androidx.lifecycle.LiveData

class PayslipRepository(private val payslipDao: PayslipDao) {

    var getLatest3: LiveData<List<Payslip>> = payslipDao.getLatest3()
    var getAll: LiveData<List<Payslip>> = payslipDao.getAll()
    var getLatestMth: LiveData<List<Payslip>> = payslipDao.getLatestMth()

    suspend fun insert(payslip: Payslip) {
        payslipDao.insert(payslip)
    }

    suspend fun deleteAll() {
        payslipDao.deleteAll()
    }

}