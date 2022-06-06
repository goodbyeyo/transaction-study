package study.transaction.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * memberService        @Transaction:OFF
     * memberRepository     @Transaction:ON
     * logRepository        @Transaction:ON
     */
    @Test
    void outer_transaction_off_success() {
        //given
        String username = "outer_transaction_off_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }

    /**
     * memberService        @Transaction:OFF
     * memberRepository     @Transaction:ON
     * logRepository        @Transaction:ON Exception
     */
    @Test
    void outer_transaction_off_fail() {
        //given
        String username = "로그예외_outer_transaction_off_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);



        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }

    /**
     * memberService        @Transaction:ON
     * memberRepository     @Transaction:OFF
     * logRepository        @Transaction:OFF
     */
    @Test
    void single_transaction() {
        //given
        String username = "single_transaction";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }



}