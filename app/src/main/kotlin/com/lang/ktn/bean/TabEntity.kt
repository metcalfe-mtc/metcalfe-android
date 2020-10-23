package com.lang.ktn.bean

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Created by ziyong on 2018/11/16.
 * com.bvc.adt.bean.TabEntity
 * —。—
 */
class TabEntity(var title: String, var selectedIcon: Int, var unSelectedIcon: Int) :
    CustomTabEntity {

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}
