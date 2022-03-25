package edu.singaporetech.hr

import android.os.Parcelable
import com.google.type.DateTime
import java.util.*


/**
 * However, with Cloud Firestore,
 * we can remove the last two parts of it.
 * This is because Firestore provides its own local cache.
 * An (additional) local cache is not needed or recommended.
 * This means we can remove the model and remote data source and
 * combine them in a single repository class as shown in the below diagram.
 * https://medium.com/firebase-developers/android-mvvm-firestore-37c3a8d65404
 * **/
//model class
data class AttendanceItem(
    var id: String = "", //document ID
    var clockInDate: Date? = null,
    var clockInAddress: String? = null,
    var clockOutDate: Date? = null,
    var clockOutAddress: String? = null,
    var IssueReason: String ?= null,
    var IssueStatus: String ?= null,
    var UserID: String ?= null,
    var attendanceStatus: String ?= null
)


