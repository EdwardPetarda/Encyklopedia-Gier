package pl.projektBsk.games;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends PagingAndSortingRepository<Game,Long> {
     Page<Game> findAllByPlatform(String platform, Pageable pageable);
    Page<Game> findAllByGenre(String genre, Pageable pageable);
      Page<Game> findByNameContains(String name,Pageable pageable);
      Page<Game> findAllByGenreAndPlatform(String genre, String platform, Pageable pageable);
      Optional<Game> findByNameAndPlatform(String name, String platform);
      void deleteById(Long id);
      boolean existsByDetails(GameDetails gameDetails);
      int countByGenre(String genre);
      int countByGenreAndPlatform(String genre, String platform);
      int countByPlatform(String platform);
      int countByNameContaining(String name);
    List<Game> findAllByImgSrc(String imgSrc);


    @Modifying
    @Transactional
    @Query(value = DELETE_FAVORITE,nativeQuery = true)
    void deleteFromFavorite(@Param("id") Long id);


    static final String DELETE_FAVORITE = "DELETE FROM Ulubione  WHERE game_id = :id ";
}
