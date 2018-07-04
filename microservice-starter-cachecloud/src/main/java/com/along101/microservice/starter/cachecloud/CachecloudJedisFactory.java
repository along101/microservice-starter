package com.along101.microservice.starter.cachecloud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yinzuolong on 2017/9/20.
 */
public class CachecloudJedisFactory extends JedisConnectionFactory {

    private String appId;

    private int http_conn_timeout = 3000;
    private int http_socket_timeout = 5000;
    private String client_version = "1.0";
    private String domain_url = "http://cache.along101corp.com";
    private String redis_cluster_suffix = "/cache/client/redis/cluster/%s.json?clientVersion=";
    private String redis_sentinel_suffix = "/cache/client/redis/sentinel/%s.json?clientVersion=";
    private String redis_standalone_suffix = "/cache/client/redis/standalone/%s.json?clientVersion=";
    private String cachecloud_report_url = "/cachecloud/client/reportData.json";

    /**
     * jedis连接超时(单位:毫秒)
     */
    private int connectionTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * jedis读写超时(单位:毫秒)
     */
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;

    /**
     * 节点定位重试次数:默认3次
     */
    private int maxRedirections = 5;

    private String type;


    /**
     * 初始化standalone的参数 host  port
     *
     * @param appId
     * @param domain_url
     */
    public CachecloudJedisFactory(String appId, String domain_url) {
        super();
        String redisStandaloneUrl = domain_url + redis_standalone_suffix + client_version;
        String url = String.format(redisStandaloneUrl, appId);
        String response = doGet(url, "UTF-8", http_conn_timeout, http_socket_timeout);
        /**
         * 心跳返回的请求无效；
         */
        JSONObject responseJson = null;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("read json from response error, appId: %s.",
                    appId));
        }
        /**
         * 从心跳中提取HostAndPort，构造JedisPool实例；
         */
        String instance = responseJson.getString("standalone");
        if (StringUtils.isEmpty(instance)) {
            throw new IllegalArgumentException("redis standalone appId is not exist");
        }
        String[] instanceArr = instance.split(":");
        if (instanceArr.length != 2) {
            throw new IllegalArgumentException(String.format("instance info is invalid, instance: %s, appId: %s, continue..."
                    , instance, appId));
        }
        setHostName(instanceArr[0]);
        setPort(Integer.parseInt(instanceArr[1]));
    }

    public CachecloudJedisFactory(String appId, String domain_url, JedisPoolConfig poolConfig) {
        //实例化空对象RedisClusterConfiguration，才可以回调父类的方法createCluster()来初始化JedisCluster
        super(new RedisClusterConfiguration(), poolConfig);
        this.appId = appId;
        this.domain_url = domain_url;

    }

    protected JedisCluster createCluster(RedisClusterConfiguration clusterConfig, GenericObjectPoolConfig poolConfig) {
        return createClusterByCC();
    }

    private JedisCluster createClusterByCC() {
        String redisClsuterUrl = domain_url + redis_cluster_suffix + client_version;
        String url = String.format(redisClsuterUrl, appId);
        String response = doGet(url, "UTF-8", http_conn_timeout, http_socket_timeout);
        HeartbeatInfo heartbeatInfo = null;
        try {
            heartbeatInfo = JSON.parseObject(response, HeartbeatInfo.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("remote build error");
        }

        /** 检查客户端版本 **/
        if (heartbeatInfo.getStatus() == -1) {
            throw new IllegalArgumentException(heartbeatInfo.getMessage());
        }

        Set<HostAndPort> nodeList = new HashSet<HostAndPort>();
        //形如 ip1:port1,ip2:port2,ip3:port3
        String nodeInfo = heartbeatInfo.getShardInfo();
        //为了兼容,如果允许直接nodeInfo.split(" ")
        nodeInfo = nodeInfo.replace(" ", ",");
        String[] nodeArray = nodeInfo.split(",");
        for (String node : nodeArray) {
            String[] ipAndPort = node.split(":");
            if (ipAndPort.length < 2) {
                continue;
            }
            String ip = ipAndPort[0];
            int port = Integer.parseInt(ipAndPort[1]);
            nodeList.add(new HostAndPort(ip, port));
        }
        return new JedisCluster(nodeList, connectionTimeout, soTimeout, maxRedirections, getPoolConfig());
    }

    /**
     * @param link
     * @param encoding
     * @return
     */
    private String doGet(String link, String encoding, int connectTimeout, int readTimeout) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            // conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedInputStream in = new BufferedInputStream(
                    conn.getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int i = 0; (i = in.read(buf)) > 0; ) {
                out.write(buf, 0, i);
            }
            out.flush();
            String s = new String(out.toByteArray(), encoding);
            return s;
        } catch (Exception e) {
            throw new IllegalArgumentException("connect cachecloud error.");
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClient_version() {
        return client_version;
    }

    public void setClient_version(String client_version) {
        this.client_version = client_version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setHttp_conn_timeout(int http_conn_timeout) {
        this.http_conn_timeout = http_conn_timeout;
    }

    public void setHttp_socket_timeout(int http_socket_timeout) {
        this.http_socket_timeout = http_socket_timeout;
    }

    public void setDomain_url(String domain_url) {
        this.domain_url = domain_url;
    }

    public void setRedis_cluster_suffix(String redis_cluster_suffix) {
        this.redis_cluster_suffix = redis_cluster_suffix;
    }

    public void setRedis_sentinel_suffix(String redis_sentinel_suffix) {
        this.redis_sentinel_suffix = redis_sentinel_suffix;
    }

    public void setRedis_standalone_suffix(String redis_standalone_suffix) {
        this.redis_standalone_suffix = redis_standalone_suffix;
    }

    public void setCachecloud_report_url(String cachecloud_report_url) {
        this.cachecloud_report_url = cachecloud_report_url;
    }

    private static class HeartbeatInfo {

        /**
         * 应用id
         */
        private long appId;

        /**
         * 实例个数
         */
        private int shardNum;

        /**
         * 分配信息
         */
        private String shardInfo;

        /**
         * 应用状态
         */
        private int status;

        /**
         * 消息
         */
        private String message;

        public long getAppId() {
            return appId;
        }

        public void setAppId(long appId) {
            this.appId = appId;
        }

        public int getShardNum() {
            return shardNum;
        }

        public void setShardNum(int shardNum) {
            this.shardNum = shardNum;
        }

        public String getShardInfo() {
            return shardInfo;
        }

        public void setShardInfo(String shardInfo) {
            this.shardInfo = shardInfo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "HeartbeatInfo{" + "appId=" + appId + ", shardNum=" + shardNum
                    + ", shardInfo='" + shardInfo + '\'' + ", status=" + status
                    + ", message='" + message + '\'' + '}';
        }
    }

}