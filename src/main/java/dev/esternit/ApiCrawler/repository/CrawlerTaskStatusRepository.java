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

    public List<CrawlerTaskStatusRecord> findAll() {
        return dsl.selectFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .orderBy(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID)
                .fetch();
    }

    public Optional<CrawlerTaskStatusRecord> findById(Integer taskId) {
        return dsl.selectFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .fetchOptional();
    }

    public CrawlerTaskStatusRecord create(String imdbUrl, String status, String assignedInstance) {
        return dsl.insertInto(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.IMDB_URL, imdbUrl)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.STATUS, status)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.ASSIGNED_INSTANCE, assignedInstance)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.LAST_UPDATED, OffsetDateTime.now())
                .returning()
                .fetchOne();
    }

    public CrawlerTaskStatusRecord update(Integer taskId, String status, String assignedInstance) {
        return dsl.update(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.STATUS, status)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.ASSIGNED_INSTANCE, assignedInstance)
                .set(CrawlerTaskStatus.CRAWLER_TASK_STATUS.LAST_UPDATED, OffsetDateTime.now())
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .returning()
                .fetchOne();
    }

    public void delete(Integer taskId) {
        dsl.deleteFrom(CrawlerTaskStatus.CRAWLER_TASK_STATUS)
                .where(CrawlerTaskStatus.CRAWLER_TASK_STATUS.TASK_ID.eq(taskId))
                .execute();
    }
}
