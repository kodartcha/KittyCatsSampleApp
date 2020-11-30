package com.gabriel_codarcea.core.network.mocklogin

import com.gabriel_codarcea.core.network.BuildConfig
import com.gabriel_codarcea.core.network.BuildConfig.LOGIN_BASE_URL
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class FakeInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response?
        if (BuildConfig.DEBUG) {
            val responseString: String

            val uri: HttpUrl = chain.request().url
            val query: String = uri.toUri().query

            responseString = if (uri.toString().contains("${LOGIN_BASE_URL}auth/login")) {
                if (!uri.queryParameter("user").isNullOrEmpty() &&
                    !uri.queryParameter("password").isNullOrEmpty()
                ) {
                    if (uri.queryParameter("user") == correctUser &&
                        uri.queryParameter("password") == correctPassword
                    ) {
                        fakeLoginResponseOK
                    } else fakeLoginResponseUnauthorized
                } else throw IOException("EMPTY_CREDENTIALS")
            } else if (query.contains("${LOGIN_BASE_URL}auth/refresh")) {
                throw IOException("IS_REFRESH")
            } else throw IOException("INVALID_URL")

            if (responseString.isNotEmpty()) {
                response = Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(
                        responseString.toByteArray()
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .addHeader("content-type", "application/json")
                    .build()
            } else {
                response = Response.Builder()
                    .code(600)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(null)
                    .addHeader("content-type", "application/json")
                    .build()
            }

        } else {
            response = chain.proceed(chain.request())
        }
        return response
    }

    companion object {
        // FAKE RESPONSES.
        private const val correctUser = "test@test.com"
        private const val correctPassword = "112233"
        private const val fakeUser =
            "{\"id\":1000,\"first_name\":\"Firstname\",\"last_name\":\"Lastname\", \"email\":\"test@test.com\"}"

        private const val fakeLoginResponseOK =
            "{\"status_code\":200,\"auth_token\":\"1234567890\",\"refresh_token\":\"0987654321\", \"user\":$fakeUser}"
        private const val fakeLoginResponseUnauthorized =
            "{\"status_code\":401,\"error\":\"UNAUTHORIZED\"}"
    }
}