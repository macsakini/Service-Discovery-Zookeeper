package org.zk.sdsc;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperConnection {
    private ZooKeeper zoo;
    CountDownLatch latch = new CountDownLatch(1);

    public ZooKeeper connect(String host) throws InterruptedException, IOException {
        zoo = new ZooKeeper(host, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState() == Event.KeeperState.SyncConnected){
                    latch.countDown();
                }
            }
        });
        latch.await();
        return zoo;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
