package com.example.myapplication.Json;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {

    // Интерфейс для работы с retrofit, задающий команды-запросы для сервера, используя команду GET
    // Команды комбинируются с базовым адресом сайта (baseUrl()) и получается полный путь к странице.

    // GET-запрос для сервера, получающий информацию о музеях.
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("museums/$?l=50")
    Call<Museums> getMuseums();

    // GET-запрос для сервера, получающий информацию о выставках.
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("events/$?")
    Call<Museums> getEvents(
            @Query("f") String date,
            @Query("l") int size
    );

    // GET-запрос для сервера, получающий информацию о парках.
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("parks/$?l=50")
    Call<Museums> getParks();

    // GET-запрос с параметрами для сервера, получающий информацию о музеях
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("museums/$?")
    Call<Museums> sortMuseums(
            @Query("f") String place,
            @Query("l") int size
    );

    // GET-запрос с параметрами для сервера, получающий информацию о выставках
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("events/$?")
    Call<Museums> sortEvent(
            @Query("f") String f,
            @Query("l") int size
    );

    // GET-запрос с параметрами для сервера, получающий информацию о парках
    @Headers({"X-API-KEY: 87c12fd5776c9479f69d12876f2646a2c6c7e3fe6f4eabed37fa17c763f6ab28"})
    @GET("parks/$?")
    Call<Museums> sortParks(
            @Query("f") String place,
            @Query("l") int size
    );
}
