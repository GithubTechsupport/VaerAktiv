package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.data.places.PlacesRepository

import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class PlacesRepositoryTest {

    private lateinit var placesClient: PlacesClient
    private lateinit var repository: PlacesRepository
    private val token = AutocompleteSessionToken.newInstance()

    @Before
    fun setup() {
        placesClient = mock()
        repository = PlacesRepository(placesClient)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getAutocompletePredictions throws for blank query`() = runTest {
        repository.getAutocompletePredictions("", token)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `getAutocompletePredictions throws for null sessionToken`() = runTest {
        repository.getAutocompletePredictions("query", null)
    }

    @Test
    fun `getAutocompletePredictions returns list on success`() = runTest {
        val pred = mock<AutocompletePrediction>()
        val response = mock<FindAutocompletePredictionsResponse> {
            on { autocompletePredictions } doReturn listOf(pred)
        }
        whenever(placesClient.findAutocompletePredictions(any()))
            .thenReturn(Tasks.forResult(response))

        val result = repository.getAutocompletePredictions("q", token)

        assertEquals(1, result.size)
        assertSame(pred, result.first())
    }
}