package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.CrawlerTaskStatusDto;
import dev.esternit.ApiCrawler.mapper.CrawlerTaskStatusMapper;
import dev.esternit.ApiCrawler.repository.CrawlerTaskStatusRepository;
import dev.esternit.jooq.generated.tables.records.CrawlerTaskStatusRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrawlerTaskStatusServiceTest {

    @Mock
    private CrawlerTaskStatusRepository crawlerTaskStatusRepository;

    @Mock
    private CrawlerTaskStatusMapper crawlerTaskStatusMapper;

    @InjectMocks
    private CrawlerTaskStatusService crawlerTaskStatusService;

    private CrawlerTaskStatusRecord taskRecord1;
    private CrawlerTaskStatusRecord taskRecord2;
    private CrawlerTaskStatusDto taskDto1;
    private CrawlerTaskStatusDto taskDto2;

    @BeforeEach
    void setUp() {
        // Setup test data
        taskRecord1 = new CrawlerTaskStatusRecord();
        taskRecord1.setTaskId(1);
        taskRecord1.setImdbUrl("https://imdb.com/test1");
        taskRecord1.setStatus("pending");
        taskRecord1.setAssignedInstance("instance1");
        taskRecord1.setLastUpdated(OffsetDateTime.now());

        taskRecord2 = new CrawlerTaskStatusRecord();
        taskRecord2.setTaskId(2);
        taskRecord2.setImdbUrl("https://imdb.com/test2");
        taskRecord2.setStatus("in_progress");
        taskRecord2.setAssignedInstance("instance2");
        taskRecord2.setLastUpdated(OffsetDateTime.now());

        taskDto1 = new CrawlerTaskStatusDto(
                1,
                "https://imdb.com/test1",
                "pending",
                null,
                null,
                null,
                OffsetDateTime.now(),
                "instance1"
        );

        taskDto2 = new CrawlerTaskStatusDto(
                2,
                "https://imdb.com/test2",
                "in_progress",
                null,
                null,
                null,
                OffsetDateTime.now(),
                "instance2"
        );
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        when(crawlerTaskStatusRepository.findAll()).thenReturn(Arrays.asList(taskRecord1, taskRecord2));
        when(crawlerTaskStatusMapper.mapToDto(taskRecord1)).thenReturn(taskDto1);
        when(crawlerTaskStatusMapper.mapToDto(taskRecord2)).thenReturn(taskDto2);

        // Act
        List<CrawlerTaskStatusDto> result = crawlerTaskStatusService.getAllTasks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(taskDto1, result.get(0));
        assertEquals(taskDto2, result.get(1));
        verify(crawlerTaskStatusRepository).findAll();
        verify(crawlerTaskStatusMapper, times(2)).mapToDto(any(CrawlerTaskStatusRecord.class));
    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        // Arrange
        when(crawlerTaskStatusRepository.findById(1)).thenReturn(Optional.of(taskRecord1));
        when(crawlerTaskStatusMapper.mapToDto(taskRecord1)).thenReturn(taskDto1);

        // Act
        Optional<CrawlerTaskStatusDto> result = crawlerTaskStatusService.getTaskById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(taskDto1, result.get());
        verify(crawlerTaskStatusRepository).findById(1);
        verify(crawlerTaskStatusMapper).mapToDto(taskRecord1);
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(crawlerTaskStatusRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<CrawlerTaskStatusDto> result = crawlerTaskStatusService.getTaskById(999);

        // Assert
        assertTrue(result.isEmpty());
        verify(crawlerTaskStatusRepository).findById(999);
        verify(crawlerTaskStatusMapper, never()).mapToDto(any(CrawlerTaskStatusRecord.class));
    }

    @Test
    void createTask_ShouldCreateAndReturnTask() {
        // Arrange
        String imdbUrl = "https://imdb.com/new";
        String status = "pending";
        String assignedInstance = "instance1";
        when(crawlerTaskStatusRepository.create(imdbUrl, status, assignedInstance)).thenReturn(taskRecord1);
        when(crawlerTaskStatusMapper.mapToDto(taskRecord1)).thenReturn(taskDto1);

        // Act
        CrawlerTaskStatusDto result = crawlerTaskStatusService.createTask(imdbUrl, status, assignedInstance);

        // Assert
        assertNotNull(result);
        assertEquals(taskDto1, result);
        verify(crawlerTaskStatusRepository).create(imdbUrl, status, assignedInstance);
        verify(crawlerTaskStatusMapper).mapToDto(taskRecord1);
    }

    @Test
    void updateTask_WhenTaskExists_ShouldUpdateAndReturnTask() {
        // Arrange
        Integer taskId = 1;
        String status = "in_progress";
        String assignedInstance = "instance2";
        when(crawlerTaskStatusRepository.update(taskId, status, assignedInstance)).thenReturn(taskRecord1);
        when(crawlerTaskStatusMapper.mapToDto(taskRecord1)).thenReturn(taskDto1);

        // Act
        Optional<CrawlerTaskStatusDto> result = crawlerTaskStatusService.updateTask(taskId, status, assignedInstance);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(taskDto1, result.get());
        verify(crawlerTaskStatusRepository).update(taskId, status, assignedInstance);
        verify(crawlerTaskStatusMapper).mapToDto(taskRecord1);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        Integer taskId = 999;
        String status = "in_progress";
        String assignedInstance = "instance2";
        when(crawlerTaskStatusRepository.update(taskId, status, assignedInstance)).thenReturn(null);

        // Act
        Optional<CrawlerTaskStatusDto> result = crawlerTaskStatusService.updateTask(taskId, status, assignedInstance);

        // Assert
        assertTrue(result.isEmpty());
        verify(crawlerTaskStatusRepository).update(taskId, status, assignedInstance);
        verify(crawlerTaskStatusMapper, never()).mapToDto(any(CrawlerTaskStatusRecord.class));
    }

    @Test
    void deleteTask_ShouldCallRepositoryDelete() {
        // Arrange
        Integer taskId = 1;

        // Act
        crawlerTaskStatusService.deleteTask(taskId);

        // Assert
        verify(crawlerTaskStatusRepository).delete(taskId);
    }
}
