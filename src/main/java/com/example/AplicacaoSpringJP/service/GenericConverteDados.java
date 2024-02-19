package com.example.AplicacaoSpringJP.service;

public interface GenericConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
