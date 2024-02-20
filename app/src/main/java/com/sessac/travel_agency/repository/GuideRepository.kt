package com.sessac.travel_agency.repository

import com.sessac.travel_agency.common.TravelAgencyApplication
import com.sessac.travel_agency.data.GuideItem
import com.sessac.travel_agency.database.AppDatabase

class GuideRepository {

    private val guideDao = AppDatabase.getDatabase(TravelAgencyApplication.getTravelApplication()).guideDao()

    fun insertGuide(newGuide: GuideItem) {
        guideDao.insertGuide(newGuide)
    }

    fun updateGuide(updateGuide: GuideItem) {
        guideDao.updateGuide(updateGuide)
    }

    fun deleteGuide(id: Int) {
        guideDao.deleteGuide(id)
    }

    fun findAllGuides(): List<GuideItem> {
        return guideDao.getAllGuideList()
    }

//    private val db = FirebaseFirestore.getInstance()
//    private val guideCollection = db.collection("GuideItemFireStore")
//
////    fun findAllGuides(): Task<QuerySnapshot> {
//    suspend fun findAllGuides(): List<GuideItemFireStore>{
//        return guideCollection.get().await().toObjects(GuideItemFireStore::class.java)
//    }
//
//    fun insertGuide(guideItem: GuideItemFireStore){
//        guideCollection.document(guideItem.guideId.toString()).set(guideItem)
//    }
//
//    fun deleteGuide(guideId: Int){
//        guideCollection.document(guideId.toString()).delete()
//    }
//
//    fun updateGuide(guideItem: GuideItemFireStore){
//        guideCollection.document(guideItem.guideId.toString()).set(guideItem, SetOptions.merge())
//    }
}