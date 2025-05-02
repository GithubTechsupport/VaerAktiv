package no.uio.ifi.in2000.vaeraktiv

import androidx.activity.ComponentActivity
import no.uio.ifi.in2000.vaeraktiv.utils.LocationTracker
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@RunWith(MockitoJUnitRunner::class)
class MainActivityUnitTest {

    @Mock
    private lateinit var mockLocationTracker: LocationTracker

    @Test
    fun `locationTracker start is called when permission is granted`() {

    }
}