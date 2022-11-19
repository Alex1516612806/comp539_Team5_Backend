package com.team5.team5_backend;

import com.google.cloud.bigtable.hbase.BigtableConfiguration;
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

public class BigtableCli {
//    private static final String PROJECT_ID = "[YOUR_PROJECT_ID]";
//    private static final String INSTANCE_ID = "[YOUR_INSTANCE_ID]";
//    // Include the following line if you are using app profiles.
//    // If you do not include the following line, the connection uses the
//    // default app profile.
//    private static final String APP_PROFILE_ID = "[YOUR_APP_PROFILE_ID]";
//
//    private static Connection connection = null;
//
//    public static void connect() throws IOException {
//        Configuration config = BigtableConfiguration.configure(PROJECT_ID, INSTANCE_ID);
//        // Include the following line if you are using app profiles.
//        // If you do not include the following line, the connection uses the
//        // default app profile.
//        config.set(BigtableOptionsFactory.APP_PROFILE_ID_KEY, APP_PROFILE_ID);
//
//        connection = BigtableConfiguration.connect(config);
//    }
}