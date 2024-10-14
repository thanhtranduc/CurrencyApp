package org.thanhtd.currencyapp.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.thanhtd.currencyapp.domain.CurrencyApiService
import org.thanhtd.currencyapp.domain.PreferencesRepository
import org.thanhtd.currencyapp.domain.model.ApiResponse
import org.thanhtd.currencyapp.domain.model.Currency
import org.thanhtd.currencyapp.domain.model.CurrencyCode
import org.thanhtd.currencyapp.domain.model.RequestState

class CurrencyApiServiceImpl(private val preferences: PreferencesRepository) : CurrencyApiService {
    companion object {
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
        const val API_KEY = "cur_live_Vsve4LHuPCaUkG765iiHQUDWVBtAeY8F4THUZfdc"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
        install(DefaultRequest) {
            headers {
                append("apikey", API_KEY)
            }
        }
    }

    override suspend fun getLatestExchangeRates(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())

                val availableCurrencyCode = apiResponse.data.keys.filter {
                    CurrencyCode.entries
                        .map { code -> code.name }
                        .toSet()
                        .contains(it)
                }

                val availableCurrencies = apiResponse.data.values
                    .filter { currency -> availableCurrencyCode.contains(currency.code) }


                val lastUpdated = apiResponse.meta.lastUpdatedAt
                preferences.saveLastUpdated(lastUpdated)

                RequestState.Success(data = availableCurrencies)
            } else {
                RequestState.Error(message = "Http error code ${response.status}")
            }
        } catch (e: Exception) {
            RequestState.Error(message = e.message.toString())
        }
    }
}