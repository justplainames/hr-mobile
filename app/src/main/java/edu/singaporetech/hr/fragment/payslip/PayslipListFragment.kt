package edu.singaporetech.hr.fragment.payslip

import android.Manifest
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.data.Payslip
import edu.singaporetech.hr.adapter.PayslipAdapter
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.PayslipViewModel
import edu.singaporetech.hr.databinding.FragmentPayslipListBinding
import java.time.LocalDate

class PayslipListFragment() : Fragment() , PayslipAdapter.OnItemClickListener {
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
        val binding = DataBindingUtil.inflate<FragmentPayslipListBinding>(
            inflater, R.layout.fragment_payslip_list, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListObserver = Observer<ArrayList<Payslip>> { items->
            adapter= PayslipAdapter(items,this) // add items to adapter
            binding.payslipListRecyclerView.adapter=adapter
        }

        viewModel.payslip.observe(requireActivity(), payslipListObserver)

        binding.payslipListRecyclerView.layoutManager=LinearLayoutManager(activity)
        binding.payslipListRecyclerView.setHasFixedSize(true)
        return binding.root
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



    @SuppressLint("NewApi")
    override fun onItemClickDownload(position: Int) {
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)

        getActivity()?.getViewModelStore()?.clear()

        val payslipListObserver = Observer<java.util.ArrayList<Payslip>> { payslip->
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
        biometricAuthentication()
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
        //values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
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
                    Toast.makeText(this@PayslipListFragment.requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
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