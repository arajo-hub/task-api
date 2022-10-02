package com.kmong.api.member.repository;

import com.kmong.api.member.domain.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

import static com.kmong.api.member.domain.QMember.member;

@Repository
@Transactional
public class MemberRepository {

    private EntityManager em;
    private JPAQueryFactory queryFactory;

    @Autowired
    public MemberRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 회원 가입
     *
     * @return 가입 완료된 회원
     */
    public Optional<Member> save(Member member) {
        em.persist(member);
        return Optional.ofNullable(member);
    }

    /**
     * 회원 전체 삭제
     */
    public void deleteAll() {
        queryFactory.delete(member).execute();
    }

    /**
     * 아이디로 회원정보 검색
     * @param id 정보를 검색할 회원 아이디
     * @return 회원정보
     */
    public Optional<Member> findById(String id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }
}
