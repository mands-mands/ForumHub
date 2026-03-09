package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.dto.DadosAtualizarTopico;
import br.com.alura.forumhub.dto.DadosCadastrarTopico;
import br.com.alura.forumhub.dto.DadosDetalhamentoTopico;
import br.com.alura.forumhub.dto.DadosListagemTopico;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
                    .orElseGet(() -> {
                        Curso novo = new Curso();
                        novo.setNome(dadosCadastrarTopico.curso());
                        return cursoRepository.save(novo);
                    });

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

    @GetMapping
    public ResponseEntity<List<DadosListagemTopico>> listarTopicos() {
        var topicos = topicoRepository.findAll();

        var dto = topicos.stream()
                .map(t -> new DadosListagemTopico(
                        t.getId(),
                        t.getTitulo(),
                        t.getMensagem(),
                        t.getDataCriacao(),
                        t.getStatus(),
                        t.getAutor().getLogin(),  // devolve só o login (não expõe senha)
                        t.getCurso().getNome()
                ))
                .toList();

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalharTopico(@PathVariable Long id) {
        var topicoOpt = topicoRepository.findById(id);

        if (topicoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado.");
        }

        var t = topicoOpt.get();

        var dto = new DadosDetalhamentoTopico(
                t.getId(),
                t.getTitulo(),
                t.getMensagem(),
                t.getDataCriacao(),
                t.getStatus(),
                t.getAutor().getLogin(),
                t.getCurso().getNome()
        );

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTopico(@PathVariable Long id,
                                             @RequestBody @Valid DadosAtualizarTopico dados,
                                             Authentication authentication) {

        var topicoOpt = topicoRepository.findById(id);
        if (topicoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado.");
        }

        var topico = topicoOpt.get();

        // (opcional, mas recomendado) só o autor pode atualizar
        String login = authentication.getName();
        if (!topico.getAutor().getLogin().equals(login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas o autor pode atualizar este tópico.");
        }

        // Regra do cadastro: não permitir duplicado
        // Importante: se o usuário não mudou nada, não deve dar conflito
        boolean mudou = !topico.getTitulo().equals(dados.titulo()) || !topico.getMensagem().equals(dados.mensagem());
        if (mudou && topicoRepository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um tópico com o mesmo título e mensagem.");
        }

        topico.setTitulo(dados.titulo());
        topico.setMensagem(dados.mensagem());

        var atualizado = topicoRepository.save(topico);

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirTopico(@PathVariable Long id,
                                           Authentication authentication) {

        var topicoOpt = topicoRepository.findById(id);
        if (topicoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado.");
        }

        var topico = topicoOpt.get();

        // (recomendado) só o autor pode excluir
        String login = authentication.getName();
        if (!topico.getAutor().getLogin().equals(login)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas o autor pode excluir este tópico.");
        }

        topicoRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // 204
    }
}
