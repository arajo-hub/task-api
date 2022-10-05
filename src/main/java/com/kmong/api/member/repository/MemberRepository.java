package com.kmong.api.member.repository;

import com.kmong.api.member.domain.Member;
import com.kmong.api.member.request.MemberCreate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
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

    /**
     * 아이디와 이메일 중복 여부 조회
     * @param memberCreate 중복 여부를 조회할 회원 정보
     * @return 중복 여부
     */
    public boolean isAlreadyExist(MemberCreate memberCreate) {
        List<Member> alreadyExistMembers = queryFactory.selectFrom(member)
                .where(isSameId(memberCreate.getMemberId()).or(isSameEmail(memberCreate.getEmail())))
                .fetch();
        return !CollectionUtils.isEmpty(alreadyExistMembers);
    }

    private BooleanExpression isSameId(String id) {
        return StringUtils.isNullOrEmpty(id) ? null : member.id.equalsIgnoreCase(id);
    }

    private BooleanExpression isSameEmail(String email) {
        return StringUtils.isNullOrEmpty(email) ? null : member.email.equalsIgnoreCase(email);
    }
}
