package id.bangkumis.tradewise.ui.screens

sealed class Screen(val route: String){
    object DashboardScreen: Screen("dashboard_screen")
    object PortfolioScreen: Screen("portfolio_screen")
    object CoinDetailScreen: Screen("coinDetail_screen/{coinID}"){
        fun withArgs(coinID: String): String{
            return "coinDetail_screen/$coinID"
        }
    }
}