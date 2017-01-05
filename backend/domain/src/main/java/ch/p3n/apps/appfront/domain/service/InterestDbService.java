package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Database service for {@link InterestEntity}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class InterestDbService {

    private static final String SELECT_INTEREST_BY_INTEREST_ID = "db.mapper.InterestMapper.selectInterestByInterestId";

    private static final String SELECT_INTEREST_WITH_ACTIVE_BROADCAST = "db.mapper.InterestMapper.selectInterestWithActiveBroadcast";

    private static final String INSERT_INTEREST = "db.mapper.InterestMapper.insertInterest";

    private static final String DELETE_INTEREST_ALREADY_EXPIRED = "db.mapper.InterestMapper.deleteInterestAlreadyExpired";

    private static final String DELETE_INTEREST_BY_ID = "db.mapper.InterestMapper.deleteInterestById";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public InterestEntity getInterestByInterestId(final String interestId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(SELECT_INTEREST_BY_INTEREST_ID, interestId);
        }
    }

    public Collection<InterestEntity> getInterestsWithActiveBroadcast() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(SELECT_INTEREST_WITH_ACTIVE_BROADCAST);
        }
    }

    public InterestEntity createInterest(InterestEntity interest) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert(INSERT_INTEREST, interest);
            return interest;
        }
    }

    public void deleteInterestAlreadyExpired() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.delete(DELETE_INTEREST_ALREADY_EXPIRED);
        }
    }

    public void deleteInterestById(final int interestId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.delete(DELETE_INTEREST_BY_ID, interestId);
        }
    }

}