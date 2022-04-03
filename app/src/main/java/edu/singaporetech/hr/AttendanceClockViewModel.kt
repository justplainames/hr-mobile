package edu.singaporetech.hr

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import com.itextpdf.text.pdf.security.LtvTimestamp.timestamp
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.time.*
import java.util.*


class AttendanceClockViewModel : ViewModel(){
    private var _clockRecords: MutableLiveData<ArrayList<AttendanceItem>> = MutableLiveData<ArrayList<AttendanceItem>>()
    private var _attendanceSummary : MutableLiveData<AttendanceSummary> = MutableLiveData<AttendanceSummary>()
    private var _clockInStatus : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private lateinit var firestore: FirebaseFirestore
    var documentId = ""


    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToClockStatus()
    }

    @SuppressLint("NewApi")
    private fun listenToClockStatus() {

        firestore.collection("Attendance")
            .orderBy("clockInDate", Query.Direction.DESCENDING)
            .addSnapshotListener {
                    snapshot, error ->
                if(error != null){
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    val clockRecords = ArrayList<AttendanceItem>()
                    val documents = snapshot.documents
                    documents.forEach{
                        val clockRecord = it.toObject(AttendanceItem::class.java)
                        if (clockRecord != null){
                            clockRecord.id = it.id

                            clockRecords.add(clockRecord!!)
                        }
                    }
                    _clockRecords.value = clockRecords
                    if (clockRecords.get(0).clockOutDate == null){
                        _clockInStatus.value = false
                        Log.i("TESTING", "CAME IN}")
                    } else {_clockInStatus.value = true}

                    Log.i("TESTING", "clockRecords.get(0): ${(clockRecords.get(0).clockOutDate)}")
                    Log.i("TESTING", "_clocInStatus.value: ${_clockInStatus.value}")

                }
            }


        val currentYear = LocalDateTime.now().year
        val currentMonth = LocalDateTime.now().month
        val startDate = LocalDateTime.of(currentYear, currentMonth, 1,0,0,1)
        var zdt = startDate.atZone(ZoneId.of("Singapore"));
        var millis = zdt.toInstant().toEpochMilli();


        firestore.collection("Attendance")
            .orderBy("clockInDate", Query.Direction.DESCENDING)
            .whereGreaterThanOrEqualTo("clockInDate", Date(millis))
            .whereLessThanOrEqualTo("clockInDate", Date())
            .addSnapshotListener {
                    snapshot, error ->
                if(error != null){
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if(snapshot!=null){

                    var daysWorked = 0
                    var hoursWorked = 0f
                    var minutesWorked = 0f
                    var daysMissed = 0
                    var totalOT = 0f
                    val documents = snapshot.documents
                    documents.forEach{
                        val attendanceRecord = it.toObject(AttendanceItem::class.java)

                        if (attendanceRecord != null){
                            // ADD THIS IN THE PARAMETER WHEN SETTLED attendanceRecord.attendanceStatus=="Present"
                            if (attendanceRecord.clockOutDate!=null){
                                var clockin = (attendanceRecord.clockInDate)?.getTime()
                                var clockout = (attendanceRecord.clockOutDate)?.getTime()
                                hoursWorked +=((clockout!! - clockin!!)/ 1000 / 60 / 60)
                                minutesWorked+=((clockout!! - clockin!!)/ 1000 / 60 % 60)
                                daysWorked += 1
                            } else if (attendanceRecord.attendanceStatus=="Absent"){
                                daysMissed += 1
                            } else {}

                        }

                    }
                    if(hoursWorked > 40 ){
                        totalOT = (40 - hoursWorked) * -1
                    }


                    _attendanceSummary.value = AttendanceSummary(hoursWorked=hoursWorked.toInt()
                        , daysWorked = daysWorked
                        , minutesWorked = minutesWorked.toInt()
                        , daysMissed = daysMissed
                        , totalOT = totalOT
                        , percentageMissed = daysWorked.toFloat()/(daysWorked.toFloat()+daysMissed.toFloat()))
                }
            }
    }

    fun save(clockstatus: AttendanceItem){
        val document = firestore.collection("Attendance").document()
        clockstatus.id = document.id
        val set = document.set(clockstatus)
        set.addOnSuccessListener {
            documentId = document.id
            Log.d("firebase", "attendance saved")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }

    fun update(document_id: String, clockoutTime: Date, clockoutAddress: String){
        val documenttId = _clockRecords.value!!.get(0).id
        Log.i("TESTING", "ViewModel.Update()")
        Log.i("TESTING", "documentID: ${documenttId}, Clockout Time:${clockoutTime}, Clockout Address: ${clockoutAddress}")
        val document = firestore.collection("Attendance").document(documenttId)
        Log.i("TESTING", "Created Document")
        val update = document.update("clockOutDate" , clockoutTime , "clockOutAddress", clockoutAddress)
        update.addOnSuccessListener {
            documentId = ""
            Log.d("firebase", "attendance saved")
        }
        update.addOnFailureListener {
            Log.d("firebase", "save failed")
        }
    }

    internal var attendance: MutableLiveData<ArrayList<AttendanceItem>>
        get() {return _clockRecords}
        set(value) {_clockRecords = value}

    internal var attendanceSummary: MutableLiveData<AttendanceSummary>
        get() {return _attendanceSummary}
        set(value) {_attendanceSummary = value}

    internal var clockInStatus: MutableLiveData<Boolean>
        get() {return _clockInStatus}
        set(value) {_clockInStatus = value}
}