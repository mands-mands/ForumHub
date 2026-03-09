package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.dto.DadosCadastrarTopico;
import br.com.alura.forumhub.model.Curso;
import br.com.alura.forumhub.model.StatusTopico;
import br.com.alura.forumhub.model.Topico;
import br.com.alura.forumhub.model.Usuario;
import br.com.alura.forumhub.repository.CursoRepository;
import br.com.alura.forumhub.repository.TopicoRepository;
import br.com.alura.forumhub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private TopicoRepository topicoRepository;
    private CursoRepository cursoRepository;
    private UsuarioRepository usuarioRepository;

    public TopicoController(TopicoRepository topicoRepository, CursoRepository cursoRepository, UsuarioRepository usuarioRepository){
        this.topicoRepository = topicoRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DadosCadastrarTopico dadosCadastrarTopico, Authentication authentication) throws IllegalAccessException {
        if (topicoRepository.existsByTituloAndMensagem(dadosCadastrarTopico.titulo(), dadosCadastrarTopico.mensagem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um tópico com o mesmo título e mensagem.");
        } else {
            Curso curso = cursoRepository.findByNome(dadosCadastrarTopico.curso())
                    .orElseThrow(() -> new
                            IllegalAccessException("Curso não encontrado: " + dadosCadastrarTopico.curso()));

            String login = authentication.getName();
            Usuario autor = (Usuario) usuarioRepository.findByLogin(login);

            Topico topico = new Topico();
            topico.setTitulo(dadosCadastrarTopico.titulo());
            topico.setMensagem(dadosCadastrarTopico.mensagem());
            topico.setCurso(curso);
            topico.setAutor(autor);
            topico.setDataCriacao(LocalDateTime.now());
            topico.setStatus(StatusTopico.ABERTO);

            Topico salvo = topicoRepository.save(topico);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);


    }
    }
}
