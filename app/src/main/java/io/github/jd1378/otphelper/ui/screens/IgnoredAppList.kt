package io.github.jd1378.otphelper.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.github.jd1378.otphelper.R
import io.github.jd1378.otphelper.ui.components.AppImage
import io.github.jd1378.otphelper.ui.components.TitleBar
import io.github.jd1378.otphelper.ui.components.getAppInfo
import io.github.jd1378.otphelper.ui.components.shortenAppLabel
import io.github.jd1378.otphelper.ui.navigation.MainDestinations

@Composable
fun IgnoredAppList(
    onNavigateToRoute: (String, Boolean, Boolean) -> Unit,
    upPress: () -> Unit,
    viewModel: IgnoredAppListViewModel
) {
  val ignoredApps = viewModel.ignoredApps.collectAsLazyPagingItems()
  val listState = rememberLazyListState()

  Scaffold(
      topBar = {
        TitleBar(
            upPress = upPress,
            text = LocalContext.current.getString(R.string.IGNORED_LIST_ROUTE),
        )
      }) { padding ->
        Column(
            Modifier.padding(padding).padding(dimensionResource(R.dimen.padding_page)),
        ) {
          Text(
              stringResource(R.string.ignored_list_description),
              modifier = Modifier.fillMaxWidth(),
              fontSize = 15.sp)

          HorizontalDivider(Modifier.padding(top = dimensionResource(R.dimen.padding_page)))

          Box(
              modifier =
                  Modifier.fillMaxSize().padding(top = dimensionResource(R.dimen.padding_medium)),
          ) {
            when (ignoredApps.loadState.refresh) {
              is LoadState.Loading ->
                  CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(40.dp))
              is LoadState.Error ->
                  Card {
                    Column(
                        Modifier.padding(vertical = 70.dp, horizontal = 20.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                      Text(stringResource(R.string.error))
                    }
                  }
              else -> {
                if (ignoredApps.itemCount == 0) {
                  Row {
                    Text(
                        stringResource(R.string.list_is_empty),
                        modifier = Modifier.fillMaxSize(),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                    )
                  }
                } else {
                  LazyColumn(
                      state = listState,
                      modifier = Modifier.fillMaxSize(),
                      horizontalAlignment = Alignment.CenterHorizontally,
                      verticalArrangement =
                          Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                  ) {
                    items(
                        count = ignoredApps.itemCount,
                        key = ignoredApps.itemKey { it.packageName },
                    ) { index ->
                      val ignoredNotif = ignoredApps[index]
                      if (ignoredNotif != null) {
                        IgnoredAppListItem(ignoredNotif.packageName, ignoredNotif.totalItems) {
                          onNavigateToRoute(
                              MainDestinations.IGNORED_APP_DETAIL_ROUTE +
                                  "/" +
                                  ignoredNotif.packageName,
                              false,
                              true)
                        }
                      }
                    }
                    item {
                      if (ignoredApps.loadState.append == LoadState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
}

@Composable
fun IgnoredAppListItem(
    packageName: String,
    totalItems: Long,
    onClick: (packageName: String) -> Unit
) {
  val context = LocalContext.current
  val appInfo = remember(packageName) { getAppInfo(context, packageName) }

  ListItem(
      modifier = Modifier.clip(MaterialTheme.shapes.large).clickable { onClick(packageName) },
      leadingContent = {
        AppImage(
            appInfo.icon,
            modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp)),
        )
      },
      headlineContent = { Text(appInfo.shortenAppLabel(40)) },
      supportingContent = {
        Text(
            text = stringResource(R.string.n_items, totalItems),
            color = MaterialTheme.colorScheme.primary,
        )
      },
      trailingContent = {
        Icon(
            painterResource(R.drawable.baseline_navigate_next_24),
            null,
        )
      },
      colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
  )
}
