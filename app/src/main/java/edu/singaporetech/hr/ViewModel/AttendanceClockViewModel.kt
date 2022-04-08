package edu.singaporetech.hr.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.*
import edu.singaporetech.hr.data.AttendanceItem
import edu.singaporetech.hr.data.AttendanceStatus
import edu.singaporetech.hr.data.AttendanceSummary
import java.time.*
import java.util.*


class AttendanceClockViewModel : ViewModel(){
    private var _clockRecords: MutableLiveData<ArrayList<AttendanceItem>> = MutableLiveData<ArrayList<AttendanceItem>>()
    private var _attendanceSummary : MutableLiveData<AttendanceSummary> = MutableLiveData<AttendanceSummary>()
    private var _clockInStatus : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var documentId = ""
    private var _attendanceStatus: MutableLiveData<ArrayList<AttendanceStatus>> = MutableLiveData<ArrayList<AttendanceStatus>>()


    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listenToClockStatus()
        listenToAttendanceStatus()
    }

    @SuppressLint("NewApi")
    private fun listenToClockStatus() {

        firestore.collection("Attendance")
            .orderBy("clockInDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val clockRecords = ArrayList<AttendanceItem>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val clockRecord = it.toObject(AttendanceItem::class.java)
                        if (clockRecord != null) {
                            clockRecord.id = it.id

                            clockRecords.add(clockRecord)
                        }
                    }
                    _clockRecords.value = clockRecords
                    if (clockRecords.get(0).clockOutDate == null) {
                        _clockInStatus.value = false
                        Log.i("TESTING", "CAME IN}")
                    } else {
                        _clockInStatus.value = true
                    }

                    Log.i("TESTING", "clockRecords.get(0): ${(clockRecords.get(0).clockOutDate)}")
                    Log.i("TESTING", "_clocInStatus.value: ${_clockInStatus.value}")

                }
            }


        val currentYear = LocalDateTime.now().year
        val currentMonth = LocalDateTime.now().month
        val startDate = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 1)
        val zdt = startDate.atZone(ZoneId.of("Singapore"));
        var millis = zdt.toInstant().toEpochMilli();
        firestore.collection("Attendance")
            .orderBy("clockInDate", Query.Direction.DESCENDING)
            .whereGreaterThanOrEqualTo("clockInDate", Date(millis))
            .whereLessThanOrEqualTo("clockInDate", Date())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if (snapshot != null) {

                    var daysWorked = 0
                    var hoursWorked = 0f
                    var minutesWorked = 0f
                    var daysMissed = 0
                    var totalOT = 0f
                    var daysLate = 0
                    var daysOnTime = 0

                    val documents = snapshot.documents
                    documents.forEach {
                        val attendanceRecord = it.toObject(AttendanceItem::class.java)

                        if (attendanceRecord != null) {
                            // ADD THIS IN THE PARAMETER WHEN SETTLED attendanceRecord.attendanceStatus=="Present"
                            if (attendanceRecord.clockOutDate != null) {
                                var clockin = (attendanceRecord.clockInDate)?.getTime()
                                var clockout = (attendanceRecord.clockOutDate)?.getTime()

                                /**
                                 * After clocking in/out calculates the number of
                                 * 1) hours and minutes worked of the day
                                 * 2) updates number of 
                                 */
                                hoursWorked += ((clockout!! - clockin!!) / 1000 / 60 / 60)
                                minutesWorked += ((clockout!! - clockin!!) / 1000 / 60 % 60)
                                daysWorked += 1
                                daysMissed = 20 - daysWorked

                                if (attendanceRecord.attendanceStatus == "Late") {
                                    daysLate += 1
                                    Log.d("attendance record", daysLate.toString())
                                }

                                if (attendanceRecord.attendanceStatus == "onTime") {
                                    daysOnTime += 1
                                    Log.d("attendance record", daysOnTime.toString())
                                }
                                if (attendanceRecord.attendanceStatus == "Absent") {
                                    daysMissed += 1
                                    Log.d("attendance record", daysMissed.toString())
                                }
                            }
                            if (hoursWorked > 40) {
                                totalOT = (40 - hoursWorked) * -1
                            }
                            _attendanceSummary.value = AttendanceSummary(
                                hoursWorked = hoursWorked.toInt(),
                                daysWorked = daysWorked,
                                minutesWorked = minutesWorked.toInt(),
                                daysMissed = daysMissed,
                                totalOT = totalOT,
                                percentageMissed = daysWorked.toFloat() / (daysWorked.toFloat() + daysMissed.toFloat()),
                                daysLate = daysLate,
                                daysOnTime = daysWorked
                            )
                        }
                    }
                }
            }
    }

    private fun listenToAttendanceStatus() {
        firestore.collection("attendanceStatus")
            .addSnapshotListener {
                    snapshot, error ->
                if(error != null){
                    Log.e("firestore Error", error.message.toString())
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    val attendance = ArrayList<AttendanceStatus>()
//                val documents = snapshot.documents
                    snapshot!!.documents.forEach{
                        val attendanceStatus = it.toObject(AttendanceStatus::class.java)
                        if (attendanceStatus != null){
//                        leaveRecord.leaveId = it.id
                            attendance.add(attendanceStatus!!)
                        }
                    }
                    _attendanceStatus.value = attendance
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

    fun updateAttendanceStatusLate(){
        val document = firestore.collection("attendanceStatus").document("attendance")
        val set = document.update("late" ,FieldValue.increment(1) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved!!!!!!")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed!!!!")
        }
    }

    fun updateAttendanceStatusOnTime(){
        val document = firestore.collection("attendanceStatus").document("attendance")
        val set = document.update("onTime" ,FieldValue.increment(1) )
        set.addOnSuccessListener {
            Log.d("firebase", "document saved!!!!!!")
        }
        set.addOnFailureListener {
            Log.d("firebase", "save failed!!!!")
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

    internal var attendanceStatus: MutableLiveData<ArrayList<AttendanceStatus>>
        get() {return _attendanceStatus}
        set(value) {_attendanceStatus = value}

}