package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.sleuth.Tracer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//@RefreshScope
@RestController
public class LogGenerationController {

    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(LogGenerationApplication.class.getName());
    private static final String device = "web";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public LogGenerationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate=300000)
    @RequestMapping("/generateActivities")
    public String generateActivities() throws Exception {

        // empty activities table
        jdbcTemplate.update("DELETE FROM technical_activities;");

        // insert trace_span table
        jdbcTemplate.update("INSERT INTO trace_span (trace_id, span_id, session_id)\n" +
                "SELECT DISTINCT s.trace_id, id, session_id\n" +
                "FROM zipkin_spans s, zipkin_annotations a, activities ac\n" +
                "WHERE a.span_id = s.id AND a.trace_id = ac.trace_id;");

        // empty activities table and create successful user activities
        jdbcTemplate.update("DELETE FROM activities;");

        jdbcTemplate.update("INSERT INTO activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  start_ts_edges,\n" +
                "  end_ts_edges,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ")\n" +
                "  SELECT DISTINCT\n" +
                "    a1.a_value                                            AS session_ID,\n" +
                "    m1.pretty_name                                        AS activity,\n" +
                "    FROM_UNIXTIME((s1.start_ts * 0.000001))               AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts * 0.000001))               AS start_ts_edges,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts_edges,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "    FLOOR(RAND() * 999999999999999999)                    AS span_id,\n" +
                "    'user'                                                AS activity_type,\n" +
                "    FALSE                                                 AS failure,\n" +
                "    1                                                     AS sorting\n" +
                "  FROM zipkin_spans AS s1\n" +
                "    INNER JOIN zipkin_annotations AS a1 ON\n" +
                "                                    s1.trace_id = a1.trace_id\n" +
                "                                    AND s1.id = a1.span_id\n" +
                "                                    AND s1.parent_id IS NULL\n" +
                "                                    AND a1.a_key = 'sessionID'\n" +
                "                                    AND a1.trace_id NOT IN (SELECT trace_id\n" +
                "                                                            FROM zipkin_annotations\n" +
                "                                                            WHERE a_key = 'error')\n" +
                "    INNER JOIN activity_mappings AS m1 ON s1.name = m1.technical_activity AND m1.is_activity = TRUE;");

        // create failed user activities
        jdbcTemplate.update("INSERT INTO activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  start_ts_edges,\n" +
                "  end_ts_edges,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ")\n" +
                "  SELECT DISTINCT\n" +
                "    a1.a_value                                            AS session_ID,\n" +
                "    CONCAT(m1.pretty_name, ' failed')                     AS activity,\n" +
                "    FROM_UNIXTIME((s1.start_ts * 0.000001))               AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts * 0.000001))               AS start_ts_edges,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts_edges,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "    FLOOR(RAND() * 999999999999999999)                    AS span_id,\n" +
                "    'user'                                                AS activity_type,\n" +
                "    TRUE                                                  AS failure,\n" +
                "    1                                                     AS sorting\n" +
                "  FROM zipkin_annotations AS a1\n" +
                "    INNER JOIN zipkin_spans AS s1 ON\n" +
                "                                    s1.trace_id = a1.trace_id\n" +
                "                                    AND s1.id = a1.span_id\n" +
                "                                    AND s1.parent_id IS NULL\n" +
                "                                    AND a1.a_key = 'sessionID'\n" +
                "                                    AND a1.trace_id IN (SELECT trace_id\n" +
                "                                                                  FROM zipkin_annotations\n" +
                "                                                                  WHERE a_key = 'error')\n" +
                "    INNER JOIN activity_mappings AS m1 ON s1.name = m1.technical_activity AND m1.is_activity = TRUE;");

        // create successful system activities
        jdbcTemplate.update("INSERT INTO activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  service_name,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ") SELECT\n" +
                "    a2.a_value                                            AS session_id,\n" +
                "    s1.name                                               AS activity,\n" +
                "    FROM_UNIXTIME(s1.start_ts * 0.000001)                 AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    FLOOR(RAND() * 999999999999999999)                    AS trace_id,\n" +
                "    -- s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "     s1.id                                                AS span_id,\n" +
                "    \"system\"                                              AS activity_type,\n" +
                "    am.calls_service                                      AS service_name,\n" +
                "    FALSE                                                 AS failure,\n" +
                "    2                                                     AS sorting\n" +
                "  FROM zipkin_spans s1\n" +
                "    INNER JOIN zipkin_spans s2 ON s1.id = s2.id AND s1.name LIKE 'http:/%'\n" +
                "                                  AND (s1.id NOT IN (SELECT DISTINCT a5.span_id\n" +
                "                                                     FROM zipkin_annotations a5\n" +
                "                                                       JOIN zipkin_annotations a6\n" +
                "                                                         ON a5.a_key = 'cs' AND a5.span_id = a6.span_id AND\n" +
                "                                                            a6.span_id NOT IN (SELECT span_id\n" +
                "                                                                               FROM zipkin_annotations\n" +
                "                                                                               WHERE a_key = 'cr'))\n" +
                "                                       AND (s1.id NOT IN (SELECT DISTINCT a7.span_id\n" +
                "                                                          FROM zipkin_annotations a7\n" +
                "                                                            JOIN zipkin_annotations a8\n" +
                "                                                              ON a7.a_key = 'error' AND a7.span_id = a8.span_id AND\n" +
                "                                                                 a8.span_id NOT IN (SELECT span_id\n" +
                "                                                                                    FROM zipkin_annotations\n" +
                "                                                                                    WHERE a_key = 'ss'))))\n" +
                "\n" +
                "    INNER JOIN zipkin_spans s3 ON s3.trace_id = s1.trace_id AND s3.parent_id IS NULL\n" +
                "    INNER JOIN zipkin_annotations a2 ON a2.span_id = s3.id AND a2.a_key = 'sessionID'\n" +
                "    INNER JOIN activity_mappings am ON s2.name = technical_activity AND is_activity = TRUE;");

        // create failed system activities
        jdbcTemplate.update("INSERT INTO activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  service_name,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ") SELECT\n" +
                "    a2.a_value                                            AS session_id,\n" +
                "    CONCAT(am.pretty_name, ' failed')                     AS activity,\n" +
                "    FROM_UNIXTIME(s1.start_ts * 0.000001)                 AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    FLOOR(RAND() * 999999999999999999)                    AS trace_id,\n" +
                "--    s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "    s1.id                                            AS span_id,\n" +
                "    am.type                                               AS activity_type,\n" +
                "    am.calls_service                                      AS service_name,\n" +
                "    TRUE                                                  AS failure,\n" +
                "    2                                                     AS sorting\n" +
                "  FROM zipkin_spans s1\n" +
                "    INNER JOIN zipkin_spans s2 ON s1.id = s2.id AND s1.name LIKE 'http:/%'\n" +
                "                                  AND (s1.id IN (SELECT DISTINCT a5.span_id\n" +
                "                                                 FROM zipkin_annotations a5\n" +
                "                                                   JOIN zipkin_annotations a6\n" +
                "                                                     ON a5.a_key = 'cs' AND a5.span_id = a6.span_id AND\n" +
                "                                                        a6.span_id NOT IN (SELECT span_id\n" +
                "                                                                           FROM zipkin_annotations\n" +
                "                                                                           WHERE a_key = 'cr'))\n" +
                "                                       OR (s1.id IN (SELECT DISTINCT a7.span_id\n" +
                "                                                     FROM zipkin_annotations a7\n" +
                "                                                       JOIN zipkin_annotations a8\n" +
                "                                                         ON a7.a_key = 'error' AND a7.span_id = a8.span_id AND\n" +
                "                                                            a8.span_id NOT IN (SELECT span_id\n" +
                "                                                                               FROM zipkin_annotations\n" +
                "                                                                               WHERE a_key = 'ss'))))\n" +
                "\n" +
                "    INNER JOIN zipkin_spans s3 ON s3.trace_id = s1.trace_id AND s3.parent_id IS NULL\n" +
                "    INNER JOIN zipkin_annotations a2 ON a2.span_id = s3.id AND a2.a_key = 'sessionID'\n" +
                "    INNER JOIN activity_mappings am ON s2.name = technical_activity AND is_activity = TRUE;");

         //jdbcTemplate.update("DELETE FROM technical_activities;");

        // create successful system activities (technical_activities)
        jdbcTemplate.update(
                "INSERT INTO technical_activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  service_name,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ") SELECT\n" +
                "    a2.a_value                                            AS session_id,\n" +
                "    s1.name                                               AS activity,\n" +
                "    FROM_UNIXTIME(s1.start_ts * 0.000001)                 AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    -- FLOOR(RAND() * 999999999999999999)                    AS trace_id,\n" +
                "    s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "     s1.id                                                AS span_id,\n" +
                "    \"system\"                                              AS activity_type,\n" +
                "    am.calls_service                                      AS service_name,\n" +
                "    FALSE                                                 AS failure,\n" +
                "    2                                                     AS sorting\n" +
                "  FROM zipkin_spans s1\n" +
                "    INNER JOIN zipkin_spans s2 ON s1.id = s2.id AND s1.name LIKE 'http:/%'\n" +
                "                                  AND (s1.id NOT IN (SELECT DISTINCT a5.span_id\n" +
                "                                                     FROM zipkin_annotations a5\n" +
                "                                                       JOIN zipkin_annotations a6\n" +
                "                                                         ON a5.a_key = 'cs' AND a5.span_id = a6.span_id AND\n" +
                "                                                            a6.span_id NOT IN (SELECT span_id\n" +
                "                                                                               FROM zipkin_annotations\n" +
                "                                                                               WHERE a_key = 'cr'))\n" +
                "                                       AND (s1.id NOT IN (SELECT DISTINCT a7.span_id\n" +
                "                                                          FROM zipkin_annotations a7\n" +
                "                                                            JOIN zipkin_annotations a8\n" +
                "                                                              ON a7.a_key = 'error' AND a7.span_id = a8.span_id AND\n" +
                "                                                                 a8.span_id NOT IN (SELECT span_id\n" +
                "                                                                                    FROM zipkin_annotations\n" +
                "                                                                                    WHERE a_key = 'ss'))))\n" +
                "\n" +
                "    INNER JOIN zipkin_spans s3 ON s3.trace_id = s1.trace_id AND s3.parent_id IS NULL\n" +
                "    INNER JOIN zipkin_annotations a2 ON a2.span_id = s3.id AND a2.a_key = 'sessionID'\n" +
                "    INNER JOIN activity_mappings am ON s2.name = technical_activity AND is_activity = TRUE;");

        // create failed system activities (technical_activities)
        jdbcTemplate.update("INSERT INTO technical_activities (\n" +
                "  session_id,\n" +
                "  activity,\n" +
                "  start_ts,\n" +
                "  end_ts,\n" +
                "  duration,\n" +
                "  trace_id,\n" +
                "  trace_id_hex,\n" +
                "  span_id,\n" +
                "  activity_type,\n" +
                "  service_name,\n" +
                "  failure,\n" +
                "  sorting\n" +
                ") SELECT\n" +
                "    a2.a_value                                            AS session_id,\n" +
                "    CONCAT(am.pretty_name, ' failed')                     AS activity,\n" +
                "    FROM_UNIXTIME(s1.start_ts * 0.000001)                 AS start_ts,\n" +
                "    FROM_UNIXTIME((s1.start_ts + s1.duration) * 0.000001) AS end_ts,\n" +
                "    s1.duration * 0.001                                   AS duration,\n" +
                "    -- FLOOR(RAND() * 999999999999999999)                    AS trace_id,\n" +
                "    s1.trace_id                                           AS trace_id,\n" +
                "    LOWER(HEX(s1.trace_id))                               AS trace_id_hex,\n" +
                "    s1.id                                                 AS span_id,\n" +
                "    am.type                                               AS activity_type,\n" +
                "    am.calls_service                                      AS service_name,\n" +
                "    TRUE                                                  AS failure,\n" +
                "    2                                                     AS sorting\n" +
                "  FROM zipkin_spans s1\n" +
                "    INNER JOIN zipkin_spans s2 ON s1.id = s2.id AND s1.name LIKE 'http:/%'\n" +
                "                                  AND (s1.id IN (SELECT DISTINCT a5.span_id\n" +
                "                                                 FROM zipkin_annotations a5\n" +
                "                                                   JOIN zipkin_annotations a6\n" +
                "                                                     ON a5.a_key = 'cs' AND a5.span_id = a6.span_id AND\n" +
                "                                                        a6.span_id NOT IN (SELECT span_id\n" +
                "                                                                           FROM zipkin_annotations\n" +
                "                                                                           WHERE a_key = 'cr'))\n" +
                "                                       OR (s1.id IN (SELECT DISTINCT a7.span_id\n" +
                "                                                     FROM zipkin_annotations a7\n" +
                "                                                       JOIN zipkin_annotations a8\n" +
                "                                                         ON a7.a_key = 'error' AND a7.span_id = a8.span_id AND\n" +
                "                                                            a8.span_id NOT IN (SELECT span_id\n" +
                "                                                                               FROM zipkin_annotations\n" +
                "                                                                               WHERE a_key = 'ss'))))\n" +
                "\n" +
                "    INNER JOIN zipkin_spans s3 ON s3.trace_id = s1.trace_id AND s3.parent_id IS NULL\n" +
                "    INNER JOIN zipkin_annotations a2 ON a2.span_id = s3.id AND a2.a_key = 'sessionID'\n" +
                "    INNER JOIN activity_mappings am ON s2.name = technical_activity AND is_activity = TRUE;");

        // trigger data relaod
        jdbcTemplate.update("INSERT INTO RELOAD_TRIGGER_TABLE (.RELOAD_TRIGGER_TABLE.DATA_MODEL_NAME, .RELOAD_TRIGGER_TABLE.RELOAD_REQUEST_TIME) VALUES ('Datamodel', AddTime(now(), '00:00:00'));");

        return "Acitivities created and data model reaload triggered";
    }



}
