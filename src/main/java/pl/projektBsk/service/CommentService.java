package pl.projektBsk.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.projektBsk.comment.Comment;
import pl.projektBsk.comment.CommentRepository;
import pl.projektBsk.exception.NoUserOrGameException;
import pl.projektBsk.games.Game;
import pl.projektBsk.games.GameRepository;
import pl.projektBsk.user.User;
import pl.projektBsk.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private UserRepository userRepository;
    private GameRepository gameRepository;
    private CommentRepository commentRepository;

    @Autowired
    public CommentService(UserRepository userRepository, GameRepository gameRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional
    public boolean addComment(String content, Long gameId){
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> user = userRepository.findByUsername(auth.getName());
            Optional<Game> game = gameRepository.findById(gameId);
            Comment comment = new Comment(content, user.get());

            if (user.isPresent() && game.isPresent()) {
                List<Comment> comments = user.get().getComments();
                comments.add(comment);
                user.get().setComments(comments);
                comments = game.get().getComments();
                comments.add(comment);
                game.get().setComments(comments);
                return true;
            }
            throw new NoUserOrGameException();
        }
        catch (NoUserOrGameException e) {
            return false;
        }
    }

    @Transactional
    public boolean deleteComment(Long commentId, Long gameId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Game> game = gameRepository.findById(gameId);
        if(user.isPresent() && comment.isPresent()){
            List<Comment> comments = user.get().getComments();
            comments.remove(comment.get());
            user.get().setComments(comments);
            comments = game.get().getComments();
            comments.remove(comment.get());
            game.get().setComments(comments);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean editComment(Long commentId, Long gameId,String content){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Game> game = gameRepository.findById(gameId);
        if(user.isPresent() && comment.isPresent()){
            Comment temp = comment.get();
            comment.get().setContent(content);
            List<Comment> comments = user.get().getComments();
            int i = comments.indexOf(temp);
            comments.set(i,comment.get());
            user.get().setComments(comments);
            comments = game.get().getComments();
            i = comments.indexOf(temp);
            comments.set(i,comment.get());
            game.get().setComments(comments);
            return true;
        }
        return false;
    }
}
