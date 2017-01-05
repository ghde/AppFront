package ch.p3n.apps.appfront.domain.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Mapper for {@link LocalDateTime} to {@link java.sql.Timestamp}.
 *
 * @author deluc1
 * @author zempm3
 */
public class LocalDateTimeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(final PreparedStatement ps, final int i, final LocalDateTime parameter, final JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setTime(i, null);
        } else {
            final Timestamp ts = Timestamp.valueOf(parameter);
            final Calendar calendar = GregorianCalendar.from(ZonedDateTime.of(parameter, ZoneId.systemDefault()));
            ps.setTimestamp(i, ts, calendar);
        }
    }

    @Override
    public LocalDateTime getNullableResult(final ResultSet rs, final String columnName) throws SQLException {
        return toLocalDateTime(rs.getTimestamp(columnName));
    }

    @Override
    public LocalDateTime getNullableResult(final ResultSet rs, final int columnIndex) throws SQLException {
        return toLocalDateTime(rs.getTimestamp(columnIndex));
    }

    @Override
    public LocalDateTime getNullableResult(final CallableStatement cs, final int columnIndex) throws SQLException {
        return toLocalDateTime(cs.getTimestamp(columnIndex));
    }

    private LocalDateTime toLocalDateTime(final Timestamp ts) {
        if (ts != null) {
            return LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

}
