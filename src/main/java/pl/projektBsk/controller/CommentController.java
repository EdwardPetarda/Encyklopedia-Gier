package pl.projektBsk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.projektBsk.service.CommentService;
import pl.projektBsk.service.GameService;

@Controller
public class CommentController {

    private CommentService commentService;
    private GameService gameService;

    @Autowired
    public CommentController(CommentService commentService, GameService gameService) {
        this.commentService = commentService;
        this.gameService = gameService;
    }

    @PostMapping("/comment/add/{id}")
    private String addComment(@PathVariable Long id, @RequestParam String content,Model model){

        if(commentService.addComment(content,id)) {
            return "redirect:/game/{id}";
        }
        model.addAttribute("message","Probowales dodac komentarz do gry, ktora nie istnieje");
        return "errorView";
    }

    @GetMapping("/comment/delete")
    private String deleteComment(@RequestParam Long gameId, @RequestParam Long commentId, Model model){
        if(gameService.exist(gameId)){
            if (commentService.deleteComment(commentId,gameId)) {
                return "redirect:/game/" + gameId;
            }
            else {
                model.addAttribute("message", "Probowales usunac komentarz, ktory nie istnieje");
                return "errorView";
            }

        }else
            model.addAttribute("message","Probowales usunac komentarz, ktory nie istnieje");
            return "errorView";
    }

    @PostMapping("/comment/edit/{gameId}/{commentId}")
    private String editComment(@PathVariable Long gameId, @PathVariable Long commentId, @RequestParam String content, Model model){
        if(gameService.exist(gameId)){
            if(commentService.editComment(commentId,gameId,content)) {
                return "redirect:/game/" + gameId;
            }
            else {
                model.addAttribute("message", "Komentarz, ktory chciales edytowac nie istnieje");
                return "errorView";
            }
        }
        model.addAttribute("message","Komentarz, ktory chciales edytowac nie istnieje");
        return "errorView";
    }

    @PostMapping("/comFavorite/add/{id}")
    private String addCommentFavorite(@PathVariable Long id, @RequestParam String content,Model model){

        if(commentService.addComment(content,id)) {
            return "redirect:/ulubione/{id}";
        }
        model.addAttribute("message","Probowales dodac komentarz do gry, ktora nie istnieje");
        return "errorView";
    }

    @GetMapping("/comFavorite/delete")
    private String deleteCommentFavorite(@RequestParam Long gameId, @RequestParam Long commentId, Model model){
        if(gameService.exist(gameId)){
            if (commentService.deleteComment(commentId,gameId)) {
                return "redirect:/ulubione/" + gameId;
            }
            else {
                model.addAttribute("message", "Probowales usunac komentarz, ktory nie istnieje");
                return "errorView";
            }

        }else
            model.addAttribute("message","Probowales usunac komentarz, ktory nie istnieje");
        return "errorView";
    }

    @PostMapping("/comFavorite/edit/{gameId}/{commentId}")
    private String editCommentFavorite(@PathVariable Long gameId, @PathVariable Long commentId, @RequestParam String content, Model model){
        if(gameService.exist(gameId)){
            if(commentService.editComment(commentId,gameId,content)) {
                return "redirect:/ulubione/" + gameId;
            }
            else {
                model.addAttribute("message", "Komentarz, ktory chciales edytowac nie istnieje");
                return "errorView";
            }
        }
        model.addAttribute("message","Komentarz, ktory chciales edytowac nie istnieje");
        return "errorView";
    }

    @PostMapping("/comRate/add/{id}")
    private String addCommentRate(@PathVariable Long id, @RequestParam String content,Model model){

        if(commentService.addComment(content,id)) {
            return "redirect:/ratings/{id}";
        }
        model.addAttribute("message","Probowales dodac komentarz do gry, ktora nie istnieje");
        return "errorView";
    }

    @GetMapping("/comRate/delete")
    private String deleteCommentRate(@RequestParam Long gameId, @RequestParam Long commentId, Model model){
        if(gameService.exist(gameId)){
            if (commentService.deleteComment(commentId,gameId)) {
                return "redirect:/ratings/" + gameId;
            }
            else {
                model.addAttribute("message", "Probowales usunac komentarz, ktory nie istnieje");
                return "errorView";
            }

        }else
            model.addAttribute("message","Probowales usunac komentarz, ktory nie istnieje");
        return "errorView";
    }

    @PostMapping("/comRate/edit/{gameId}/{commentId}")
    private String editCommentRate(@PathVariable Long gameId, @PathVariable Long commentId, @RequestParam String content, Model model){
        if(gameService.exist(gameId)){
            if(commentService.editComment(commentId,gameId,content)) {
                return "redirect:/ratings/" + gameId;
            }
            else {
                model.addAttribute("message", "Komentarz, ktory chciales edytowac nie istnieje");
                return "errorView";
            }
        }
        model.addAttribute("message","Komentarz, ktory chciales edytowac nie istnieje");
        return "errorView";
    }

    

}
