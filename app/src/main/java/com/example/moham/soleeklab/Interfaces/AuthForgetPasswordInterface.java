package com.example.moham.soleeklab.Interfaces;

public interface AuthForgetPasswordInterface {

    boolean checkEmailValidation(String email);

    void instantiateViews();

    void showNoNetworkDialog();
}
