package com.github.felipeaaa1.libraryapi.repository.specs;

import com.github.felipeaaa1.libraryapi.model.Livro;
import com.github.felipeaaa1.libraryapi.model.enums.GeneroLivro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo){
        return ((root, query, cb) ->
                cb.like(cb.upper(root.get("titulo")), "%"+ titulo.toUpperCase() +"%"));
    }

    public static Specification<Livro> generoEqual(GeneroLivro generoLivro){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("genero"), generoLivro);
    }
    public static Specification<Livro> anoPublicacaoLike(Integer ano ){
        return (root, query, cb) ->
                cb.equal(
                        cb.function("to_char",String.class, root.get("dataPublicacao"), cb.literal("YYYY")), ano.toString());
    }

    public static Specification<Livro> nomeAutorLike(String nome){
        return (root, query, cb) -> {
            Join<Object, Object> joinAutor = root.join("autor", JoinType.LEFT);
            return cb.like(cb.upper(joinAutor.get("nome")), "%"+nome.toUpperCase()+"%");
//        mesma coisa mas sem controlar o join
//        return (root, query, cb) -> cb.like(cb.upper(root.get("autor").get("nome")), "%"+nome.toUpperCase()+"%" );
        };

    }


}
