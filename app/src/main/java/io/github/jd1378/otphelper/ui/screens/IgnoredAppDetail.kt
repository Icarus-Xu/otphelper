package io.github.jd1378.otphelper.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.github.jd1378.otphelper.R
import io.github.jd1378.otphelper.data.local.entity.IgnoredNotif
import io.github.jd1378.otphelper.data.local.entity.IgnoredNotifType
import io.github.jd1378.otphelper.model.getTranslation
import io.github.jd1378.otphelper.ui.components.AppInfoResult
import io.github.jd1378.otphelper.ui.components.IgnoreAppButton
import io.github.jd1378.otphelper.ui.components.IgnoreNotifIdButton
import io.github.jd1378.otphelper.ui.components.IgnoreNotifTagButton
import io.github.jd1378.otphelper.ui.components.IgnoreOriginButton
import io.github.jd1378.otphelper.ui.components.TitleBar
import io.github.jd1378.otphelper.ui.components.drawVerticalScrollbar
import io.github.jd1378.otphelper.ui.components.getAppInfo
import io.github.jd1378.otphelper.ui.components.shortenAppLabel
import io.github.jd1378.otphelper.ui.utils.SkipFirstLaunchedEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IgnoredAppDetail(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: IgnoredAppDetailViewModel
) {
  val ignoreItems = viewModel.ignoredItems.collectAsLazyPagingItems()
  val packageName = viewModel.packageName.collectAsStateWithLifecycle()
  val listState = rememberLazyListState()
  val context = LocalContext.current
  val appInfoResult = remember(packageName.value) { getAppInfo(context, packageName.value) }

  SkipFirstLaunchedEffect(ignoreItems.itemCount) {
    if (ignoreItems.itemCount == 0) {
      upPress()
    }
  }

  Scaffold(
      topBar = {
        TitleBar(
            upPress = upPress,
            text = appInfoResult.appLabel,
        )
      },
  ) { padding ->
    Column(
        modifier.padding(padding).padding(dimensionResource(R.dimen.padding_page)),
    ) {
      if (appInfoResult.failed) {
        Text(
            stringResource(R.string.app_label_not_visible_reason),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier =
                Modifier.fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_settings)),
            color = MaterialTheme.colorScheme.tertiary,
        )
      }
      Box(
          modifier =
              Modifier.fillMaxSize()
                  .padding(top = dimensionResource(R.dimen.padding_medium))
                  .drawVerticalScrollbar(listState),
      ) {
        when (ignoreItems.loadState.refresh) {
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
            if (ignoreItems.itemCount > 0) {
              LazyColumn(
                  state = listState,
                  modifier = Modifier.fillMaxSize(),
                  horizontalAlignment = Alignment.CenterHorizontally,
              ) {
                items(
                    count = ignoreItems.itemCount,
                    key = ignoreItems.itemKey { it.id },
                ) { index ->
                  val ignoredNotif = ignoreItems[index]!! // because enablePlaceholders = false

                  IgnoredAppDetailItem(
                      Modifier.animateItem(fadeOutSpec = null),
                      ignoredNotif,
                      appInfoResult,
                      addDivider = index < ignoreItems.itemCount - 1,
                  ) {
                    viewModel.removeIgnoredNotif(ignoredNotif)
                  }
                }
                item {
                  if (ignoreItems.loadState.append == LoadState.Loading) {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IgnoredAppDetailItem(
    modifier: Modifier = Modifier,
    ignoredNotif: IgnoredNotif,
    appInfoResult: AppInfoResult,
    addDivider: Boolean = false,
    onDelete: () -> Unit
) {
  Column(modifier) {
    FlowRow(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      when (ignoredNotif.type) {
        IgnoredNotifType.APPLICATION -> {

          Text(
              modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
              text = appInfoResult.shortenAppLabel(40),
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
          )
          Spacer(Modifier.padding(5.dp))

          IgnoreAppButton(true, onDelete)
        }
        else -> {
          Text(
              modifier = Modifier.align(Alignment.CenterVertically),
              text =
                  buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                      append(ignoredNotif.type.getTranslation() + ": ")
                    }
                    append(ignoredNotif.typeData)
                  },
              fontSize = 16.sp,
          )
          Spacer(Modifier.padding(5.dp))
          when (ignoredNotif.type) {
            IgnoredNotifType.NOTIFICATION_ID -> IgnoreNotifIdButton(true, onDelete)
            IgnoredNotifType.NOTIFICATION_TAG -> IgnoreNotifTagButton(true, onDelete)
            IgnoredNotifType.SMS_ORIGIN -> IgnoreOriginButton(true, onDelete)
            else -> {
              // already handled "IgnoredNotifType.APPLICATION"
            }
          }
        }
      }
    }

    if (addDivider) {
      HorizontalDivider(Modifier.padding(vertical = dimensionResource(R.dimen.padding_settings)))
    }
  }
}
