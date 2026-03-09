package br.com.alura.forumhub.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastrarTopico(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotBlank String curso
) {}