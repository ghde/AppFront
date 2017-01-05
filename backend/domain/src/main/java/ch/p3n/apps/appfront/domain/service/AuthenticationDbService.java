package ch.p3n.apps.appfront.domain.service;

import ch.p3n.apps.appfront.domain.entity.AuthenticationEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Database service for {@link AuthenticationEntity}.
 *
 * @author deluc1
 * @author zempm3
 */
@Component
public class AuthenticationDbService {

    private static final String SELECT_AUTHENTICATION_BY_CLIENT_ID = "db.mapper.AuthenticationMapper.selectAuthenticationByClientId";

    private static final String INSERT_AUTHENTICATION = "db.mapper.AuthenticationMapper.insertAuthentication";

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public AuthenticationEntity getAuthenticationByClientId(final String clientId) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectOne(SELECT_AUTHENTICATION_BY_CLIENT_ID, clientId);
        }
    }

    public AuthenticationEntity createAuthentication(AuthenticationEntity authentication) {
        try (final SqlSession sqlSession = sqlSessionFactory.openSession()) {
            sqlSession.insert(INSERT_AUTHENTICATION, authentication);
            return authentication;
        }
    }

}
