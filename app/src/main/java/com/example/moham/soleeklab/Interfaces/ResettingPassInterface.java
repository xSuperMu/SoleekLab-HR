package com.example.moham.soleeklab.Interfaces;

public interface ResettingPassInterface {

    boolean checkPasswordValidation(String password, int whichPassword);

    boolean checkPasswordMatch(String password, String reTypedPassword);

    void resetPassword();

    void instantiateViews();
}
