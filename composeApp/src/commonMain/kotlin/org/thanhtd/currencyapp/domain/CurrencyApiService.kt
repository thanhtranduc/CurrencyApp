package org.thanhtd.currencyapp.domain

import org.thanhtd.currencyapp.domain.model.Currency
import org.thanhtd.currencyapp.domain.model.RequestState

interface CurrencyApiService {
    suspend fun getLatestExchangeRates(): RequestState<List<Currency>>
}