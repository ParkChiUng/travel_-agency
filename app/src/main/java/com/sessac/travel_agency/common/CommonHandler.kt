package com.sessac.travel_agency.common

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sessac.travel_agency.R
import java.text.SimpleDateFormat
import java.util.Locale

class CommonHandler {
    private lateinit var dialog: BottomSheetDialog
    private lateinit var pickMedia: ActivityResultLauncher<Intent>
    private lateinit var imageUri: Uri
    private lateinit var onImageSelected: ((Uri) -> Unit)
    private val dialogMap = HashMap<View, BottomSheetDialog>()

    fun spinnerHandler(items: Array<String>, binding: AutoCompleteTextView, context: Context) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        val autoCompleteTextView: AutoCompleteTextView = binding
        autoCompleteTextView.setAdapter(adapter)
    }

    fun imageCallback(activityResultRegistry: ActivityResultRegistry) {
        pickMedia = activityResultRegistry.register(
            "key",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data!!
                onImageSelected.invoke(imageUri)
            }
        }
    }

    fun imageSelect(onImageSelected: (Uri) -> Unit) {
        this.onImageSelected = onImageSelected
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickMedia.launch(galleryIntent)
    }

    fun dateHandler(firstDate: Array<String>, secondDate: AutoCompleteTextView, context: Context) {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    }

    fun showDialog(view: View, context: Context) {
        dialog = BottomSheetDialog(context)

        // viewGroup 삭제
        if (view.parent != null) (view.parent as ViewGroup).removeView(view)

        dialog.setContentView(view)
        dialog.show()

        dialogMap[view] = dialog
    }

    fun dismissDialog(view: View) {
        dialogMap[view]?.dismiss()
    }

    // 디버깅용 토스트메시지
    fun toastMessage(message: String) {
        Toast.makeText(TravelAgencyApplication.getTravelApplication(), message, Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * 알림 팝업
     * commonHandler.alertDialog(context, 모드) { (yes선택시 처리해야할 함수) }
     *
     * 주의 : 객체 생성 후 꼭 닫아줄 것
     *
     * 모드
     * 0. default - 에러
     * 1. "delete" - 삭제
     * 2. "update" - 수정
     * 3. "create" - 생성
     * 4. "warning" - 주의 - (화면전환시 데이터 날라감 경고)
     *
     * 화면 전환 경고 예시 : 패키지 등록 Toolbar의 Navigation Icon 클릭 시 뒤로 가기 동작 설정
     * binding.toolbar.setNavigationOnClickListener {
     *     // 작성 중인 글이 있을때(기존 데이터에서 변동이 있을때)
     *     val alertDialog = commonHandler.alertDialog(requireContext(), "warning") {
     *         findNavController().popBackStack()  // yes클릭시 해야할 작업(화면전환)
     *     }
     *     alertDialog.dismiss()
     * }
     *
     * 삭제 예시 : 리사이클러뷰 어댑터에게서 넘겨받은 position을 가지고 프래그먼트의 onLongClick에서 deleteTodo함수(db실제 삭제) 실행함
     * overide fun onLongClick(position: Int) {
     *     val alertDialog = commonHandler.alertDialog(requireContext(), "delete") {
     *         delTodo(position) // yes클릭시 해야할 작업(db삭제)
     *     }
     *     alertDialog.dismiss()
     * }
     *
     * 삭제 완료 예시 : db에 삭제 작업 완료 후 삭제되었습니다 alert 띄운다.
     * val alertDialog = commonHandler.alertDialog(requireContext(), "delete") { }
     * alertDialog.dismiss()
     *
     * 수정 완료 예시 : db에 수정 작업 완료 후 수정되었습니다 alert 띄운다.
     * val alertDialog = commonHandler.alertDialog(requireContext(), "update") { }
     * alertDialog.dismiss()
     *
     * 등록 완료 예시 : db에 등록 작업 완료 후 등록되었습니다 alert 띄운다.
     * val alertDialog = commonHandler.alertDialog(requireContext(), "create") { }
     * alertDialog.dismiss()
     *
     * */
    fun alertDialog(context: Context, mode: String, todo: () -> Unit): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)


        // 기본값
        var alertTitleResId = R.string.alert_title_err
        var alertMsgResId = R.string.alert_message_try_again

        when (mode) {
            "delete" -> {
                alertTitleResId = R.string.alert_title_del
                alertMsgResId = R.string.alert_message_del
                if (todo != null) {
                    builder.setNegativeButton(R.string.alert_no, null)  // 아무 작업 해주지 않으면 null
                    builder.setPositiveButton(R.string.alert_yes) { _, _ ->  // 클릭됐을때 작동할 것을 람다 표현식으로 바로 전달
                        todo.invoke()  // Yes 클릭시 db삭제할 함수
                    }
                } else {
                    // delTodo없는 경우 삭제완료되었습니다 띄움
                    alertMsgResId = R.string.alert_message_del_done
                }
            }

            "update" -> {
                alertTitleResId = R.string.alert_title_update
                alertMsgResId = R.string.alert_message_update
            }

            "create" -> {
                alertTitleResId = R.string.alert_title_create
                alertMsgResId = R.string.alert_message_create
            }

            "warning" -> {
                alertTitleResId = R.string.alert_title_warning
                alertMsgResId = R.string.alert_message_waring
                builder.setNegativeButton(R.string.alert_no, null)
                builder.setPositiveButton(R.string.alert_yes) { _, _ ->  // 클릭됐을때 작동할 것을 람다 표현식으로 바로 전달
                    todo.invoke()  // Yes 클릭시 화면 뒤로가기 함수
                }
            }
        }

        val alertTitle = context.resources.getString(alertTitleResId)
        val alertMsg = context.resources.getString(alertMsgResId)

        builder.setTitle(alertTitle)
        builder.setMessage(alertMsg)

        val alertDialog = builder.create()  // AlertDialog 객체 생성
        alertDialog.show()  // 화면에 표시

        return alertDialog  // 생성된 AlertDialog 객체 반환. 한번 사용 후 dismiss 닫아주기 위함
    }

}