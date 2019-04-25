package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository timeEntriesRepo;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntriesRepo, MeterRegistry meterRegistry) {
        this.timeEntriesRepo = timeEntriesRepo;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        ResponseEntity<List<TimeEntry>> responseEntity;
        List<TimeEntry> timeEntries;

        actionCounter.increment();
        timeEntries = this.timeEntriesRepo.list();

        responseEntity = new ResponseEntity<>(timeEntries, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry timeEntry;
        HttpStatus status = HttpStatus.NOT_FOUND;

        timeEntry = this.timeEntriesRepo.find(id);
        if(timeEntry != null) {
            status = HttpStatus.OK;
            actionCounter.increment();
        }

        responseEntity = new ResponseEntity<>(timeEntry, status);
        return responseEntity;
    }


    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry createdTimeEntry;

        createdTimeEntry = this.timeEntriesRepo.create(timeEntry);
        responseEntity = new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);

        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());

        return responseEntity;
    }


    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry timeEntry) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry updatedTimeEntry;
        HttpStatus status = HttpStatus.NOT_FOUND;

        updatedTimeEntry = this.timeEntriesRepo.update(id, timeEntry);
        if(updatedTimeEntry != null) {
            status = HttpStatus.OK;
            actionCounter.increment();
        }

        responseEntity = new ResponseEntity<>(updatedTimeEntry, status);


        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        ResponseEntity<TimeEntry> responseEntity;

        this.timeEntriesRepo.delete(id);
        responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        actionCounter.increment();
        timeEntrySummary.record(timeEntriesRepo.list().size());

        return responseEntity;
    }
}
