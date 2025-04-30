package no.uio.ifi.in2000.vaeraktiv

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.vaeraktiv.di.ViewModelModule
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MainActivityUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var context: Context
    private lateinit var permissionManager: PermissionManager
    private lateinit var activity: MainActivity
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    @Before
    fun setup() = run {
        permissionManager = mock(PermissionManager::class.java)
        context = mock(Context::class.java)
        homeScreenViewModel = mock(HomeScreenViewModel::class.java)
        activity = MainActivity()
        activity.homeScreenViewModel = homeScreenViewModel
        Dispatchers.setMain(testDispatcher)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when permission is granted then start tracking is called`() = runTest {
        when(permissionManager)
    }
}