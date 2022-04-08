package edu.singaporetech.hr

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AttendenceActivityValidatorTest::class,
    LeaveActivityValidatorTest::class,
    PayslipActivityValidatorTest::class,
    SignActivityTest::class,
)
class UnitTestSuite