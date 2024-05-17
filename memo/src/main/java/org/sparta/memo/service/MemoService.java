package org.sparta.memo.service;

import org.sparta.memo.dto.MemoRequestDto;
import org.sparta.memo.dto.MemoResponseDto;
import org.sparta.memo.entity.Memo;
import org.sparta.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Service
public class MemoService {
  //  private final JdbcTemplate jdbcTemplate;
private final MemoRepository memoRepository;
@Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
        //        this.jdbcTemplate = jdbcTemplate;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        // 요청 DTO로부터 Memo 엔티티 생성
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);
        // DB 저장 // 삽입된 행의 ID를 얻기 위한 KeyHolder

        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        // 생성된 메모를 Response DTO로 변환하여 반환
        return new MemoResponseDto(memo);
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll();

    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            memoRepository.update(requestDto, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {

        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            memoRepository.delete(id);


            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

}

