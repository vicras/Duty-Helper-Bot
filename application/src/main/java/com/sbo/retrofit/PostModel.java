package com.sbo.retrofit;

import com.sbo.domain.postgres.entity.PeopleOnDuty;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Collectors;

public class PostModel {

    public int rowWidth;
    public int rowHeight;
    public Collection<People> records;

    public PostModel(int rowWidth, int rowHeight, Collection<PeopleOnDuty> records) {
        this.rowWidth = rowWidth;
        this.rowHeight = rowHeight;
        this.records = records.stream()
                .map(
                        e -> new People(e.getPerson().getId().toString(), e.getFromTime()
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z",
                                e.getToTime()
                                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z"))
                .collect(Collectors.toList());

    }

    class People {
        public String personId;
        public String from;
        public String to;

        public People(String personId, String from, String to) {
            this.personId = personId;
            this.from = from;
            this.to = to;
        }
    }


}


