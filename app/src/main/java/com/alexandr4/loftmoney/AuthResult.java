package com.alexandr4.loftmoney;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResult extends Result {
    @Expose
    @SerializedName("auth_token")
    public String authToken;
}