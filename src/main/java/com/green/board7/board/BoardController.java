package com.green.board7.board;

import com.green.board7.board.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="게시판")
@RestController
@Validated
@RequestMapping("/board")
public class BoardController {
    private final Logger LOGGER;
    private final BoardService service;

    @Autowired
    public BoardController(BoardService service) {
        LOGGER = LoggerFactory.getLogger(BoardController.class);
        this.service = service;
    }
    @PostMapping
    @Operation(summary = "글등록", description = "<em>글 등록할 수 있습니다.</em> <br> 글 등록할 수 있습니다.")
    public int postBoard(@RequestBody BoardInsDto dto) {
        return service.insBoard(dto);
    }
    @GetMapping
    public List<BoardVo> getBoardAll(@RequestParam @Min(value = 1, message = "page값은 1이상이어야 합니다.") int page
                                    , @RequestParam(defaultValue = "30") int row) {
        LOGGER.info("page : " + page);
        BoardDto dto = new BoardDto();
        dto.setPage(page);
        dto.setRowLen(row);
        return service.selBoardAll(dto);
    }
    @GetMapping("/{iboard}")
    public BoardDetailVo getBoardById(@PathVariable int iboard) {
        BoardDto dto = new BoardDto();
        dto.setIboard(iboard);
        LOGGER.debug(dto.toString());
        return service.selBoardById(dto);
    }

    @PutMapping
    public int putBoard(@RequestBody BoardDto dto) {
        return service.updBoard(dto);
    }

    @DeleteMapping("/{iboard}")
    public int deleteBoard(@PathVariable int iboard) {
        BoardDto dto = new BoardDto();
        dto.setIboard(iboard);
        return service.delBoard(dto);
    }
}
