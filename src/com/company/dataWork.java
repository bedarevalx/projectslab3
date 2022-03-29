package com.company;

import java.util.Date;
import java.util.Date;
import org.jfree.data.gantt.Task;

public class dataWork extends Task {
    public dataWork(String description, long start, long end) {
        super(description, new Date(start), new Date(end));
    }

    public static dataWork duration(String description, long start, long duration) {
        return new dataWork(description, start, start + duration);
    }
}
