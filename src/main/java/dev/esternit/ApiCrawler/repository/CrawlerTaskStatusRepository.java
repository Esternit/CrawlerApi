package dev.esternit.ApiCrawler.repository;

import dev.esternit.jooq.generated.tables.CrawlerTaskStatus;
import dev.esternit.jooq.generated.tables.records.CrawlerTaskStatusRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CrawlerTaskStatusRepository {

    private final DSLContext dsl;

    /**
     * SELECT * FROM crawler_task_status ORDER BY task_id;
     */
    public List<CrawlerTaskStatusRecord> findAll() {
        return dsl.selectFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .orderBy(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID)
                .fetch();
    }

    /**
     * SELECT * FROM crawler_task_status WHERE task_id = ?;
     */
    public Optional<CrawlerTaskStatusRecord> findById(Integer taskId) {
        return dsl.selectFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .fetchOptional();
    }

    /**
     * INSERT INTO crawler_task_status (imdb_url, status, assigned_instance) VALUES (?, ?, ?) RETURNING *;
     */
    public CrawlerTaskStatusRecord create(String imdbUrl, String status, String assignedInstance) {
        return dsl.insertInto(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.IMDB_URL, imdbUrl)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.STATUS, status)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.ASSIGNED_INSTANCE, assignedInstance)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.LAST_UPDATED, OffsetDateTime.now())
                .returning()
                .fetchOne();
    }

    /**
     * UPDATE crawler_task_status SET status = ?,
     * assigned_instance = ? WHERE task_id = ? RETURNING *;
     */
    public CrawlerTaskStatusRecord update(Integer taskId, String status, String assignedInstance) {
        return dsl.update(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.STATUS, status)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.ASSIGNED_INSTANCE, assignedInstance)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.LAST_UPDATED, OffsetDateTime.now())
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .returning()
                .fetchOne();
    }

    /**
     * DELETE FROM crawler_task_status WHERE task_id = ?;
     */
    public void delete(Integer taskId) {
        dsl.deleteFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .execute();
    }
}
