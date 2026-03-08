package br.com.alura.forumhub.repository;

import br.com.alura.forumhub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Optional<Topico> existsByTituloAndMensagem(String titulo, String mensagem);
}
