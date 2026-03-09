package br.com.alura.forumhub.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizarTopico(
        @NotBlank String titulo,
        @NotBlank String mensagem
) {}