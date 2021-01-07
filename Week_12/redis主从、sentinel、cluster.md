1.1 主从复制：

使用docker直接配置，具体配置如下：

启动主节点

docker run -itd --name redis-6379  -p 6379:6379 redis --appendonly yes --protected-mode no

启动从节点1

docker run -itd --name redis-6380  -p 6380:6379 redis --appendonly yes --protected-mode no
docker exec -it redis-6380 /bin/bash
$redis-cli
replicaof 172.19.16.1 6379
# tip：Redis5.0之前，主从配置命令使用slaveof
# 使用命令info查看主从是否连接上，简介内容如下，表示已经连接上
info replication
# Replication
role:slave
master_host:172.19.16.1
master_port:6379
master_link_status:up

1.2 sentinel 高可用

使用docker-compose进行配置

redis1.conf
bind 0.0.0.0
protected-mode no
port 6379

redis2.conf
bind 0.0.0.0
protected-mode no
port 6380
replicaof 127.0.0.1 6379

redis3.conf
bind 0.0.0.0
protected-mode no
port 6381
replicaof 127.0.0.1 6379

sentinel1.conf
port 26379
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 180000
sentinel parallel-syncs mymaster 1

sentinel2.conf
port 26380
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 180000
sentinel parallel-syncs mymaster 1

sentinel3.conf
port 26381
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 10000
sentinel failover-timeout mymaster 180000
sentinel parallel-syncs mymaster 1

docker-compose.yml
version: '3'
services:
redis1:
image: redis
container_name: redis1
ports:
- 6379:6379
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis1.conf:/etc/redis/redis.conf

redis2:
image: redis
container_name: redis2
ports:
- 6380:6380
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis2.conf:/etc/redis/redis.conf

redis3:
image: redis
container_name: redis3
ports:
- 6381:6381
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis3.conf:/etc/redis/redis.conf

sentinel1:
image: redis
container_name: sentinel01
ports:
- 26379:26379
network_mode: host
command: redis-sentinel /etc/redis/sentinel.conf
volumes:
- ./sentinel1.conf:/etc/redis/sentinel.conf

sentinel2:
image: redis
container_name: sentinel02
ports:
- 26380:26380
network_mode: host
command: redis-sentinel /etc/redis/sentinel.conf
volumes:
- ./sentinel2.conf:/etc/redis/sentinel.conf

sentinel3:
image: redis
container_name: sentinel03
ports:
- 26381:26381
network_mode: host
command: redis-sentinel /etc/redis/sentinel.conf
volumes:
- ./sentinel3.conf:/etc/redis/sentinel.conf

docker-compose up 启动docker-compse

通过命令docker stop redis1手动停止主节点，再查看redis2，redis3节点信息，可以看到，主节点已经转换到redis3

1.3 Cluster 集群

redis节点配置文件，一共6个节点，端口分别是6379,6380,6381,6382,6383,6384，配置文件以redis1为例，其他5个节点类似：

redis1.conf
bind 0.0.0.0
protected-mode no
port 6379
cluster-enabled yes
cluster-config-file nodes-6379.conf
cluster-require-full-coverage no

docker-compose.yml
version: '3'
services:
redis1:
image: redis
container_name: redis1
ports:
- 6379:6379
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis1.conf:/etc/redis/redis.conf

redis2:
image: redis
container_name: redis2
ports:
- 6380:6380
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis2.conf:/etc/redis/redis.conf

redis3:
image: redis
container_name: redis3
ports:
- 6381:6381
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis3.conf:/etc/redis/redis.conf

redis4:
image: redis
container_name: redis4
ports:
- 6382:6382
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis4.conf:/etc/redis/redis.conf

redis5:
image: redis
container_name: redis5
ports:
- 6383:6383
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis5.conf:/etc/redis/redis.conf

redis6:
image: redis
container_name: redis6
ports:
- 6384:6384
network_mode: host
command: redis-server /etc/redis/redis.conf
volumes:
- ./redis6.conf:/etc/redis/redis.conf

使用docker-compose up 启动docker compose配置文件

创建redis cluster

# 进入容器
docker exec -it redis1 /bin/bash
# 创建redis 集群
# replicas 1 表示集群中的每个主节点创建一个从节点
redis-cli --cluster create 127.0.0.1:6379 127.0.0.1:6380 127.0.0.1:6381 127.0.0.1:6382 127.0.0.1:6383 127.0.0.1:6384 --cluster-replicas 1

# 连接至某个节点
root@docker-desktop:/usr/local/bin# redis-cli
# 查看集群信息
127.0.0.1:6379> cluster info
# 查看集群节点
127.0.0.1:6379> cluster nodes