package br.com.unisinos.es.t2.adapter.in.web;

import lombok.Data;

@Data
public class GetUserResponse {
    private String id;
    private String name;
    private String email;
}
