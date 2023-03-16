package org.zk.sdsd;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ServiceWatcher implements Watcher {
    private ZooKeeper zooKeeper;
    private String serviceName;
    private String servicePath;
    private String serviceInfo;

    public ServiceWatcher(String connectString, String serviceName) throws Exception {
        this.serviceName = serviceName;
        this.servicePath = "/services/" + serviceName;

        this.zooKeeper = new ZooKeeper(connectString, 5000, this);

        // Check if the service node exists and set a watch on it
        Stat stat = zooKeeper.exists(servicePath, this);

        if (stat != null) {
            // Get the service information and set a watch on the node
            byte[] data = zooKeeper.getData(servicePath, this, null);
            serviceInfo = new String(data);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.NodeDeleted) {
                // The service is down, handle the event
                serviceInfo = null;
            } else if (event.getType() == EventType.NodeDataChanged) {
                // The service information has changed, update the connection
                try {
                    byte[] data = zooKeeper.getData(servicePath, this, null);
                    serviceInfo = new String(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (event.getType() == EventType.NodeCreated) {
                // The service is up, get its information and set a watch on the node
                try {
                    byte[] data = zooKeeper.getData(servicePath, this, null);
                    serviceInfo = new String(data);
                    zooKeeper.exists(servicePath, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void close() throws Exception {
        zooKeeper.close();
    }

    public static void main(String[] args) throws Exception {
        ServiceWatcher watcher = new ServiceWatcher("localhost:2181", "myService");

        while (watcher.getServiceInfo() == null) {
            Thread.sleep(1000);
            System.out.println("Waiting for service to come up...");
        }

        System.out.println("Service is up: " + watcher.getServiceInfo());

        // Keep the program running to continue monitoring the service
        Thread.sleep(Long.MAX_VALUE);

        watcher.close();
    }
}