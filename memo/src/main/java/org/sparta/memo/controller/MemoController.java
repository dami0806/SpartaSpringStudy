package org.sparta.memo.controller;

import org.sparta.memo.dto.MemoRequestDto;
import org.sparta.memo.dto.MemoResponseDto;
import org.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;
//
//@RestController
//@RequestMapping("/api")
//public class MemoController {
//    private final Map<Long, Memo> memoList = new HashMap<>();
//
//    //Post /api/memos  return: MemoResponseDto
//    @PostMapping("/memos")
//    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
//
//        //RequestDto -> Entity
//        Memo memo = new Memo(requestDto);
//
//        //Memo Max ID Check
//        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
//        memo.setId(maxId);
//        // DB 저장
//        memoList.put(memo.getId(), memo);
//
//        //Entity -> ResponseDto
//        MemoRequestDto responseDto = new MemoRequestDto();
//
//        return null;
//    }
//
//    //Get /api/memos   return : List<MemoResponseDto>
//    @GetMapping("/memos")
//    public List<MemoResponseDto> getMemos() {
//        // Map -> List
//        List<MemoResponseDto> responseList = memoList.values().stream()
//                .map(MemoResponseDto::new).toList();
//        return responseList;
//        // <MemoResponseDto>
//    }
//
//    //Put /api/memos/{id}   return: Long
//    @PutMapping("/memos/{id}")
//    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
//
//        //해당 메모가 DB에 존재하는지 확인
//        if (memoList.containsKey(id)) {
//            //해당 메모 가져오기
//            Memo memo = memoList.get(id);
//
//            memo.update(requestDto);
//            return memo.getId();
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
//        }
//    }
//
//    //Delete /api/memos/{id}   return: Long
//    @DeleteMapping("/memos/{id}")
//    public Long deleteMemo(@PathVariable Long id) {
//        // 해당 메모가 DB에 존재하는지 확인
//        if (memoList.containsKey(id)) {
//            // 해당 메모를 삭제하기
//            memoList.remove(id);
//            return id;
//        } else {
//            throw new IllegalArgumentException("선택한 메모는 삭제할 수 없습니다.");
//        }
//    }
//}
//
//

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    // JdbcTemplate 인스턴스 주입. 데이터베이스 작업을 위해 사용됨.
    private final JdbcTemplate jdbcTemplate;

    public MemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // 요청 DTO로부터 Memo 엔티티 생성
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장 // 삽입된 행의 ID를 얻기 위한 KeyHolder
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체
        // SQL 쿼리 준비
        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";

        // 데이터베이스에 새 메모 삽입
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();

        // 생성된 키(ID)를 메모에 설정
        memo.setId(id);

        // Entity -> ResponseDto
        // 생성된 메모를 Response DTO로 변환하여 반환
        return new MemoResponseDto(memo);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if(memo != null) {
            // memo 내용 수정
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if(memo != null) {
            // memo 삭제
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }
}