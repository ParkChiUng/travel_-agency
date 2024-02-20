//package com.sessac.travel_agency.common
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.net.Uri
//import android.provider.MediaStore
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.AutoCompleteTextView
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.ActivityResultRegistry
//import androidx.activity.result.contract.ActivityResultContracts
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.sessac.travel_agency.R
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import java.util.concurrent.TimeUnit
//import kotlin.math.abs
//
//private val dialogMap = HashMap<View, BottomSheetDialog>()
//
//fun commonToast(message: String){
//    Toast.makeText(TravelAgencyApplication.getTravelApplication(), message, Toast.LENGTH_SHORT).show()
//}
//
///**
// * [spinner 핸들러]
// * @param items 스피너에서 보여질 item 리스트
// * @param spinner 스피너 레이아웃
// * @param context 프레그먼트 context
// */
//fun spinnerHandler(items: Array<String>, spinner: AutoCompleteTextView) {
//    val adapter = ArrayAdapter(TravelAgencyApplication.getTravelApplication(), android.R.layout.simple_dropdown_item_1line, items)
//    spinner.setAdapter(adapter)
//}
//
//
///**
// * [갤러리 선택 이미지 콜백 함수]
// * 1. 갤러리 앱 launch
// * 2. 선택한 image callback
// */
//fun imageSelectAndCallback(activityResultRegistry: ActivityResultRegistry, onImageSelected: (Uri) -> Unit) {
//    val galleryLauncher = activityResultRegistry.register(
//        "key",
//        ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            onImageSelected.invoke(result.data?.data!!)
//        }
//    }
//    //Tutor Pyo Photo Picker
//    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//    galleryLauncher.launch(galleryIntent)
//}
//
//
///**
// * [날짜 Text 리턴 함수]
// * 시작 날짜 종료 날짜 Text 리턴
// *
// * @param startDate 시작 날짜
// * @param endDate 종료 날짜
// * @return "startDate ~ endDate"
// *
// */
//fun dateHandler(startDate: Date, endDate: Date): String {
//    //tutor pyo
//    val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
//    return dateFormat.format(startDate) + " ~ " + dateFormat.format(endDate)
//}
//
//
///**
// * [day 구하는 함수]
// * 시작 날짜와 종료 날짜를 통해 며칠인지 계산하여 리턴함.
// *
// * @param startDate 시작 날짜
// * @param endDate 종료 날짜
// * @return 일수
// *
// * 시작 날짜와 종료 날짜를 그냥 계산하면 당일은 포함되지 않아 일수를 구할 때 하루가 줄어든 상태가 됨.
// * 따라서 하루를 추가하여 계산
// */
//fun dayCalculator(startDate: Date, endDate: Date): Int {
//    return TimeUnit.DAYS.convert(
//        abs(endDate.time + TimeUnit.DAYS.toMillis(1) - startDate.time),
//        TimeUnit.MILLISECONDS
//    ).toInt()
//}
//
///**
// * dialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme) // 키보드가 레이아웃 가리지 않게 & 모서리 둥글게
// *         dialog.setContentView(view)
// *         dialog.show()
// */
//fun showBottomSheet(view: View) {
//
//    if (view.parent != null) (view.parent as ViewGroup).removeView(view)
//
//    val dialog = BottomSheetDialog(TravelAgencyApplication.getTravelApplication(), R.style.AppBottomSheetDialogTheme).apply {
//        setContentView(view)
//        show()
//    }
//
//    dialogMap[view] = dialog
//}
//
//fun dismissBottomSheet(view: View) {
//    dialogMap[view]?.dismiss()
//    dialogMap.remove(view)
//}
//
