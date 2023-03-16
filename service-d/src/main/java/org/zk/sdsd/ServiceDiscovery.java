package org.zk.sdsd;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class ServiceDiscovery implements Watcher {

    private ZooKeeper zooKeeper;

    public ServiceDiscovery(String connectString) throws Exception {
        this.zooKeeper = new ZooKeeper(connectString, 5000, this);
    }

    @Override
    public void process(WatchedEvent event) {
        // Handle watch events, if necessary
    }

    public void close() throws Exception {
        zooKeeper.close();
    }

    public List<String> discoverServices(String serviceName) throws Exception {
        // Get the list of service nodes
        List<String> nodes = zooKeeper.getChildren("/services", false);

        // Filter the nodes for the specified service name
        nodes.removeIf(node -> !node.startsWith(serviceName));

        // Add a watch to the service nodes
        for (String node : nodes) {
            zooKeeper.exists("/services/" + node, this);
        }

        return nodes;
    }

    public static void main(String[] args) throws Exception {
        // Discover the service
        ServiceDiscovery discovery = new ServiceDiscovery("localhost:2181");

        List<String> services = discovery.discoverServices("myService");

        System.out.println("Services found: " + services);

        // Keep the program running to continue discovering services
        Thread.sleep(Long.MAX_VALUE);

        discovery.close();
    }
}
