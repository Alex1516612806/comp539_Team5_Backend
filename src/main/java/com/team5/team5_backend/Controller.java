package com.team5.team5_backend;

import java.io.IOException;

public class Controller {
    public DB myDb = null;

    public Controller() throws IOException {
        myDb = new DB("rice-comp-539-spring-2022", "rice-comp-539-shared-table");
    }
}
