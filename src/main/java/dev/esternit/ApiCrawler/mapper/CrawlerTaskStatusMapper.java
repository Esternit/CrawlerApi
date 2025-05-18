package dev.esternit.ApiCrawler.mapper;

import dev.esternit.ApiCrawler.dto.CrawlerTaskStatusDto;
import dev.esternit.jooq.generated.tables.records.CrawlerTaskStatusRecord;
import org.springframework.stereotype.Component;

@Component
public class CrawlerTaskStatusMapper {

    public CrawlerTaskStatusDto mapToDto(CrawlerTaskStatusRecord task) {
        if (task == null) {
            return null;
        }

        return new CrawlerTaskStatusDto(
                task.getTaskId(),
                task.getImdbUrl(),
                task.getStatus(),
                task.getStartedAt(),
                task.getFinishedAt(),
                task.getErrorMessage(),
                task.getLastUpdated(),
                task.getAssignedInstance()
        );
    }
}
