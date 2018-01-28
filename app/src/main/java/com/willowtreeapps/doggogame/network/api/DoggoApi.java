package com.willowtreeapps.doggogame.network.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DoggoApi {
    @GET("/api/breeds/list")
    Call<DoggoBreedResponse> getBreeds();

    @GET("/api/breed/{breedName}/images/random")
    Call<DoggoImage> getDoggoImage(@Path("breedName") String breedName);
}
