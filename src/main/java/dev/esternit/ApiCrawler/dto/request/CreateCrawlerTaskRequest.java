package dev.esternit.ApiCrawler.dto.request;

public record CreateCrawlerTaskRequest(
        String imdbUrl,
        String status,
        String assignedInstance
) {}