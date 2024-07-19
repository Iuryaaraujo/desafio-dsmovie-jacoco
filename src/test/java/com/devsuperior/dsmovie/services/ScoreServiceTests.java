package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private Long existingMovieId, nonExistingMovieId;
	private ScoreEntity scoreEntity;
	private ScoreDTO scoreDTO;
	private UserEntity user;
	private MovieEntity movie;

	@BeforeEach
	void setup() throws Exception {
		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		scoreEntity = ScoreFactory.createScoreEntity();
		scoreDTO = ScoreFactory.createScoreDTO();
		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();

		// Simulando comportamento de teste
		// userService.authenticated()
		Mockito.when(userService.authenticated()).thenReturn(user);
		// movieRepository.findByIdd existe
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		// movieRepository.findByIdd não existe
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		// scoreRepository.saveAndFlush(score)
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		// movie = movieRepository.save(movie);
		Mockito.when(movieRepository.save(any())).thenReturn(movie);

	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {

		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {

		movie.setId(nonExistingMovieId);
		scoreEntity.setMovie(movie);
		scoreEntity.setUser(user);
		scoreEntity.setValue(4.0);
		movie.getScores().add(scoreEntity);
		scoreDTO = new ScoreDTO(scoreEntity);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = service.saveScore  (scoreDTO);
		});
	}
}
