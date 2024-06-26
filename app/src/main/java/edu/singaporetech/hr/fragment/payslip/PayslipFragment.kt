package edu.singaporetech.hr.fragment.payslip

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.data.Payslip
import edu.singaporetech.hr.adapter.PayslipAdapter
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.PayslipViewModel
import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import edu.singaporetech.hr.fragment.HomeFragment
import java.time.LocalDate
import java.util.*
/*
    PayslipFragment : Payslip Fragment
        - View the overview of the Payslip section
            - inclusive of:
                1. View the 3 recent payslip records
                2. "View all" button to navigate to the list of payslip
                3. Brief data visualisation of the most recent payslip
                    - When button clicked,
                        1. Biometric Authentication
                        2. If success, Download the most recent payslip
                        3. Trigger the processing of firebase variables into the PDF
                2. "Conslidated Payslip" label that has a next arrow
                        1. navigate to the month year picker
 */
class PayslipFragment : Fragment(), PayslipAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var adapter : PayslipAdapter
    private val PERMISSION_CODE=1000
    var tempTotalDeduction = ""
    var tempTotalEarning = ""
    var tempCpf = ""
    var tempAsstFund = ""
    var tempBasicWage = ""
    var tempBonus= ""
    var tempOpeOthers = ""
    var tempNetPay= ""
    var tempOt= ""
    var tempDateOfPayDay= ""

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipBinding>(
            inflater, R.layout.fragment_payslip, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListObserver = Observer<ArrayList<Payslip>> { items->
            adapter= PayslipAdapter(items.take(3) as ArrayList<Payslip>,this) // add items to adapter
            binding.recyclerViewPaySlip.adapter=adapter
        }

        viewModel.payslip.observe(requireActivity(), payslipListObserver)

        binding.recyclerViewPaySlip.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlip.setHasFixedSize(true)

        val payslipMoreDetailsButton= binding.payslipMoreDetailsButton
        payslipMoreDetailsButton.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipDetailFragment(0))
                    .commitNow()
        }

        val payslipDownloadConsoButton= binding.payslipDownloadConsoButton
        payslipDownloadConsoButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipConsoFragment())
                .commitNow()
        }
        val payslipListButton= binding.payslipListButton
        payslipListButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipListFragment())
                .commitNow()
        }


        viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
            binding.selectedPayslipMthYr.setText(android.text.format.DateFormat.format("MMM yyyy", payslip[0].dateOfPayDay).toString())

            binding.textViewNetPay.setText("NetPay: ${payslip[0].netPay.toString()}")
            var netpay: Float = payslip[0].netPay?.toFloat() ?: 0.0f
            var earning: Float =payslip[0].totalEarning?.toFloat() ?: 0.0f
            val value:Float= ((netpay) / (earning)) *360f
            val circularProgressBar = binding.circularProgressBar
            circularProgressBar.apply {

                setProgressWithAnimation(value, 2000) // =1s
                // Set Progress Max
                progressMax = 360f
                // Set background ProgressBar Color
                backgroundProgressBarColor = Color.RED
                // or with gradient
                backgroundProgressBarColorDirection =
                    CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                // Set Width
                progressBarWidth = 15f // in DP
                backgroundProgressBarWidth = 15f // in DP
                // Other
                roundBorder = false
                startAngle = 0f
                progressDirection = CircularProgressBar.ProgressDirection.TO_LEFT
            }
        })


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "HomePage"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomeFragment())
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClickDownload(position: Int) {
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)

        getActivity()?.getViewModelStore()?.clear()

        val payslipListObserver = Observer<ArrayList<Payslip>> { payslip->
                tempDateOfPayDay=android.text.format.DateFormat.format("MMM yyyy", payslip[position].dateOfPayDay).toString()!!
                tempTotalDeduction=payslip[position].totalDeduction.toString()!!
                tempTotalEarning=payslip[position].totalEarning.toString()!!
                tempCpf=payslip[position].cpf.toString()!!
                tempBasicWage=payslip[position].basicWage.toString()!!
                tempBonus=payslip[position].bonus.toString()!!
                tempOpeOthers=payslip[position].opeOthers.toString()!!
                tempNetPay=payslip[position].netPay.toString()!!
                tempOt=payslip[position].ot.toString()!!
                tempAsstFund=payslip[position].asstFund.toString()!!

        }
        viewModel.payslip.observe(requireActivity(), payslipListObserver)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricAuthentication()
        }
    }
    override fun onItemClickNext(position: Int) {
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, PayslipDetailFragment(position))
            .commitNow()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun showDownloadDialog(){
        val values= ContentValues()
        values.put(MediaStore.MediaColumns.DISPLAY_NAME,"Payslip_${LocalDate.now()}")
        values.put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOWNLOADS+"/HR")
        val uri: Uri? = requireActivity().getContentResolver().insert(MediaStore.Files.getContentUri("external"),values)
        if (uri!=null){
            var outputStream=requireActivity().getContentResolver().openOutputStream(uri)
            var document= Document()

            PdfWriter.getInstance(document,outputStream)
            document.open()

            document.addAuthor("HR")
            addDataIntoPDF(document)

            document.close()
        }
    }

    fun addDataIntoPDF(document: Document){
        val paraGraph= Paragraph()

        val headingFont = Font(Font.FontFamily.HELVETICA,24F, Font.BOLD)
        val subHeaderFont = Font(Font.FontFamily.HELVETICA,20F, Font.BOLD)
        val bodyFont = Font(Font.FontFamily.HELVETICA,18F, Font.BOLD)
        val smallerFont = Font(Font.FontFamily.HELVETICA,13F, Font.BOLD)
        paraGraph.add(Paragraph("PAYSLIP ADVICE",headingFont))
        addEmptyLines(paraGraph,2)
        paraGraph.add(Paragraph("PERIOD: END-"+tempDateOfPayDay,subHeaderFont))
        addEmptyLines(paraGraph,2)
        paraGraph.add(Paragraph("TOTAL EARNING: $"+tempTotalEarning,subHeaderFont))
        paraGraph.add(Paragraph("_______________________________",subHeaderFont))
        paraGraph.add(Paragraph("BASIC WAGE: $"+tempBasicWage,bodyFont))
        paraGraph.add(Paragraph("BONUS: $"+tempBonus,bodyFont))
        paraGraph.add(Paragraph("OT: $"+tempOt,bodyFont))
        addEmptyLines(paraGraph,2)
        paraGraph.add(Paragraph("TOTAL DEDUCTION: $"+tempTotalDeduction,subHeaderFont))
        paraGraph.add(Paragraph("_______________________________",subHeaderFont))
        paraGraph.add(Paragraph("OPE-Others: $"+tempOpeOthers,bodyFont))
        paraGraph.add(Paragraph("CPF: $"+tempCpf,bodyFont))
        paraGraph.add(Paragraph("ASST.FUND: $"+tempAsstFund,bodyFont))
        addEmptyLines(paraGraph,2)
        paraGraph.add(Paragraph("NET PAY: $"+tempNetPay,subHeaderFont))
        addEmptyLines(paraGraph,3)
        paraGraph.add(Paragraph("This is a computer-generated report. Please inform Head/Payroll of HR division immediately should there be any discrepancy in this statement",smallerFont))
        addEmptyLines(paraGraph,7)
        document.add(paraGraph)
    }
    private fun requestStoragePermission():Boolean{
        var permissionGranted=false

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if ( ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                    Toast.makeText(this@PayslipFragment.requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun addEmptyLines(paragraph: Paragraph, lineCount:Int){
        for (i in 0 until lineCount){
            paragraph.add(Paragraph("\n"))
        }
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


