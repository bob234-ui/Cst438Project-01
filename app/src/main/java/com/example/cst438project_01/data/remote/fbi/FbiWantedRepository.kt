package com.example.cst438project_01.data.remote.fbi

/**
 * This class gets wanted-person information from the FBI API.
 *
 * Make a repository inside a ViewModel, then call it inside
 * `viewModelScope.launch`:
 *
 * ```
 * private val fbiRepository = FbiWantedRepository()
 *
 * viewModelScope.launch {
 *     fbiRepository.getWantedPeople(page = 1)
 *         .onSuccess { response ->
 *             val wantedPeople = response.items
 *             Give the list to the screen.
 *         }
 *         .onFailure { error ->
 *             Show an error message.
 *         }
 * }
 * ```
 *
 * You can also search by FBI office or a person's name:
 *
 * ```
 * fbiRepository.getWantedPeople(
 *     page = 1,
 *     fieldOffice = "miami",
 *     title = "Smith"
 * )
 * ```
 *
 * Each [FbiWantedPerson] can have a name, description, case details, reward,
 * warning, FBI offices, FBI webpage link, and image link.
 *
 * The API request can succeed or fail. Use `onSuccess` to work with the data
 * and `onFailure` to handle errors, such as having no internet connection.
 */
class FbiWantedRepository(
    private val api: FbiWantedApi = FbiApiClient.api
) {
    suspend fun getWantedPeople(
        page: Int = 1,
        fieldOffice: String? = null,
        title: String? = null
    ): Result<FbiWantedResponse> = runCatching {
        require(page > 0) { "Page must be greater than zero." }

        api.getWantedPeople(
            page = page,
            fieldOffice = fieldOffice?.trim()?.ifBlank { null },
            title = title?.trim()?.ifBlank { null }
        )
    }
}
