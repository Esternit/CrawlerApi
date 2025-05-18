package dev.esternit.ApiCrawler.service;

import dev.esternit.ApiCrawler.dto.CrawlerTaskStatusDto;
import dev.esternit.ApiCrawler.mapper.CrawlerTaskStatusMapper;
import dev.esternit.ApiCrawler.repository.CrawlerTaskStatusRepository;
import dev.esternit.generated.tables.records.CrawlerTaskStatusRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CrawlerTaskStatusService {

    private final CrawlerTaskStatusRepository crawlerTaskStatusRepository;
    private final CrawlerTaskStatusMapper crawlerTaskStatusMapper;

    //Выводит все задачи
    public List<CrawlerTaskStatusDto> getAllTasks() {
        return crawlerTaskStatusRepository.findAll().stream()
                .map(crawlerTaskStatusMapper::mapToDto)
                .toList();
    }

    //Выводит задачу по id
    public Optional<CrawlerTaskStatusDto> getTaskById(Integer taskId) {
        return crawlerTaskStatusRepository.findById(taskId)
                .map(crawlerTaskStatusMapper::mapToDto);
    }

    //Создает задачу (для того, чтобы добавить новые ссылки в парсинг)
    @Transactional
    public CrawlerTaskStatusDto createTask(String imdbUrl, String status, String assignedInstance) {
        CrawlerTaskStatusRecord task = crawlerTaskStatusRepository.create(imdbUrl, status, assignedInstance);
        return crawlerTaskStatusMapper.mapToDto(task);
    }

    //Обновляет задачу
    @Transactional
    public Optional<CrawlerTaskStatusDto> updateTask(Integer taskId, String status, String assignedInstance) {
        return Optional.ofNullable(crawlerTaskStatusRepository.update(taskId, status, assignedInstance))
                .map(crawlerTaskStatusMapper::mapToDto);
    }

    // Удаляет задачу
    @Transactional
    public void deleteTask(Integer taskId) {
        crawlerTaskStatusRepository.delete(taskId);
    }
}
