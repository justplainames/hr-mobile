package edu.singaporetech.hr

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LeaveNavigationTest::class,
    AttendanceNavigationTest::class,
    PayslipNavigationTest::class
)
class NavigationTestSuite