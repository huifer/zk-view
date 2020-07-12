package org.huifer.zkview.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.zookeeper.common.X509Exception.SSLContextException;
import org.huifer.zkview.model.ResultVO;
import org.huifer.zkview.model.TableEntity;
import org.huifer.zkview.model.ZookeeperState;
import org.huifer.zkview.model.req.IpPortReq;
import org.huifer.zkview.service.IZookeeperStateService;
import org.huifer.zkview.service.impl.IZookeeperStateServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class ZookeeperStateController {

    IZookeeperStateService zookeeperStateService = new IZookeeperStateServiceImpl();

    /**
     * 单个节点的详情
     *
     * @param req
     * @return
     * @throws IOException
     * @throws SSLContextException
     */
    @PostMapping("/zk")
    public ResultVO zkState(
            @RequestBody IpPortReq req
    ) throws IOException, SSLContextException {
        ZookeeperState state = zookeeperStateService.srvr(req.getIp(), req.getPort());
        return new ResultVO("ok", state, 200);
    }

    /**
     * 列表
     *
     * @return
     * @throws IOException
     * @throws SSLContextException
     */
    @PostMapping("/mntr")
    public ResultVO mntr() throws IOException, SSLContextException {
        List<String> hosts = new ArrayList<>();
        hosts.add("localhost");
        hosts.add("127.0.0.1");
        hosts.add("127.0.0.1");
        hosts.add("127.0.0.1");
        return new ResultVO("ok", clusterInfo(hosts), 200);
    }

    /**
     * 单个节点的配置
     *
     * @param req
     * @return
     * @throws IOException
     * @throws SSLContextException
     */
    @PostMapping("conf")
    public ResultVO conf(
            @RequestBody IpPortReq req
    ) throws IOException, SSLContextException {
        return new ResultVO("ok", zookeeperStateService.conf(req.getIp(), req.getPort()), 200);
    }

    /**
     * 单个节点的环境
     *
     * @param req
     * @return
     * @throws IOException
     * @throws SSLContextException
     */
    @PostMapping("envi")
    public ResultVO envi(
            @RequestBody IpPortReq req
    ) throws IOException, SSLContextException {
        return new ResultVO("ok", zookeeperStateService.envi(req.getIp(), req.getPort()), 200);
    }

    /**
     * 集群信息
     */
    private TableEntity clusterInfo(List<String> hosts)
            throws IOException, SSLContextException {
        List<Map<String, String>> query = new ArrayList<>();
        List<String> head = new ArrayList<>();
        head.add("配置");

        for (String host : hosts) {
            Map<String, String> localhost1 = zookeeperStateService.mntr(host, 2181);
            query.add(localhost1);
            head.add(host);
        }

        Map<String, List<String>> body = new HashMap<>();
        Map<String, String> map = query.get(0);
        for (String s : map.keySet()) {
            List<String> one = new ArrayList<>();
            one.add(s);
            body.put(s, one);
        }

        for (Map<String, String> stringStringMap : query) {
            stringStringMap.forEach(
                    (k, v) -> {
                        boolean b = body.containsKey(k);
                        if (b) {
                            body.get(k).add(v);
                        }
                    }
            );
        }

        Collection<List<String>> values = body.values();

        List<List<String>> res = new ArrayList<>();

        for (List<String> value : values) {
            res.add(value);
        }
        return new TableEntity(head, res);

    }
}
