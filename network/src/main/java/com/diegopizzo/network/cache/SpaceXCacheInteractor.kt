package com.diegopizzo.network.cache

import com.diegopizzo.network.model.CompanyInfo
import com.diegopizzo.network.model.Launch
import com.diegopizzo.network.model.Rocket
import com.diegopizzo.network.service.ApiService
import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.MemoryPolicy
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.store.rx2.getSingle
import com.dropbox.store.rx2.ofSingle
import io.reactivex.Single
import retrofit2.Response
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class SpaceXCacheInteractor(
    private val api: ApiService,
    private val ttlCacheInMinute: Long
) : ISpaceXCacheInteractor {

    /**
     * sets the maximum time an entry can live in the cache since the last write, where "write"
     * means adding a new cache entry and replacing an existing entry with a new one.
     */
    private val cachePolicy =
        MemoryPolicy.builder<Any, Any>().setExpireAfterWrite(ttlCacheInMinute.minutes).build()

    private val companyInfoStore: Store<Any, Response<CompanyInfo>> =
        StoreBuilder.from(fetcher = Fetcher.ofSingle { api.getCompanyInfo() })
            .cachePolicy(cachePolicy)
            .build()

    private val launchesStore: Store<Any, Response<List<Launch>>> =
        StoreBuilder.from(fetcher = Fetcher.ofSingle { api.getLaunches() }).cachePolicy(cachePolicy)
            .build()

    private val rocketStore: Store<String, Response<Rocket>> =
        StoreBuilder.from(fetcher = Fetcher.ofSingle { key: String -> api.getRocketById(key) })
            .cachePolicy(cachePolicy)
            .build()


    override fun getCompanyInfo(): Single<Response<CompanyInfo>> {
        return companyInfoStore.getSingle(NO_KEY)
    }

    override fun getLaunches(): Single<Response<List<Launch>>> {
        return launchesStore.getSingle(NO_KEY)
    }

    override fun getRocketById(id: String): Single<Response<Rocket>> {
        return rocketStore.getSingle(id)
    }

    companion object {
        private const val NO_KEY = "NO_KEY"
    }
}