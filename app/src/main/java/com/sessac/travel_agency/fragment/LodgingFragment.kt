package com.sessac.travel_agency.fragment

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sessac.travel_agency.adapter.LodgingAdapter
import com.sessac.travel_agency.R
import com.sessac.travel_agency.common.CommonHandler
import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.LodgingItem
import com.sessac.travel_agency.databinding.FragmentLodgingBinding
import com.sessac.travel_agency.databinding.FragmentPackageAddBinding
import com.sessac.travel_agency.helper.TravelAgencyOpenHelper

class LodgingFragment : Fragment() {

    // 지역 선택
    private lateinit var location: String
    private lateinit var binding: FragmentLodgingBinding

    // 리사이클러뷰
    private lateinit var recyclerView: RecyclerView
    private lateinit var lodgingList: ArrayList<LodgingItem>
    private lateinit var lodgingAdapter: LodgingAdapter


    private val commonHandler: CommonHandler = CommonHandler()

    private lateinit var areaList: Array<String>

    // DB
    private lateinit var dbHelper: TravelAgencyOpenHelper
    private lateinit var db: SQLiteDatabase
    private val TAG = "LodgingAddFragment"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        binding = FragmentLodgingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 지역선택 드롭다운 메뉴
        val areaText: AutoCompleteTextView = binding.dropLocations
        areaList = resources.getStringArray(R.array.select_region)
        commonHandler.spinnerHandler(areaList, areaText, requireContext())

        // 선택된지역->리사이클러뷰로 where지역 데이터가져오기
        setupLocationSpinnerHandler()

        //리사이클러뷰
        setupRecyclerviewAdapter()

        //플로팅버튼(숙소등록 프래그먼트로)
        setupFloatingButton()

        // DB
        val app = TravelAgencyApplication.getTravelApplication()
        dbHelper = app.dbHelper
        db = app.db

        return root
    }


    // 지역 선택 메뉴 사용자 클릭 이벤트 받기
    private fun setupLocationSpinnerHandler() {
        binding.dropLocations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // 선택한 지역을 변수에 저장해두기(지역 조건에 맞는 Lodging list만 db에서 받아서 리사이클러뷰에 뿌려줄 용도)
                location = parent.getItemAtPosition(position).toString()

//                when (location) {
//                    "전체" -> setupRecyclerviewAdapter()
//                    else -> null
//                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    //플로팅버튼(숙소등록)
    private fun setupFloatingButton() {
        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            insertLodgingData()
            val view: View = layoutInflater.inflate(R.layout.fragment_lodging_add, null)

            val areaText: AutoCompleteTextView = view.findViewById(R.id.drop_new_area)
            val starText: AutoCompleteTextView = view.findViewById(R.id.drop_new_rating)
            val addBtn: Button = view.findViewById(R.id.button_addLodging)

            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()

            // 지역선택, 숙소등급 선택 드롭다운 메뉴 세팅
            areaList = resources.getStringArray(R.array.select_region)

            val starList = resources.getStringArray(R.array.select_star)

            // 스피너 핸들러
            commonHandler.spinnerHandler(areaList, areaText, requireContext()) // 지역
            commonHandler.spinnerHandler(starList, starText, requireContext()) // 숙소등급

            // 등록 버튼
            addBtn.setOnClickListener {
                dialog.dismiss() // 닫기전 선택된 데이터가 데이터클래스에 담겨 insert 되게 추가할것
            }

        }
    }

    private fun setupRecyclerviewAdapter() {
        lodgingList = ArrayList()

        recyclerView = binding.lodgingfragmentRecyclerview
        recyclerView.setHasFixedSize(true) // 리사이클러뷰의 크기가 변할 일이 없음 명시. 리사이클러뷰 레이아웃 다시 잡을 필요 없이 아이템 자리만 다시 잡기
        recyclerView.layoutManager = LinearLayoutManager(activity)
        initData()

        lodgingAdapter = LodgingAdapter(lodgingList)
        recyclerView.adapter = lodgingAdapter


        // 아이템 하나 클릭되면 모달 바텀시트 띄우기 -> Intent 기존데이터 가져가서 바인딩
        lodgingAdapter.onItemClick = {

            // 프래그먼트, 뷰 가져오기
            val view: View = layoutInflater.inflate(R.layout.fragment_lodging_edit, null) //레이아웃
            val img: ImageView = view.findViewById(R.id.lodging_detailed_image) // ID
            val nameText: EditText = view.findViewById(R.id.lodging_detailed_name)
            val areaText: AutoCompleteTextView = view.findViewById(R.id.drop_detailed_area2)
            val starText: AutoCompleteTextView = view.findViewById(R.id.drop_detailed_rating)

            // 아이템의 데이터 Set
            img.setImageResource(it.lImage)
            nameText.setText(it.lName)
            areaText.setText(it.area)
            starText.setText(it.starNum.toString())

            val dialog = BottomSheetDialog(requireContext())
            dialog.setContentView(view)
            dialog.show()

            val starList = resources.getStringArray(R.array.select_star)

            // 스피너 핸들러
            commonHandler.spinnerHandler(areaList, areaText, requireContext()) // 지역
            commonHandler.spinnerHandler(starList, starText, requireContext()) // 별점

        }

    }

    // DB data 가져오기(우선 샘플데이터로)
    private fun initData() {
        val hotel1 = LodgingItem(1, "제주도", "그랜드 조선", R.drawable.hotel1, 5)
        val hotel2 = LodgingItem(2, "제주도", "신라 호텔", R.drawable.hotel2, 3)
        val hotel3 = LodgingItem(3, "서울시", "마리나베이샌즈호텔", R.drawable.hotel3, 5)
        val hotel4 = LodgingItem(4, "경상도", "공항 호텔", R.drawable.hotel4, 4)
        val hotel5 = LodgingItem(1, "경상도", "현대 호텔", R.drawable.hotel5, 2)
        val hotel6 = LodgingItem(2, "강원도", "인터컨티넨탈", R.drawable.hotel3, 3)
        val hotel7 = LodgingItem(3, "전라도", "현대민박", R.drawable.hotel4, 1)
        val hotel8 = LodgingItem(4, "충청도", "게하", R.drawable.hotel2, 4)

        lodgingList.add(hotel1)
        lodgingList.add(hotel2)
        lodgingList.add(hotel3)
        lodgingList.add(hotel4)
        lodgingList.add(hotel5)
        lodgingList.add(hotel6)
        lodgingList.add(hotel7)
        lodgingList.add(hotel8)
    }


    private fun insertLodgingData() {
        val values = ContentValues().apply {
            put("area", "제주도")
            put("lName", "그랜드 조선")
            put("lImage", R.drawable.hotel1)
            put("starNum", 4)
        }
        db.insert(TravelAgencyOpenHelper.TABLE_LODGING, null, values)
    }


    // 프래그먼트 화면을 벗어나면 기존 선택된 데이터 제거.
    override fun onPause() {
        super.onPause()

        // 기존에 선택된 지역을 제거하여 화면전환 후 돌아올시 새롭게 보이도록하기위함
        binding.dropLocations.text = null

    }

}