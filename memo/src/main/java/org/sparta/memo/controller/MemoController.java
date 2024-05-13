package org.sparta.memo.controller;

import org.sparta.memo.dto.MemoRequestDto;
import org.sparta.memo.dto.MemoResponseDto;
import org.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    //Post /api/memos  return: MemoResponseDto
    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {

        //RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        //Memo Max ID Check
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
        memo.setId(maxId);
        // DB 저장
        memoList.put(memo.getId(), memo);

        //Entity -> ResponseDto
        MemoRequestDto responseDto = new MemoRequestDto();

        return null;
    }

    //Get /api/memos   return : List<MemoResponseDto>
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map -> List
        List<MemoResponseDto> responseList = memoList.values().stream()
                .map(MemoResponseDto::new).toList();
        return responseList;
        // <MemoResponseDto>
    }

    //Put /api/memos/{id}   return: Long
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {

        //해당 메모가 DB에 존재하는지 확인
        if (memoList.containsKey(id)) {
            //해당 메모 가져오기
            Memo memo = memoList.get(id);

            memo.update(requestDto);
            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    //Delete /api/memos/{id}   return: Long
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        if (memoList.containsKey(id)) {
            // 해당 메모를 삭제하기
            memoList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 삭제할 수 없습니다.");
        }
    }
}


