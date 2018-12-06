package com.example.moham.soleeklab.Interfaces;

public interface AuthLoginInterface {

    boolean checkEmailValidation(String email);

    boolean checkPasswordValidation(String password);

    void tryToLogin();

    void instantiateViews();

    void showNoNetworkDialog();
}