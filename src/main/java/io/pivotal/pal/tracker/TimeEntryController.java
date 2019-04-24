package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;

    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        ResponseEntity<List<TimeEntry>> responseEntity;
        List<TimeEntry> timeEntries;

        timeEntries = this.timeEntryRepository.list();

        responseEntity = new ResponseEntity<>(timeEntries, HttpStatus.OK);

        return responseEntity;
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable Long id) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry timeEntry;
        HttpStatus status = HttpStatus.NOT_FOUND;

        timeEntry = this.timeEntryRepository.find(id);
        if(timeEntry != null) {
            status = HttpStatus.OK;
        }

        responseEntity = new ResponseEntity<>(timeEntry, status);
        return responseEntity;
    }


    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntry) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry createdTimeEntry;

        createdTimeEntry = this.timeEntryRepository.create(timeEntry);
        responseEntity = new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);

        return responseEntity;
    }


    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry timeEntry) {
        ResponseEntity<TimeEntry> responseEntity;
        TimeEntry updatedTimeEntry;
        HttpStatus status = HttpStatus.NOT_FOUND;

        updatedTimeEntry = this.timeEntryRepository.update(id, timeEntry);
        if(updatedTimeEntry != null) {
            status = HttpStatus.OK;
        }

        responseEntity = new ResponseEntity<>(updatedTimeEntry, status);


        return responseEntity;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        ResponseEntity<TimeEntry> responseEntity;

        this.timeEntryRepository.delete(id);
        responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return responseEntity;
    }
}
