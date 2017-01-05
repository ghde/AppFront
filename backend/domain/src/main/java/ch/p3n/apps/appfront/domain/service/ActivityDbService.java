package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.ActivityEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Database service for {@link ActivityEntity}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class ActivityDbService {

    private static final String SELECT_ACTIVITY_BY_NAME = "db.mapper.ActivityMapper.selectActivityByName";

    private static final String SELECT_ACTIVITIES = "db.mapper.ActivityMapper.selectActivities";

    private static final String INSERT_ACTIVITY = "db.mapper.ActivityMapper.insertActivity";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public ActivityEntity getActivityByName(final String activityName) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(SELECT_ACTIVITY_BY_NAME, activityName);
        }
    }

    public Collection<ActivityEntity> getActivities() {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList(SELECT_ACTIVITIES);
        }
    }

    public ActivityEntity createActivity(ActivityEntity activity) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert(INSERT_ACTIVITY, activity);
            return activity;
        }
    }

}
