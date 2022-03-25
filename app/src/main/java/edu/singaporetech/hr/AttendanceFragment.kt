package edu.singaporetech.hr

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.databinding.FragmentAttendanceBinding
import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class AttendanceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: AttendanceModel
    private lateinit var adapter : AttendanceAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAttendanceBinding>(
            inflater,R.layout.fragment_attendance, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(AttendanceModel::class.java)
        //viewModelConso = ViewModelProvider(requireActivity()).get(PayslipConsoViewModel::class.java)
        val attendanceListObserver = Observer<ArrayList<Attendance>> { items->
            adapter=AttendanceAdapter(items) // add items to adapter
//            adapter=PayslipAdapter(items,this) // add items to adapter
            binding.recyclerViewAttendence.adapter=adapter
        }

        viewModel.attendance.observe(requireActivity(), attendanceListObserver)

        binding.recyclerViewAttendence.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewAttendence.setHasFixedSize(true)




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
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Attendance"
    }



//    override fun onItemClickNext(position: Int) {
//        requireActivity()
//            .supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.fragmentContainerView, PayslipDetailFragment(position))
//            .commitNow()
//    }

//


}


