package org.thanhtd.currencyapp.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.thanhtd.currencyapp.domain.MongoRepository
import org.thanhtd.currencyapp.domain.model.Currency
import org.thanhtd.currencyapp.domain.model.RequestState

class MongoImpl : MongoRepository {
    init {
        configureTheRealm()
    }

    private var realm: Realm? = null
    override fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(Currency::class)
            ).compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    override suspend fun insertCurrencyData(currency: Currency) {
        realm?.write {
            copyToRealm(currency)
        }
    }

    override fun readCurrencyData(): Flow<RequestState<List<Currency>>> {
        return realm?.query<Currency>()
            ?.asFlow()
            ?.map { result ->
                RequestState.Success(data = result.list)
            }
            ?: flow { RequestState.Error(message = "Realm not configured.") }
    }

    override suspend fun cleanUp() {
        realm?.write {
            val currencyCollection = this.query<Currency>()
            delete(currencyCollection)
        }
    }
}