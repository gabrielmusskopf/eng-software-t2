package br.com.unisinos.es.t2.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticatedUser {

    private String id;
    private String name;
    private String email;
}
