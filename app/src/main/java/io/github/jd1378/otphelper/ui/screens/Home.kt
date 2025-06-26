package io.github.jd1378.otphelper.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.jd1378.otphelper.ModeOfOperation
import io.github.jd1378.otphelper.R
import io.github.jd1378.otphelper.ui.navigation.MainDestinations

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Home(
    onNavigateToRoute: (String, Boolean, Boolean) -> Unit,
    modifier: Modifier,
    viewModel: HomeViewModel
) {
  val userSettings by viewModel.userSettings.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LaunchedEffect(userSettings) {
    if (!userSettings.isSetupFinished) {
      onNavigateToRoute(MainDestinations.MODE_ROUTE + "?setup=true", true, true)
    }
  }

  Scaffold(
      modifier = modifier,
  ) { padding ->
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
      Column(
          Modifier.width(IntrinsicSize.Max).widthIn(min = 200.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateToRoute(MainDestinations.MODE_ROUTE, true, false) }) {
              Text(text = stringResource(R.string.MODE_ROUTE))
            }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateToRoute(MainDestinations.PERMISSIONS_ROUTE, true, true) }) {
              Text(text = stringResource(R.string.PERMISSION_ROUTE))
            }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateToRoute(MainDestinations.IGNORED_APP_LIST_ROUTE, true, true) }) {
              Text(text = stringResource(R.string.ignored_list))
            }
        if (!userSettings.isHistoryDisabled) {
          Button(
              modifier = Modifier.fillMaxWidth(),
              onClick = { onNavigateToRoute(MainDestinations.HISTORY_ROUTE, true, true) }) {
                Text(text = stringResource(R.string.history))
              }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigateToRoute(MainDestinations.SETTINGS_ROUTE, true, true) }) {
              Text(text = stringResource(R.string.settings))
            }
        if (userSettings.modeOfOperation == ModeOfOperation.Notification) {
          Button(
              modifier = Modifier.fillMaxWidth(),
              onClick = { viewModel.onSendTestNotifPressed(context) },
          ) {
            Text(text = stringResource(R.string.send_test_notification))
          }
        }
      }
    }
  }
}
