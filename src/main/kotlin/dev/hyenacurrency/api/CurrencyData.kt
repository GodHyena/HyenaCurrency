package dev.hyenacurrency.api

data class CurrencyData(
    var money: Double = 0.0,
    var mobCoin: Double = 0.0,
    var gems: Double = 0.0
) {
    fun getBalance(type: CurrencyType): Double = when (type) {
        CurrencyType.MONEY -> money
        CurrencyType.MOBCOIN -> mobCoin
        CurrencyType.GEM -> gems
    }

    fun setBalance(type: CurrencyType, amount: Double) {
        when (type) {
            CurrencyType.MONEY -> money = amount
            CurrencyType.MOBCOIN -> mobCoin = amount
            CurrencyType.GEM -> gems = amount
        }
    }

    fun addBalance(type: CurrencyType, amount: Double) {
        setBalance(type, getBalance(type) + amount)
    }

    fun subtractBalance(type: CurrencyType, amount: Double) {
        setBalance(type, getBalance(type) - amount)
    }
}
