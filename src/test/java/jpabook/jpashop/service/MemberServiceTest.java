package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // 테스트를 스프링을 띄운 상태로 실행.
@SpringBootTest // 스프링 부트 띄우고 실행 => 스프링 컨테이너 @Autowired 필요
@Transactional // default 가 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService; // 테스트니까 간단하게 해도 OK
    @Autowired MemberRepository memberRepository; // 테스트니까 간단하게 해도 OK
    @Autowired EntityManager em;

    @Test
    public void 회원기입() {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush();
        Assertions.assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외 걸림: 같은 이름

        // then
        Assert.fail("에외가 발생해야 함");
    }
}