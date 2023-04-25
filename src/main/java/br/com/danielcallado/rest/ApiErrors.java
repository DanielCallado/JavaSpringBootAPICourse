package br.com.danielcallado.rest;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    @Getter
    private List<String> errors;

    public ApiErrors(String errors) {
        this.errors = Arrays.asList(errors);
    }
}
