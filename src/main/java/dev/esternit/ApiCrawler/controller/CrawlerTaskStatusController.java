package dev.esternit.ApiCrawler.controller;

import dev.esternit.ApiCrawler.dto.CrawlerTaskStatusDto;
import dev.esternit.ApiCrawler.dto.request.CreateCrawlerTaskRequest;
import dev.esternit.ApiCrawler.dto.request.UpdateCrawlerTaskRequest;
import dev.esternit.ApiCrawler.service.CrawlerTaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/crawler-task")
@Tag(name = "Crawler Task API")
@RequiredArgsConstructor
public class CrawlerTaskStatusController {

    private final CrawlerTaskStatusService crawlerTaskStatusService;

    @GetMapping("")
    @Operation(summary = "Get all crawler tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All tasks found"),})
    public ResponseEntity<List<CrawlerTaskStatusDto>> getAllTasks() {
        return new ResponseEntity<>(crawlerTaskStatusService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<CrawlerTaskStatusDto> getTaskById(@PathVariable Integer taskId) {
        return crawlerTaskStatusService.getTaskById(taskId)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("")
    @Operation(summary = "Create a new crawler task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CrawlerTaskStatusDto> createTask(@RequestBody CreateCrawlerTaskRequest request) {
        CrawlerTaskStatusDto task = crawlerTaskStatusService.createTask(
                request.imdbUrl(),
                request.status(),
                request.assignedInstance()
        );
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PatchMapping("/{taskId}")
    @Operation(summary = "Update an existing task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<CrawlerTaskStatusDto> updateTask(
            @PathVariable Integer taskId,
            @RequestBody UpdateCrawlerTaskRequest request) {
        return crawlerTaskStatusService.updateTask(
                taskId,
                request.status(),
                request.assignedInstance()
        ).map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Integer taskId) {
        crawlerTaskStatusService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
