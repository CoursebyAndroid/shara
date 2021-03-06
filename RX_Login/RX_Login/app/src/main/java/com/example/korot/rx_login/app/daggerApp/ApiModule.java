package com.example.korot.rx_login.app.daggerApp;

import android.app.Application;
import android.util.Config;
import com.example.korot.rx_login.app.utils.IApiServise;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class ApiModule {

    @Provides
    @AppScope
    public OkHttpClient provideOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (Config.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Provides
    @AppScope
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder
                .setLenient()
                .create();
    }

    @Provides
    @AppScope
    public Retrofit provideRestAdapter(Application application, Gson gson, OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(com.example.korot.rx_login.app.config.Config.URL_DOMAIN_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    @Provides
    @AppScope
    public IApiServise provideApiService(Retrofit restAdapter) {
        return restAdapter.create(IApiServise.class);
    }
}

