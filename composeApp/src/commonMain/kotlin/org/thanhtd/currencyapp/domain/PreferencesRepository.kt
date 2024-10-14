package org.thanhtd.currencyapp.domain

import kotlinx.coroutines.flow.Flow
import org.thanhtd.currencyapp.domain.model.Currency
import org.thanhtd.currencyapp.domain.model.CurrencyCode

interface PreferencesRepository {
    suspend fun saveLastUpdated(lastUpdated: String)
    suspend fun isDataFresh(currentTimestamp: Long): Boolean
    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun readResourceCurrencyCodec(): Flow<CurrencyCode>
    fun readTargetCurrencyCodec(): Flow<CurrencyCode>
}