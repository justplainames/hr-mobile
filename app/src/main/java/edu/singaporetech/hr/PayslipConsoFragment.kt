package edu.singaporetech.hr

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.whiteelephant.monthpicker.MonthPickerDialog
import edu.singaporetech.hr.databinding.FragmentPayslipConsoBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PayslipConsoFragment() : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var inputpayslipDatePickerTo: String
    lateinit var inputpayslipDatePickerFrom: String
    private lateinit var viewModel: PayslipConsoViewModel
    private val PERMISSION_CODE=1000
    var tempTotalDeductionArray = java.util.ArrayList<String>()
    var tempTotalEarningArray = java.util.ArrayList<String>()
    var tempCpfArray = java.util.ArrayList<String>()
    var tempAsstFundArray = java.util.ArrayList<String>()
    var tempBasicWageArray = java.util.ArrayList<String>()
    var tempBonusArray = java.util.ArrayList<String>()
    var tempOpeOthersArray = java.util.ArrayList<String>()
    var tempNetPayArray = java.util.ArrayList<String>()
    var tempOtArray = java.util.ArrayList<String>()
    var tempDateOfPayDayArray = java.util.ArrayList<String>()


    var monthTo:String=""
    var yearTo:String=""
    var monthFrom:String=""
    var yearFrom:String=""
    var factory = object : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PayslipConsoViewModel(
                this@PayslipConsoFragment.requireActivity()!!.application,
                monthTo, yearTo, monthFrom, yearFrom
            ) as T
        }
    }

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    val permissionGranted=requestStoragePermission()
                    if (permissionGranted){
                        showDownloadDialog()
                        notifyUser("Payslip downloaded in Downloads/HR folder!")
                    }


                }
            }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipConsoBinding>(
            inflater,R.layout.fragment_payslip_conso, container, false
        )
        fun getMonthString(month: Int): String? {
            val result = when (month) {
                0 -> "Jan"
                1 -> "Feb"
                2 -> "Mar"
                3 -> "Apr"
                4 -> "May"
                5 -> "Jun"
                6 -> "Jul"
                7 -> "Aug"
                8 -> "Sept"
                9 -> "Oct"
                10 -> "Nov"
                11 -> "Dec"

                else -> null
            }
            return result
        }
        fun getMonthInt(month: String): Int? {
            val result = when (month) {
                "Jan" -> 0
                "Feb" -> 1
                "Mar" ->2
                "Apr" ->3
                "May" ->4
                "Jun" ->5
                "Jul" ->6
                "Aug" ->7
                "Sept"->8
                "Oct"->9
                "Nov" ->10
                "Dec" -> 11

                else -> null
            }
            return result
        }
        val today = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val dateTobuilder = MonthPickerDialog.Builder(
            this@PayslipConsoFragment.requireActivity(), { selectedMonthTo, selectedYearTo ->
                inputpayslipDatePickerTo = "${getMonthString(selectedMonthTo)} $selectedYearTo"

                binding.payslipDatePickerToInput.setText(inputpayslipDatePickerTo)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)

        )

        dateTobuilder.setActivatedMonth(today.get(Calendar.MONTH))
            .setMinYear(2021)
            .setActivatedYear(2022)
            .setMaxYear(2022)
            .setTitle("SELECT MONTH AND YEAR OF PAYSLIP")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

            binding.payslipDatePickerTo.setEndIconOnClickListener{
                dateTobuilder.build().show()
            }
        val dateFrombuilder = MonthPickerDialog.Builder(
            this@PayslipConsoFragment.requireActivity(), { selectedMonthFrom, selectedYearFrom ->
                inputpayslipDatePickerFrom = "${getMonthString(selectedMonthFrom)} $selectedYearFrom"

                binding.payslipDatePickerFromInput.setText(inputpayslipDatePickerFrom)
            },

            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH)

        )

        dateFrombuilder.setActivatedMonth(today.get(Calendar.MONTH))
            .setMinYear(2021)
            .setActivatedYear(2022)
            .setMaxYear(2022)
            .setTitle("SELECT MONTH AND YEAR OF PAYSLIP")
            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)

        binding.payslipDatePickerFrom.setEndIconOnClickListener{
            dateFrombuilder.build().show()
        }

        binding.submitButton.setOnClickListener {

            var datepickerFrom= binding.payslipDatePickerFromInput.text.toString()
            var datepickerTo= binding.payslipDatePickerToInput.text.toString()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            var year= formatted.split("-")[0]
            var mth= formatted.split("-")[1]
            if (datepickerTo.isNullOrBlank() || datepickerFrom.isNullOrBlank()) {
                Toast.makeText(
                    this@PayslipConsoFragment.requireActivity(),
                    "Please fill in all fields before submit",
                    Toast.LENGTH_LONG
                ).show()
            }

            else if(((datepickerFrom.split(" ")[1].toInt() > datepickerTo.split(" ")[1].toInt()) &&
                (getMonthInt(datepickerFrom.split(" ")[0])!! > getMonthInt(datepickerTo.split(" ")[0])!!))||((datepickerFrom.split(" ")[1].toInt() > datepickerTo.split(" ")[1].toInt()) &&
                        (getMonthInt(datepickerFrom.split(" ")[0])!! < getMonthInt(datepickerTo.split(" ")[0])!!))||(((datepickerFrom.split(" ")[1].toInt() == datepickerTo.split(" ")[1].toInt()) &&
                        (getMonthInt(datepickerFrom.split(" ")[0])!! > getMonthInt(datepickerTo.split(" ")[0])!!)))) {

                Toast.makeText(
                    this@PayslipConsoFragment.requireActivity(),
                    "Date(From) cannot be later than Date(To)",
                    Toast.LENGTH_LONG
                ).show()
            }
            else if ((getMonthInt(datepickerTo.split(" ")[0])!!.toInt() >= mth.toInt())||(getMonthInt(datepickerFrom.split(" ")[0])!!.toInt() >= mth.toInt())){
                Toast.makeText(
                    this@PayslipConsoFragment.requireActivity(),
                    "Date(To)/Date(From) cannot be later than current date",
                    Toast.LENGTH_LONG
                ).show()
            }

            else{
                monthTo=datepickerTo.split(" ")[0]
                yearTo=datepickerTo.split(" ")[1]
                monthFrom=datepickerFrom.split(" ")[0]
                yearFrom=datepickerFrom.split(" ")[1]
                viewModel = ViewModelProvider(requireActivity(),factory).get(PayslipConsoViewModel::class.java)

                getActivity()?.getViewModelStore()?.clear()

                val payslipListObserver = Observer<ArrayList<Payslip>> { payslip->

                    for (item in payslip){
                        tempDateOfPayDayArray.add(android.text.format.DateFormat.format("MMM yyyy", item.dateOfPayDay).toString()!!)

                        tempTotalDeductionArray.add(item.totalDeduction.toString()!!)
                        tempTotalEarningArray.add(item.totalEarning.toString()!!)
                        tempCpfArray.add(item.cpf.toString()!!)
                        tempBasicWageArray.add(item.basicWage.toString()!!)
                        tempBonusArray.add(item.bonus.toString()!!)
                        tempOpeOthersArray.add(item.opeOthers.toString()!!)
                        tempNetPayArray.add(item.netPay.toString()!!)
                        tempOtArray.add(item.ot.toString()!!)
                        tempAsstFundArray.add(item.asstFund.toString()!!)


                    }
                }
                viewModel.payslip.observe(requireActivity(), payslipListObserver)
                biometricAuthentication()

            }



        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDownloadDialog(){
        val values= ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME,"Payslip_${LocalDate.now()}")
        values.put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS+"/HR")
//        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        val uri: Uri? = requireActivity().getContentResolver().insert(MediaStore.Files.getContentUri("external"),values)
        if (uri!=null){
            var outputStream=requireActivity().getContentResolver().openOutputStream(uri)
            var document= Document()

            PdfWriter.getInstance(document,outputStream)
            document.open()

            document.addAuthor("HR")
            addDataIntoPDF(document)
            tempTotalDeductionArray.clear()
            tempTotalEarningArray.clear()
            tempCpfArray.clear()
            tempAsstFundArray.clear()
            tempBasicWageArray.clear()
            tempBonusArray.clear()
            tempOpeOthersArray.clear()
            tempNetPayArray.clear()
            tempOtArray.clear()
            tempDateOfPayDayArray.clear()
            document.close()
        }
    }




    private fun requestStoragePermission():Boolean{
        var permissionGranted=false

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if ( checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED) {
                var permission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                permissionGranted = true
            }
        }
        else{
            permissionGranted=true
        }
        return permissionGranted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showDownloadDialog()
                    notifyUser("Payslip downloaded in Downloads/HR folder!")
                }else{
                    Toast.makeText(this@PayslipConsoFragment.requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addDataIntoPDF(document: Document){
        val paraGraph= Paragraph()

        val headingFont = Font(Font.FontFamily.HELVETICA,24F,Font.BOLD)
        val subHeaderFont = Font(Font.FontFamily.HELVETICA,20F,Font.BOLD)
        val bodyFont = Font(Font.FontFamily.HELVETICA,18F,Font.BOLD)
        val smallerFont = Font(Font.FontFamily.HELVETICA,13F,Font.BOLD)
        for (i in 0.rangeTo(tempDateOfPayDayArray.size-1)){
            paraGraph.add(Paragraph("PAYSLIP ADVICE",headingFont))
            addEmptyLines(paraGraph,2)
            paraGraph.add(Paragraph("PERIOD: END-"+tempDateOfPayDayArray[i],subHeaderFont))
            addEmptyLines(paraGraph,2)
            paraGraph.add(Paragraph("TOTAL EARNING: $"+tempTotalEarningArray[i],subHeaderFont))
            paraGraph.add(Paragraph("_______________________________",subHeaderFont))
            paraGraph.add(Paragraph("BASIC WAGE: $"+tempBasicWageArray[i],bodyFont))
            paraGraph.add(Paragraph("BONUS: $"+tempBonusArray[i],bodyFont))
            paraGraph.add(Paragraph("OT: $"+tempOtArray[i],bodyFont))
            addEmptyLines(paraGraph,2)
            paraGraph.add(Paragraph("TOTAL DEDUCTION: $"+tempTotalDeductionArray[i],subHeaderFont))
            paraGraph.add(Paragraph("_______________________________",subHeaderFont))
            paraGraph.add(Paragraph("OPE-Others: $"+tempOpeOthersArray[i],bodyFont))
            paraGraph.add(Paragraph("CPF: $"+tempCpfArray[i],bodyFont))
            paraGraph.add(Paragraph("ASST.FUND: $"+tempAsstFundArray[i],bodyFont))
            addEmptyLines(paraGraph,2)
            paraGraph.add(Paragraph("NET PAY: $"+tempNetPayArray[i],subHeaderFont))
            addEmptyLines(paraGraph,3)
            paraGraph.add(Paragraph("This is a computer-generated report. Please inform Head/Payroll of HR division immediately should there be any discrepancy in this statement",smallerFont))
            addEmptyLines(paraGraph,7)

        }
        document.add(paraGraph)
    }

    fun addEmptyLines(paragraph: Paragraph,lineCount:Int){
        for (i in 0 until lineCount){
            paragraph.add(Paragraph("\n"))
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
                    .commitNow()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (activity?.let { ActivityCompat.checkSelfPermission(it.applicationContext, android.Manifest.permission.USE_BIOMETRIC) } != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        } else true
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun biometricAuthentication(){
        checkBiometricSupport()
        val biometricPrompt = activity?.let { it1 ->
            BiometricPrompt.Builder(activity).setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .setTitle("Payslip is password protected!")
                .setSubtitle("Sign in using biometrics")
                .setNegativeButton(
                    "Cancel",
                    it1.mainExecutor,
                    DialogInterface.OnClickListener { dialog, which ->
                        notifyUser("Authentication Cancelled")
                    }).build()
        }

        if (biometricPrompt != null) {
            biometricPrompt.authenticate(
                getCancellationSignal(),
                requireContext().mainExecutor,
                authenticationCallback
            )
        }
    }

    private fun notifyUser(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal() : CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }
}
