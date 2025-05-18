package dev.esternit.ApiCrawler.dto;

import java.time.OffsetDateTime;

public record CrawlerTaskStatusDto(
        Integer taskId,
        String imdbUrl,
        String status,
        OffsetDateTime startedAt,
        OffsetDateTime finishedAt,
        String errorMessage,
        OffsetDateTime lastUpdated,
        String assignedInstance
        ) {

}
