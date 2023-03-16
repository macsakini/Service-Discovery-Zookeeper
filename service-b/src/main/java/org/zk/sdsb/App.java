/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.zk.sdsb;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class App {
    private static ZooKeeper zk;
    private static ZookeeperConnection conn;

    public static void create(String path, byte[] data) throws InterruptedException, KeeperException {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    };

    public static Stat znode_exists(String path) throws InterruptedException, KeeperException {
        return zk.exists(path, true);
    }

    public static void main(String[] args) {
        String path = "/service-a";
        byte[] data = "Service-A".getBytes();
        try{
            conn = new ZookeeperConnection();
            zk = conn.connect("localhost");
            Stat stat = znode_exists(path);
            if(stat != null){
                System.out.println("Z-node exists");
                System.out.println(stat.getAversion());
            }else{
                create(path, data);
            }
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
