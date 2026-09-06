package com.example.cst438project_01.data.remote.fbi

import com.google.gson.annotations.SerializedName

/** The top-level JSON object returned by the FBI Wanted list endpoint. */
data class FbiWantedResponse(
    val total: Int = 0,
    val page: Int = 1,
    val items: List<FbiWantedPerson> = emptyList()
)

/**
 * The FBI response has many optional fields. Keep them nullable because a wanted
 * record may describe a fugitive, missing person, unknown suspect, or incident.
 */
data class FbiWantedPerson(
    val uid: String = "",
    val title: String = "",
    val description: String? = null,
    val details: String? = null,
    val sex: String? = null,
    val race: String? = null,
    val nationality: String? = null,
    val status: String? = null,
    val url: String? = null,
    val subjects: List<String> = emptyList(),
    @SerializedName("field_offices")
    val fieldOffices: List<String> = emptyList(),
    @SerializedName("reward_text")
    val rewardText: String? = null,
    @SerializedName("warning_message")
    val warningMessage: String? = null,
    val images: List<FbiImage> = emptyList()
) {
    /** A convenient image URL for list screens. */
    val displayImageUrl: String?
        get() = images.firstOrNull()?.let { it.large ?: it.original ?: it.thumb }
}

data class FbiImage(
    val caption: String? = null,
    val original: String? = null,
    val large: String? = null,
    val thumb: String? = null
)
