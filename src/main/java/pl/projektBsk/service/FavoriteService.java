package pl.projektBsk.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.projektBsk.games.Game;
import pl.projektBsk.user.User;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private EntityManager entityManager;

    @Autowired
    public FavoriteService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void update(Game game, Long userId){
        Optional<User> user = Optional.ofNullable(entityManager.find(User.class,userId));
       if(user.isPresent()){
           List<Game> games = user.get().getUlubione();
           if(!games.contains(game)) {
               games.add(game);
               user.get().setUlubione(games);
           }

       }
    }

    @Transactional
    public void delete(Game game, Long userId){
        Optional<User> user = Optional.ofNullable(entityManager.find(User.class,userId));
        if(user.isPresent()){
            List<Game> games = user.get().getUlubione();
            games.remove(game);
            user.get().setUlubione(games);
        }
    }



}
