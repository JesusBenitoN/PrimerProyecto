package com.bootcamp.firstExercise;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bootcamp.firstExercise.models.Comentarios;
import com.bootcamp.firstExercise.models.Usuario;
import com.bootcamp.firstExercise.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class FirstExerciseApplication implements CommandLineRunner{

	private static final Logger log =LoggerFactory.getLogger(FirstExerciseApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FirstExerciseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		ejemploUsuarioComentariosFlatMap();
		
	}
	
	public void ejemploUsuarioComentariosFlatMap() {
		
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> new Usuario("John" , "Doe"));
		
		Mono<Comentarios> comentarioUsuarioMono = Mono.fromCallable(() -> {
			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("Hola Pepe, que tal");
			comentarios.addComentario("Mañana voy a la playa");
			comentarios.addComentario("Estoy tomando el curso con spring reactor");
			return comentarios;
		});
		
		usuarioMono.flatMap(u -> comentarioUsuarioMono.map(c -> new UsuarioComentarios(u, c)))
		.subscribe(uc -> log.info(uc.toString()));
		
	}
	
	public void ejemCollectList() throws Exception {
		//convertir un flux a mono
		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Andres", "Guzman"));
		usuariosList.add(new Usuario("Pedro", "Fulano"));
		usuariosList.add(new Usuario("Maria", "Fulana"));
		usuariosList.add(new Usuario("Diego", "Sultano"));
		usuariosList.add(new Usuario("Juan", "Mengano"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willis"));
		
		Flux.fromIterable(usuariosList)
		.collectList()
		//para recorrer el objeto e imprimir 1x1
		.subscribe(lista -> {
			lista.forEach(item -> log.info(item.toString()));
		});
		//se imprime todo en un objeto
		//.subscribe(usuario -> log.info(usuario.toString()));
	}
	
	public void ejemploToString() throws Exception {
		
		List<Usuario> usuariosList = new ArrayList<>();
		usuariosList.add(new Usuario("Andres", "Guzman"));
		usuariosList.add(new Usuario("Pedro", "Fulano"));
		usuariosList.add(new Usuario("Maria", "Fulana"));
		usuariosList.add(new Usuario("Diego", "Sultano"));
		usuariosList.add(new Usuario("Juan", "Mengano"));
		usuariosList.add(new Usuario("Bruce", "Lee"));
		usuariosList.add(new Usuario("Bruce", "Willis"));
		
		Flux.fromIterable(usuariosList)
				.map(usuario -> usuario.getNombre().toUpperCase().concat(usuario.getApellido().toUpperCase()))
				.flatMap(nombre -> {
					if (nombre.contains("bruce")) {
						return Mono.just(nombre);
					} else {
						//log.info("No coincide ".concat(usuario.getNombre()).concat(" a: bruc"));
						return Mono.empty();
					}
				})
				.map(nombre -> nombre.toLowerCase()
				)
				.subscribe(u -> log.info(u.toString())
				);
	}
	
	public void ejemploIFlatMap() throws Exception {
		
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Maria Fulana");
		usuariosList.add("Diego Sultano");
		usuariosList.add("Juan Mengano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		Flux.fromIterable(usuariosList)
				.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {
					if (usuario.getNombre().equalsIgnoreCase("bruc")) {
						return Mono.just(usuario);
					} else {
						//log.info("No coincide ".concat(usuario.getNombre()).concat(" a: bruc"));
						return Mono.empty();
					}
				})
				.map(usuario -> {String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				})
				.subscribe(u -> log.info(u.toString())
				);
	}
	
	public void ejemploIterable() throws Exception {
		
		List<String> usuariosList = new ArrayList<>();
		usuariosList.add("Andres Guzman");
		usuariosList.add("Pedro Fulano");
		usuariosList.add("Maria Fulana");
		usuariosList.add("Diego Sultano");
		usuariosList.add("Juan Mengano");
		usuariosList.add("Bruce Lee");
		usuariosList.add("Bruce Willis");
		
		Flux<String> nombres = Flux.fromIterable(usuariosList);
				/*Flux.just("Andres Guzman" , "Pedro Fulano" , "Maria Fulana" , "Diego Sultano" , "Juan Mengano" , "Bruce Lee" , "Bruce Willis");*/
				
		Flux<Usuario> usuarios= nombres.map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter(usuario -> usuario.getNombre().toLowerCase().equals("bruce"))
				.doOnNext(usuario -> { 
					if(usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre().concat(" ").concat(usuario.getApellido()));
				})
				.map(usuario -> {String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});
		
		usuarios.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con éxito!");
						
					}
				});
	}

}
