package pl.projektBsk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.projektBsk.exception.NoRateException;
import pl.projektBsk.exception.NoUserOrGameException;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.rating.Rating;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

@Service
public class RateService {

    private UserRepository userRepository;
    private GameRepository gameRepository;

    @Autowired
    public RateService(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    //Dodanie oceny gry przez użytkownika
    @Transactional
    public boolean addRate(Double rate, Long gameid){
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userRepository.findByUsername(auth.getName());
            Optional<Game> game = gameRepository.findById(gameid);

            if (user.isPresent() && game.isPresent()) {
                Rating userRate = new Rating(rate, game.get());
                List<Rating> rates = user.get().getRate();
                rates.add(userRate);
                rates = game.get().getRate();
                rates.add(userRate);
                game.get().setRate(rates);
                return true;
            }
            throw new NoUserOrGameException();
        }
        catch (NoUserOrGameException e) {
            return false;
        }
    }

    //Sprawdzenie czy użytkownik już ocenił grę
    public boolean rateIsExist(Long gameId){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userRepository.findByUsername(auth.getName());
            List<Rating> rates = user.get().getRate();
            Optional<Game> game = gameRepository.findById(gameId);
            if( rates.stream().map(Rating::getGame).anyMatch(Predicate.isEqual(game.get()))) {
                return true;
            }
            else {
                throw new NoRateException();
            }
        } catch (NoRateException e) {
            return false;
        }
    }

    //Edycja oceny gry użytkownika
    @Transactional
    public void editRate(Double rate, Long gameId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Game> game = gameRepository.findById(gameId);
        List<Rating> rates = user.get().getRate();
      Rating rating =rates.stream()
              .filter(r->r.getGame().equals(game.get()))
              .findFirst()
              .get();
        int indexUser = rates.indexOf(rating);
        List<Rating> gameRating = game.get().getRate();
        int indexGame = gameRating.indexOf(rating);
        rating.setRate(rate);
        rates.set(indexUser,rating);
        gameRating.set(indexGame,rating);
    }
}
