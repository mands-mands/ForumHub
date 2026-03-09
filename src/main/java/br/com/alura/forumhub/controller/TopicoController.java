package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.dto.DadosCadastrarTopico;
import br.com.alura.forumhub.model.Topico;
import br.com.alura.forumhub.repository.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity cadastrarTopico(@RequestBody @Valid DadosCadastrarTopico dadosCadastrarTopico){
        if (topicoRepository.existsByTituloAndMensagem(dadosCadastrarTopico.getTitulo(), form.getMensagem())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe um tópico com o mesmo título e mensagem.");
        } else {
            Topico topico = new Topico();
            topico.setTitulo(form.getTitulo());
            topico.setMensagem(form.getMensagem());
            topico.setAutor(form.getAutor());
            topico.setCurso(form.getCurso());

            // Persistência: usar save do JpaRepository
            Topico salvo = topicoRepository.save(topico);

            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);


    }
    }
}
