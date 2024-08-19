package com.example.bancodigital.util

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.bancodigital.R
import com.example.bancodigital.databinding.LayoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


fun Fragment.initToolbar(toolbar: Toolbar, homeAsUpEnabled:Boolean = true, light: Boolean = false) {

    val iconBack = if(light) R.drawable.ic_arrow_white else R.drawable.ic_arrow

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(iconBack)
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
    toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }
}

fun Fragment.showBottomSheet(
    titleWarning: Int? = null,
    titleButtonWarning: Int? = null,
    message: String?,
    onClick: () -> Unit = {}
){

    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    val bottomSheetBinding: LayoutBottomSheetBinding = LayoutBottomSheetBinding.inflate(
        layoutInflater,
        null,
        false
    )
    bottomSheetBinding.txtWarningTitle.text = getString( titleWarning ?: R.string.text_title_bottom_sheet)
    bottomSheetBinding.txtWarningMessage.text = message ?: getText(R.string.error_generic)

    bottomSheetBinding.btnWarning.text = getString( titleButtonWarning ?: R.string.text_button_bottom_sheet)
    bottomSheetBinding.btnWarning.setOnClickListener {
        bottomSheetDialog.dismiss()
        onClick()
    }

    bottomSheetDialog.setContentView(bottomSheetBinding.root)
    bottomSheetDialog.show()

}