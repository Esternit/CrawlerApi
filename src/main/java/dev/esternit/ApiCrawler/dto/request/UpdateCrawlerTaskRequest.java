package dev.esternit.ApiCrawler.dto.request;

public record UpdateCrawlerTaskRequest(
        String status,
        String assignedInstance
        ) {

}
