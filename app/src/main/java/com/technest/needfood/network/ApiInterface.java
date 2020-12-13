package com.technest.needfood.network;

import com.technest.needfood.models.alat.ResponseAlat;
import com.technest.needfood.models.alat.ResponseAllAlat;
import com.technest.needfood.models.bahan.ResponseAllBahan;
import com.technest.needfood.models.bahan.ResponseBahan;
import com.technest.needfood.models.intro.LoginModel;
import com.technest.needfood.models.kategori.ResponKategori;
import com.technest.needfood.models.pesanan.ResponsePesanan;
import com.technest.needfood.models.user.ResponAdmin;
import com.technest.needfood.models.user.ResponDriver;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    // PESANAN
    @GET("datapesanan")
    Call<ResponsePesanan> getPesanan(@Header("Authorization") String authToken);

    @GET("datapesanan")
    Call<ResponsePesanan> getPesananSatus(@Header("Authorization") String authToken,
                                          @Query("status") String status);

    @FormUrlEncoded
    @PUT("datapesanan/updatestatus/{id}")
    Call<ResponsePesanan> updateSatusPesanan(@Header("Authorization") String authToken,
                                             @Path("id") String id,
                                             @Field("status") String status);


    // KATEGORI
    @GET("inventori/getkategori/")
    Call<ResponKategori> getKategori(@Header("Authorization") String authToken,
                                     @Query("kategori") String kategori);


    // BAHAN
    @GET("inventori/getbahan/kategori/{id}")
    Call<ResponseAllBahan> getAllBahan(@Header("Authorization") String authToken,
                                       @Path("id") String id);

    @GET("inventori/getbahan/{id}")
    Call<ResponseBahan> getBahan(@Header("Authorization") String authToken,
                                 @Path("id") String id);


    // ALAT
    @GET("inventori/getalat/kategori/{id}")
    Call<ResponseAllAlat> getAllAlat(@Header("Authorization") String authToken,
                                     @Path("id") String id);

    @GET("inventori/getalat/{id}")
    Call<ResponseAlat> getAlat(@Header("Authorization") String authToken,
                               @Path("id") String id);


    // LOGIN
    @FormUrlEncoded
    @POST("login/mobile")
    Call<LoginModel> setLogin(@Header("Authorization") String authToken,
                              @Field("username") String username,
                              @Field("password") String password);


    // DATA ADMIN
    @GET("mobileauth/admin/{id}")
    Call<ResponAdmin> getAdmin(@Header("Authorization") String authToken,
                               @Path("id") String id);


    // DATA DRIVER
    @GET("mobileauth/driver/{id}")
    Call<ResponDriver> getDriver(@Header("Authorization") String authToken,
                                 @Path("id") String id);


}
