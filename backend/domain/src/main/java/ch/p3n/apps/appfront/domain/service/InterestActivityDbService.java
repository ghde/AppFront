package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.InterestActivityEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Database service for {@link InterestActivityEntity}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class InterestActivityDbService {

    private static final String SELECT_INTEREST_ACTIVITY_BY_INTEREST_ID = "db.mapper.InterestActivityMapper.selectInterestActivityByInterestId";

    private static final String SELECT_INTEREST_ACTIVITY_BY_ACTIVITY_ID = "db.mapper.InterestActivityMapper.selectInterestActivityByActivityId";

    private static final String INSERT_INTEREST_ACTIVITY = "db.mapper.InterestActivityMapper.insertInterestActivity";

    private static final String DELETE_INTEREST_ACTIVITY = "db.mapper.InterestActivityMapper.deleteInterestActivityByClientId";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Collection<InterestActivityEntity> getInterestActivitiesByInterestId(final int interestId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(SELECT_INTEREST_ACTIVITY_BY_INTEREST_ID, interestId);
        }
    }

    public Collection<InterestActivityEntity> getInterestActivitiesByActivityId(final int activityId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(SELECT_INTEREST_ACTIVITY_BY_ACTIVITY_ID, activityId);
        }
    }

    public InterestActivityEntity createAuthentication(InterestActivityEntity interestActivity) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert(INSERT_INTEREST_ACTIVITY, interestActivity);
            return interestActivity;
        }
    }

    public void deleteInterestActivitiesByInterestId(final int interestId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.delete(DELETE_INTEREST_ACTIVITY, interestId);
        }
    }

}