package com.bootcamp.firstExercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bootcamp.firstExercise.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class FirstExerciseApplication implements CommandLineRunner{

	private static final Logger log =LoggerFactory.getLogger(FirstExerciseApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(FirstExerciseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Andres" , "Pedro" , "Maria" , "Diego" , "Juan")
				.map(nombre -> new Usuario(nombre.toUpperCase(), null)
					)
				.doOnNext(usuario -> { 
					if(usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					System.out.println(usuario.getNombre());
				})
				.map(usuario -> {String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				});
				
		
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					
					@Override
					public void run() {
						log.info("Ha finalizado la ejecucion del observable con éxito!");
						
					}
				});
	}

}
