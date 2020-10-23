package com.lang.ktn.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
// "{"wallet":{"secret":"sha8CUNS5oNyvnVc6AhvR6hsWmAZv","address":"yH5rZtg93hgSeMTeBh4tTULV8ZERt47vuZ"},
// "mnemonic":"TASK HUT JAR WIND BITE FATE LOOT NOR MAGI FAD NUT PAP"}"
@Parcelize
data class WalletBean(var wallet: WalletGenerate, val mnemonic: String, var psd: String, var nameWallet: String) : Parcelable{
    override fun toString(): String {
        return "WalletBean(wallet=$wallet, mnemonic='$mnemonic', psd='$psd', nameWallet='$nameWallet')"
    }
}