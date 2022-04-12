package pl.projektBsk.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AddGameService {

    public boolean saveImg(MultipartFile img, String gameName) {
        try {
            String folder = "src\\main\\resources\\static\\img\\";
            byte[] bytes = img.getBytes();
            Path path = Paths.get(folder + img.getOriginalFilename());
            Files.write(path, bytes);
            return true;
        }catch (IOException e){
            return false;
        }
    }
}
