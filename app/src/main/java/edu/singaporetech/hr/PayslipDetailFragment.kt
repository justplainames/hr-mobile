package edu.singaporetech.hr

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.databinding.FragmentPayslipDetailBinding
import java.time.LocalDate
import kotlin.properties.Delegates


class PayslipDetailFragment(private var position: Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var adapterEarning : PayslipEarningDetailAdapter
    private lateinit var adapterDeduction : PayslipDeductionDetailAdapter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipDetailBinding>(
            inflater,R.layout.fragment_payslip_detail, container, false
        )

        //EARNING
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListEarningObserver = Observer<ArrayList<Payslip>> { items->
            adapterEarning=PayslipEarningDetailAdapter(items,position) // add items to adapter
            binding.recyclerViewPaySlipDetailEarning.adapter=adapterEarning
        }
        viewModel.payslip.observe(requireActivity(), payslipListEarningObserver)

        binding.recyclerViewPaySlipDetailEarning.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailEarning.setHasFixedSize(true)

        //DEDUCTION
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListDeductionObserver = Observer<ArrayList<Payslip>> { items->
            adapterDeduction=PayslipDeductionDetailAdapter(items,position) // add items to adapter
            binding.recyclerViewPaySlipDetailDeduction.adapter=adapterDeduction
        }
        viewModel.payslip.observe(requireActivity(), payslipListDeductionObserver)

        binding.recyclerViewPaySlipDetailDeduction.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailDeduction.setHasFixedSize(true)

        viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
            binding.latestPayslipMthYr.setText("${android.text.format.DateFormat.format("MMM yyyy", payslip[position].dateOfPayDay).toString()}")
            binding.textViewNetPay.setText("NetPay: ${payslip[position].netPay.toString()}")
            binding.payslipEarning.setText("(+)Earning:$${payslip[position].totalEarning.toString()}")
            binding.payslipDeduction.setText("(-)Deduction:$${payslip[position].totalDeduction.toString()}")
            var netpay: Float = payslip[position].netPay?.toFloat() ?: 0.0f
            var earning: Float =payslip[position].totalEarning?.toFloat() ?: 0.0f
            val value:Float= ((netpay) / (earning)) *360f
            val circularProgressBar = binding.circularProgressBar
            circularProgressBar.apply {

                setProgressWithAnimation(value, 2000) // =1s
                // Set Progress Max
                progressMax = 360f
                // Set ProgressBar Color
                progressBarColor = Color.GREEN
                // Set background ProgressBar Color
                backgroundProgressBarColor = Color.GRAY
                // or with gradient
                backgroundProgressBarColorStart = Color.RED
                backgroundProgressBarColorEnd = Color.RED
                backgroundProgressBarColorDirection =
                    CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

                // Set Width
                progressBarWidth = 7f // in DP
                backgroundProgressBarWidth = 7f // in DP

                // Other
                roundBorder = true
                startAngle = 0f
                progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
            }
        })
        binding.payslipDownloadButton.setOnClickListener{
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
            showDownloadDialog()
        }

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDownloadDialog(){
        val builder = AlertDialog.Builder(requireActivity(),R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.dialog_payslipconso,null)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val downloadConsoButton = view.findViewById<Button>(R.id.downloadConsoButton)
        builder.setView(view)
        cancelButton.setOnClickListener {
            builder.dismiss()
        }
        downloadConsoButton.setOnClickListener{
            val values= ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME,"Payslip_${LocalDate.now()}")
            values.put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
            //values.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_DOCUMENTS+"/HR")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
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
            builder.dismiss()
            Toast.makeText(
                this@PayslipDetailFragment.requireActivity(),
                "Payslip downloaded in Downloads/HR folder!",
                Toast.LENGTH_LONG
            ).show()

        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
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
}

