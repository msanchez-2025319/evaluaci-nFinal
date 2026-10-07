
package com.kinal.authservice;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarPassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = "Prueba123";

        String hash = encoder.encode(password);

        System.out.println("PASSWORD: " + password);
        System.out.println("HASH BCRYPT: " + hash);
        System.out.println("VALIDO: " + encoder.matches(password, hash));
    }
}
