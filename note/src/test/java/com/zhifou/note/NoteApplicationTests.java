package com.zhifou.note;

import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.note.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.io.IOException;

@SpringBootTest
class NoteApplicationTests {

    @Test
    void contextLoads() throws IOException {
        Page<NoteVO> java = new SearchService().searchNote("java", 0, 10);
    }
//653e0521fc4a0f222e5748a4ee11e2e6443cee3c
}
