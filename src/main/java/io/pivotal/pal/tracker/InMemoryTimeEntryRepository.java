package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    Map<Long, TimeEntry> map = new HashMap<>();
    long nextId = 1;

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(nextId);
        map.put(timeEntry.getId(), timeEntry);
        ++nextId;

        return timeEntry;
    }

    public TimeEntry find(long id) {
        return map.get(id);
    }

    public List<TimeEntry> list() {
        List<TimeEntry> list = new ArrayList(map.values());
        return list;
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry returnVal = null;

        TimeEntry oldVal = map.put(id, timeEntry);

        if(oldVal != null) {
            timeEntry.setId(id);
            returnVal = timeEntry;
        }

        return returnVal;
    }

    public void delete(long id) {
        map.remove(id);
    }
}
