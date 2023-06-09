/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.zk.sdsa;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.zk.sds.ZookeeperConnection;

import java.util.List;

public class App {
    private static ZooKeeper zk;
    private static ZookeeperConnection conn;
    public static void create(String path, byte[] data) throws InterruptedException, KeeperException {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    };

    public static Stat znode_exists(String path) throws InterruptedException, KeeperException {
        return zk.exists(path, true);
    }

    public static List<String> discoverServices(String serviceName) throws InterruptedException, KeeperException {
        List<String> nodes = zk.getChildren("/services",true);
        nodes.removeIf(node->!node.startsWith(serviceName));
        for(String node: nodes){
            zk.exists("/services/" + node, new Watcher() {
                @Override
                public void process(WatchedEvent event) {}
            });
        }
        return nodes;
    }

    public static void main(String[] args) {
        String path = "/service-a";
        byte[] data = "Service-A".getBytes();
        try{
            conn = new org.zk.sds.ZookeeperConnection();
            zk = conn.connect("localhost");
            Stat stat = znode_exists(path);
            if(stat != null){
                Thread.sleep(Long.MAX_VALUE);
            }else{
                create(path, data);
            }
            conn.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

