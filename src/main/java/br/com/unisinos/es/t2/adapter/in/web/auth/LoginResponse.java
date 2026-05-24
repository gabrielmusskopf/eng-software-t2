package br.com.unisinos.es.t2.adapter.in.web.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class LoginResponse {
    private String jwt;
}
