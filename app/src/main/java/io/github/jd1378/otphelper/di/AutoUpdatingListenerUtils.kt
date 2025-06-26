package io.github.jd1378.otphelper.di

import android.util.Log
import androidx.compose.runtime.Stable
import io.github.jd1378.otphelper.ModeOfOperation
import io.github.jd1378.otphelper.repository.UserSettingsRepository
import io.github.jd1378.otphelper.utils.CodeExtractor
import java.util.concurrent.CountDownLatch
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Singleton
@Stable
class AutoUpdatingListenerUtils
@Inject
constructor(private val userSettingsRepository: UserSettingsRepository) {
  companion object {
    const val TAG = "AutoUpdatingCodeExtractor"
  }

  private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
    Log.e(TAG, exception.message ?: exception.toString())
  }

  private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)
  private val latch = CountDownLatch(1)

  var codeExtractor: CodeExtractor? = null
    private set

  var isAutoDismissEnabled: Boolean = false
    private set

  var isAutoMarkAsReadEnabled: Boolean = false
    private set

  var modeOfOperation = ModeOfOperation.UNRECOGNIZED
    private set

  init {
    scope.launch {
      userSettingsRepository.userSettings.collect {
        codeExtractor =
            CodeExtractor(it.sensitivePhrasesList, it.ignoredPhrasesList, it.cleanupPhrasesList)
        isAutoDismissEnabled = it.isAutoDismissEnabled
        isAutoMarkAsReadEnabled = it.isAutoMarkAsReadEnabled
        modeOfOperation = it.modeOfOperation
        latch.countDown() // Release the latch
      }
    }
  }

  fun awaitCodeExtractor() {
    latch.await()
  }
}
