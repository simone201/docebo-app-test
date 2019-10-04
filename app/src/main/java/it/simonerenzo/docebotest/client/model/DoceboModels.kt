package it.simonerenzo.docebotest.client.model

import com.google.gson.annotations.SerializedName

object DoceboModels {

    data class APIResponse(
        val data: APIData,
        val version: String,
        @SerializedName("extra_data") val extraData: APIExtraData,
        val _links: List<Any>?
    )

    data class APIData(
        val items: List<Item>,
        val count: Int,
        @SerializedName("has_more_data") val hasMoreData: Boolean,
        val cursor: Any?,
        @SerializedName("current_page") val currentPage: Int,
        @SerializedName("current_page_size") val currentPageSize: Int,
        @SerializedName("total_page_count") val totalPageCount: Int,
        @SerializedName("total_count") val totalCount: Int
    )

    data class APIExtraData(
        @SerializedName("list_type") val listType: Int,
        @SerializedName("default_sort_addr") val defSortAddr: String,
        @SerializedName("default_sort_dir") val defSortDir: String
    )

    data class Item(
        @SerializedName("item_id") val id: String,
        @SerializedName("item_type") val type: String,
        @SerializedName("item_code") val code: String,
        @SerializedName("item_name") val name: String,
        @SerializedName("item_description") val description: String,
        @SerializedName("item_category") val category: String,
        @SerializedName("item_language") val language: String,
        @SerializedName("number_of_courses") val coursesAmount: Int?,
        @SerializedName("item_price") val price: String,
        @SerializedName("item_rating_option") val ratingOpt: String,
        @SerializedName("item_rating") val rating: Int,
        @SerializedName("is_new") val isNew: Boolean,
        @SerializedName("is_user_enrolled") val isUserEnrolled: Boolean?,
        @SerializedName("item_visibility") val visibility: String,
        @SerializedName("item_policy") val policy: String,
        @SerializedName("item_can_enroll") val canEnroll: String,
        @SerializedName("is_opened") val isOpened: String,
        @SerializedName("item_type_int") val typeInt: String,
        @SerializedName("course_type") val courseType: String,
        @SerializedName("duration") val duration: String,
        @SerializedName("waiting") val isWaiting: Boolean,
        @SerializedName("item_language_label") val languageLabel: String,
        @SerializedName("item_thumbnail") val thumbnail: String,
        @SerializedName("item_slug") val slug: String,
        @SerializedName("access_status") val accessStatus: Int,
        @SerializedName("price_status") val priceStatus: Int,
        @SerializedName("is_user_subscribed") val isUserSubscribed: Int,
        @SerializedName("is_affiliate") val isAffiliate: Boolean,
        @SerializedName("partner_fields") val partnerFields: Any?
    )

}