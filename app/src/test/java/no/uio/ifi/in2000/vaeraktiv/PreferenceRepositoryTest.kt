package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.data.preferences.PreferenceDataSource
import no.uio.ifi.in2000.vaeraktiv.data.preferences.PreferenceRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class PreferenceRepositoryTest {

    private lateinit var dataSource: PreferenceDataSource
    private lateinit var repository: PreferenceRepository

    @Before
    fun setup() {
        dataSource = mock()
        repository = PreferenceRepository(dataSource)
    }

    @Test
    fun `getEnabledPreferences returns formatted string when non-empty`() {
        val prefs = listOf(Preference("A", true), Preference("B", true))
        whenever(dataSource.getEnabledPreferences()).thenReturn(prefs)

        val output = repository.getEnabledPreferences()

        assertTrue(output.contains("Following are the user's preferences"))
        assertTrue(output.contains("A"))
        assertTrue(output.contains("B"))
        assertTrue(output.contains("Aktiviteter som koster penger"))
    }

    @Test
    fun `getEnabledPreferences returns empty when no enabled`() {
        whenever(dataSource.getEnabledPreferences()).thenReturn(emptyList())

        val output = repository.getEnabledPreferences()

        assertEquals("", output)
    }
}