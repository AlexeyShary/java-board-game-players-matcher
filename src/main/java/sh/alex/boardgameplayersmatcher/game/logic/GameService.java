package sh.alex.boardgameplayersmatcher.game.logic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sh.alex.boardgameplayersmatcher.game.data.Game;
import sh.alex.boardgameplayersmatcher.game.data.GameRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Game getById(long id) {
        return gameRepository.findById(id).orElseThrow();
    }
}