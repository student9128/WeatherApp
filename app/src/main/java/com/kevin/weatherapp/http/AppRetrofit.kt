package com.kevin.weatherapp.http

import com.kevin.playandroid.util.LogUtils
import com.kevin.playandroid.util.SPUtils
import com.kevin.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Kevin on 2021/3/19<br/>
 *
 * Blog:http://student9128.top/
 *
 * 公众号：前线开发者Kevin
 *
 * Describe:<br/>
 */
class AppRetrofit {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val CONNECT_TIME_OUT = 30L
        const val READ_TIME_OUT = 60L

        val appRetrofit: AppRetrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppRetrofit()
        }
    }

    private lateinit var retrofit: Retrofit

    constructor() {
        try {
            retrofit = Retrofit.Builder()
                .client(initBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addCallAdapterFactory()
                .baseUrl(BASE_URL)
                .build()
        }catch (e:Exception){

        }
    }

    fun getHttpService(httpService: (HttpService) -> Unit): HttpService {
        return appRetrofit.create(HttpService::class.java)
    }

    fun getHttpService(): HttpService {
        return appRetrofit.create(HttpService::class.java)
    }

    private fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    /**
     * init okhttp client for retrofit
     */
    private fun initBuilder(): OkHttpClient.Builder {
        var interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        var builder = OkHttpClient.Builder()
//        builder.sslSocketFactory(
//            SSLSocketFactoryUtils.createSSLSocketFactory(),
//            SSLSocketFactoryUtils.createTrustAllManager()
//        )
//            .hostnameVerifier(SSLSocketFactoryUtils.TrustAllHostnameVerifier())
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor)
        }
        builder.retryOnConnectionFailure(true)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
//            .addInterceptor(SaveCookieInterceptor())
//            .addInterceptor(RequestCookiesInterceptor())
        return builder
    }

//    class SaveCookieInterceptor : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val request = chain.request()
//            val response = chain.proceed(request)
//            val requestUrl = request.url().toString()
//            val domain = request.url().host()
//            val headers = response.headers(Constants.KEY_SET_COOKIE)
//            if (headers.size > 0) {
//                headers.map { t ->
//                    LogUtils.printI("AppRetrofit", "item=$t")
//                }
//            }
//            LogUtils.printD("AppRetrofit", "requestUrl======》》》》》》=====$requestUrl")
//            val x =
//                (requestUrl.contains(Constants.KEY_LOGIN) || requestUrl.contains(Constants.KEY_REGISTER))
//
//            val notEmpty = response.headers(Constants.KEY_SET_COOKIE).isNotEmpty()
//            if ((requestUrl.contains(Constants.KEY_LOGIN) || requestUrl.contains(Constants.KEY_REGISTER)) && notEmpty
//            ) {
//                val cookies = response.headers(Constants.KEY_SET_COOKIE)
//                val c = encodeCookie(cookies)
//                saveCookie(requestUrl, domain, c)
//            }
//            if (requestUrl.contains(Constants.KEY_LOGOUT) && domain.isNotEmpty()) {
//                SPUtils.save(domain, "")
//            }
//            return response
//        }

        private fun encodeCookie(cookies: List<String>): String {
            val sb = StringBuilder()
            val set = HashSet<String>()
            cookies.map { cookie -> cookie.split(";").toTypedArray() }
                .forEach { c ->
                    c.filterNot { s ->
                        set.contains(s)
                    }
                        .forEach { set.add(it) }
                }
            val iterator = set.iterator()
            while (iterator.hasNext()) {
                val cookie = iterator.next()
                sb.append(cookie).append(";")
            }
            val lastIndexOf = sb.lastIndexOf(";")
            if (sb.length - 1 == lastIndexOf) {
                sb.deleteCharAt(lastIndexOf)
            }
            return sb.toString()
        }

        private fun saveCookie(url: String, domain: String, cookies: String) {
            if (url.isNotEmpty()) {
                SPUtils.save(url, cookies)
            }
            if (domain.isNotEmpty()) {
                SPUtils.save(domain, cookies)
            }
        }

    }

//    class RequestCookiesInterceptor : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val request = chain.request()
//            val builder = request.newBuilder()
//            val domain = request.url().host()
////            LogUtils.printD("有吗？？？？","domain=$domain")
//            val cookie = getCookie(domain)
//            LogUtils.printD("有吗？？？？", "cookie=$cookie")
//            if (cookie.isNotEmpty()) {
//                builder.addHeader("Cookie", cookie)
//            }
//            return chain.proceed(builder.build())
//        }
//
//        private fun getCookie(domain: String): String {
//            return if (domain.isNotEmpty()) {
//                SPUtils.getString(domain)
//            } else {
//                ""
//            }
//        }
//
//    }