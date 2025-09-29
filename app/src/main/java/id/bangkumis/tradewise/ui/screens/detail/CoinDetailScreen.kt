package id.bangkumis.tradewise.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import id.bangkumis.tradewise.ui.components.loading.SequentialDotLoader
import id.bangkumis.tradewise.ui.components.chartinterval.ChartIntervalButtonGroup
import id.bangkumis.tradewise.ui.components.chartinterval.TimeIntervals
import id.bangkumis.tradewise.ui.components.linechart.CoinPriceChart
import id.bangkumis.tradewise.ui.components.linechart.CoinPriceLabelDrawer
import id.bangkumis.tradewise.ui.theme.LossRed
import id.bangkumis.tradewise.ui.theme.ProfitGreen
import me.bytebeats.views.charts.line.render.line.GradientLineShader
import me.bytebeats.views.charts.line.render.line.SolidLineDrawer
import me.bytebeats.views.charts.line.render.point.EmptyPointDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@Composable
fun CoinDetailScreen(
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collect{ event ->
            snackBarHostState.showSnackbar(message = event)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Box(Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ){
            if(state.coin != null){
                CoinDetailContent(
                    state = state,
                    onBuyClick = { viewModel.onBuySellButtonClicked("BUY") },
                    onSellClick = { viewModel.onBuySellButtonClicked("SELL") },
                    onConfirmTransaction = { viewModel.onConfirmTransaction(it) },
                    onDismissDialog = { viewModel.onDismissDialog() },
                    onIntervalSelected = { viewModel.onIntervalSelected(it) }
                )
            }
            if(state.isLoading){
                SequentialDotLoader(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            if (state.error.isNotBlank()){
                Text(
                    text = state.error,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CoinDetailContent(
    state: CoinDetailState,
    onBuyClick: () -> Unit,
    onSellClick: () -> Unit,
    onConfirmTransaction: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onIntervalSelected: (TimeIntervals) -> Unit
){
    val coin = state.coin!!
    val chartColor = if(state.isPriceUp){
        ProfitGreen
    } else {
        LossRed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        AsyncImage(
            model = coin.imageUrl,
            contentDescription = coin.name,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = coin.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$${coin.currentPriceUSD}",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
            )

        Spacer(Modifier.height(24.dp))
        state.lineChartData?.let{ chartData ->
            CoinPriceChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                lineChartData = chartData,
                animation = simpleChartAnimation(),
                pointDrawer = EmptyPointDrawer,
                lineDrawer = SolidLineDrawer(
                    thickness = 2.dp,
                    color = chartColor
                ),
                lineShader = GradientLineShader(
                    colors = listOf(
                        chartColor.copy(alpha = .4f),
                        chartColor.copy(alpha = 0f)
                    )
                ),
                labelDrawer = CoinPriceLabelDrawer(
                    labelTextColorLowest = LossRed,
                    labelTextColorHighest = ProfitGreen,
                ),
                horizontalOffset = 0f
            )
        }
        Spacer(Modifier.height(16.dp))
        ChartIntervalButtonGroup(
            modifier = Modifier.fillMaxWidth(),
            onIntervalChanged = onIntervalSelected
        )
        Spacer(modifier = Modifier.height(32.dp))

        // TO BE ADD
        // Stats (using a simplified layout)
        // You will need to create a DetailItem composable based on the forked code
        // DetailItem(title = "Market Cap", value = "...")
        // DetailItem(title = "All Time High", value = "...")
        // DetailItem(title = "All Time Low", value = "...")

        Spacer(modifier = Modifier.height(80.dp))
    }

    Box(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
        ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                Alignment.CenterHorizontally
            )
        ) {
            Button(
                onClick = onBuyClick,
                Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) { Text(
                "BUY",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                    )
                )
            }
            if (state.ownedAmount > 0.00000001){
                Button(
                    onClick = onSellClick,
                    Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        LossRed
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) { Text(
                    "SELL",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                        )
                    )
                }
            }
        }
    }
    if (state.isShowingDialog){
            TransactionDialog(
                onDismiss = onDismissDialog,
                onConfirm = onConfirmTransaction,
                assetName = state.coin.name,
                assetPrice = state.coin.currentPriceUSD,
                transactionType = state.transactionType,
                ownedAmount = state.ownedAmount
            )
        }
}