package com.egadwys.gi_employee.dashboard.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.egadwys.gi_employee.dashboard.fragment.AttendanceFragment
import com.egadwys.gi_employee.dashboard.fragment.DashboardFragment
import com.egadwys.gi_employee.dashboard.fragment.PayrollFragment
import com.egadwys.gi_employee.dashboard.fragment.SplFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardFragment()
            1 -> AttendanceFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }



}
