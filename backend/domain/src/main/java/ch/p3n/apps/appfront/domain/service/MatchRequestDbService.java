package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.InterestEntity;
import ch.p3n.apps.appfront.domain.entity.MatchRequestEntity;
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
public class MatchRequestDbService {

    private static final String SELECT_COMPLETED_MATCH_REQUEST = "db.mapper.MatchRequestMapper.selectCompletedMatchRequest";

    private static final String SELECT_MATCH_REQUEST_BY_INTEREST_ID = "db.mapper.MatchRequestMapper.selectMatchRequestByInterestId";

    private static final String INSERT_MATCH_REQUEST = "db.mapper.MatchRequestMapper.insertMatchRequest";

    private static final String DELETE_MATCH_REQUEST = "db.mapper.MatchRequestMapper.deleteMatchRequest";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Collection<MatchRequestEntity> getCompletedMatchRequests() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(SELECT_COMPLETED_MATCH_REQUEST);
        }
    }

    public boolean hasMatchRequestByInterestId(final MatchRequestEntity matchRequest) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(SELECT_MATCH_REQUEST_BY_INTEREST_ID, matchRequest) != null;
        }
    }

    public MatchRequestEntity createMatchRequest(MatchRequestEntity matchRequest) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert(INSERT_MATCH_REQUEST, matchRequest);
            return matchRequest;
        }
    }

    public void deleteMatchRequest(final MatchRequestEntity matchRequest) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.delete(DELETE_MATCH_REQUEST, matchRequest);
        }
    }

}
