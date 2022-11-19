package com.team5.team5_backend;

import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.hbase.BigtableConfiguration;
import com.google.cloud.bigtable.hbase.BigtableOptionsFactory;
import com.team5.team5_backend.table_object.Url;
import com.team5.team5_backend.table_object.User;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class DB {

    private static final byte[] URL_TBL = Bytes.toBytes("Url");
    private static final byte[] URL_TBL_COL_FMY_URL = Bytes.toBytes("Url");
    private static final byte[] URL_TBL_COL_LONG_URL = Bytes.toBytes("LongUrl");
    private static final byte[] URL_TBL_COL_SHORT_URL = Bytes.toBytes("ShortUrl");
    private static final byte[] URL_TBL_COL_EXP_TIME = Bytes.toBytes("ExpireTime");
    private static final byte[] URL_TBL_COL_FMY_OWNER = Bytes.toBytes("Owner");
    private static final byte[] URL_TBL_COL_OWNER = Bytes.toBytes("UserID");

    private static final byte[] USER_TBL = Bytes.toBytes("User");
    private static final byte[] USER_TBL_COL_FMY_ACCOUNT = Bytes.toBytes("User");
    private static final byte[] USER_TBL_COL_EMAIL_ADDR = Bytes.toBytes("EmailAddress");
    private static final byte[] USER_TBL_COL_USER_TYPE = Bytes.toBytes("UserType");
    private static final byte[] USER_TBL_COL_CREATE_TIME = Bytes.toBytes("CreatedTime");

    private static final byte[] USER_TBL_COL_FMY_PWD = Bytes.toBytes("Password");
    private static final byte[] USER_TBL_COL_SALT = Bytes.toBytes("Salt");
    private static final byte[] USER_TBL_COL_HASH = Bytes.toBytes("Hash");

    private static Connection connection = null;

    public DB(String projectId, String instanceId) throws IOException {

        Configuration config = BigtableConfiguration.configure(projectId, instanceId);
        // Include the following line if you are using app profiles.
        // If you do not include the following line, the connection uses the
        // default app profile.
//        config.set(BigtableOptionsFactory.APP_PROFILE_ID_KEY, APP_PROFILE_ID);

        connection = BigtableConfiguration.connect(config);

        createTable(URL_TBL, new byte[][]{URL_TBL_COL_FMY_URL, URL_TBL_COL_FMY_OWNER});
        createTable(USER_TBL, new byte[][]{USER_TBL_COL_FMY_ACCOUNT, USER_TBL_COL_FMY_PWD});

    }

    private void createTable(byte[] tableId, byte[][] columnFamilies) throws IOException {
        Admin admin = connection.getAdmin();

        if (admin.tableExists(TableName.valueOf(tableId))) {
            System.out.println(tableId + " already exists");
            return;
        }

        HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(tableId));

        for (byte[] fmy : columnFamilies) {
            descriptor.addFamily(new HColumnDescriptor(fmy));
        }

        System.out.println("DB: Create table " + descriptor.getNameAsString());
        admin.createTable(descriptor);

        return;
    }

    public boolean createUser(User u) throws IOException {

        byte[] userIDb = Bytes.toBytes(u.getUserID());

        Table userTbl = connection.getTable(TableName.valueOf(USER_TBL));

        if (userTbl.exists(new Get(userIDb))) {
            return false;
        }

        Put user = new Put(userIDb);
        user.addColumn(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_EMAIL_ADDR, Bytes.toBytes(u.getEmailAddress()));
        user.addColumn(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_USER_TYPE, Bytes.toBytes(u.getUserType()));
        user.addColumn(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_CREATE_TIME, Bytes.toBytes(u.getCreatedTime()));
        user.addColumn(USER_TBL_COL_FMY_PWD,USER_TBL_COL_SALT, Bytes.toBytes(u.getSalt()));
        user.addColumn(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_HASH, Bytes.toBytes(u.getHash()));

        userTbl.put(user);

        return true;

    }

    public User getUser(String userID) throws IOException {
        Table userTbl = connection.getTable(TableName.valueOf(USER_TBL));

        byte[] userIDb = Bytes.toBytes(userID);

        if (userTbl.exists(new Get(userIDb))) {
            Result res = userTbl.get(new Get(userIDb));

            String email = Bytes.toString(res.getValue(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_EMAIL_ADDR));
            String userType = Bytes.toString(res.getValue(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_USER_TYPE));
            String createTime = Bytes.toString(res.getValue(USER_TBL_COL_FMY_ACCOUNT,USER_TBL_COL_CREATE_TIME));
            String salt = Bytes.toString(res.getValue(USER_TBL_COL_FMY_PWD,USER_TBL_COL_SALT));
            String hash = Bytes.toString(res.getValue(USER_TBL_COL_FMY_PWD,USER_TBL_COL_HASH));

            return new User(userID, email, userType, createTime, salt, hash);

        }

        return null;

    }

    public boolean createUrl(Url u) throws IOException {

        Table urlTbl = connection.getTable(TableName.valueOf(URL_TBL));

        Put url = new Put(Bytes.toBytes(u.getSha256Val()));

        url.addColumn(URL_TBL_COL_FMY_URL,URL_TBL_COL_LONG_URL, Bytes.toBytes(u.getLongUrl()));
        url.addColumn(URL_TBL_COL_FMY_URL,URL_TBL_COL_SHORT_URL, Bytes.toBytes(u.getShortUrl()));
        url.addColumn(URL_TBL_COL_FMY_URL,URL_TBL_COL_EXP_TIME, Bytes.toBytes(u.getExpireTime()));
        url.addColumn(URL_TBL_COL_FMY_OWNER,URL_TBL_COL_OWNER, Bytes.toBytes(u.getUserID()));

        urlTbl.put(url);

        return true;
    }

    public boolean touchUrl(String sha256) throws IOException {
        Table urlTbl = connection.getTable(TableName.valueOf(URL_TBL));
        byte[] sha256b = Bytes.toBytes(sha256);
        if (urlTbl.exists(new Get(sha256b))) {
            return true;
        }
        return false;
    }

    public Url getUrl(String sha256) throws IOException {

        byte[] sha256b = Bytes.toBytes(sha256);

        Table urlTbl = connection.getTable(TableName.valueOf(URL_TBL));

        if (urlTbl.exists(new Get(sha256b))) {

            Result res = urlTbl.get(new Get(sha256b));

            String longUrl = Bytes.toString(res.getValue(URL_TBL_COL_FMY_URL,URL_TBL_COL_LONG_URL));
            String shortUrl = Bytes.toString(res.getValue(URL_TBL_COL_FMY_URL,URL_TBL_COL_SHORT_URL));
            String expiredTime = Bytes.toString(res.getValue(URL_TBL_COL_FMY_URL,URL_TBL_COL_EXP_TIME));
            String userID = Bytes.toString(res.getValue(URL_TBL_COL_FMY_OWNER,URL_TBL_COL_OWNER));

            return new Url(sha256, longUrl, shortUrl, expiredTime, userID);
        }

        return null;
    }

}
