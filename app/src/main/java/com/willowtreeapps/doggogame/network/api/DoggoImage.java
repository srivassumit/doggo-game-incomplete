package com.willowtreeapps.doggogame.network.api;

import com.google.gson.annotations.SerializedName;

/*
JSON will look like this:

{
    status: "success",
    message: "https://dog.ceo/api/img/hound-english/n02089973_1841.jpg"
}

 */
class DoggoImage {
    static final String SUCCESS = "success";

    private String status;
    private @SerializedName("message") String url;

    String getStatus() {
        return status;
    }

    String getUrl() {
        return url;
    }
}
