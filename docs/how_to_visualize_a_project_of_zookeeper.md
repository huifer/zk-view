# 如何做一个zookeeper可视化项目
> author: [huifer](https://github.com/huifer)
> gir_repo: https://github.com/huifer/zk-view
>




## 技术选型
- 后端技术
    - spring boot 
    - curator-framework
- 前端技术
    - vue
    - element-ui


选型说明:
- spring boot 快速搭建 rest-api
- curator-framework 与zookeeper 进行交互的jar
- vue element-ui 前端展示


## 实现
- curator-framework 的使用具体查看: [CSDN](https://blog.csdn.net/staHuri/article/details/106982413)
- zookeeper 的数据存储是树结构存储, 因此选择 element-ui 的树形控件比较合理. [element-ui-tree](https://element.eleme.cn/#/zh-CN/component/tree)
### 创建树结构
- 选好了数据格式接下来就是处理出这份数据了. 创建出这个树结构
- 定义树结构的 java 对象

```java
  @Data
  @AllArgsConstructor
  public static class Hc {

    private String path;
    private String showName;
    private List<Hc> child;

  }
```

- 造树的方法

```java
   private static List<Hc> child(List<String> strings, String path) {
     List<Hc> hc = new ArrayList<>();
 
     for (String string : strings) {
       if (path.equals("/")) {
         hc.add(new Hc(path + string, string, null));
       } else {
 
         Hc hc1 = new Hc(path + "/" + string, string, null);
         hc.add(hc1);
       }
     }
     return hc;
   }
  public static void calcTree(CuratorFramework curatorFramework, String path, Hc c) throws Exception {
    List<String> strings = curatorFramework.getChildren().forPath(path);
    c.setPath(path);
    c.setChild(child(strings, path));

    for (Hc hc : c.getChild()) {
      calcTree(curatorFramework, hc.getPath(), hc);
    }

  }


  public Hc tree() throws Exception {

    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    Hc hc = new Hc(null, "/", null);
    calcTree(build, "/", hc);
    build.close();
    return hc;
  }
```

- 通过这样的方式我们就可以获取这个树结构了. 

```json
{"path":"/","showName":"/","child":[{"path":"/a","showName":"a","child":[{"path":"/a/a_1","showName":"a_1","child":[{"path":"/a/a_1/a_1_1","showName":"a_1_1","child":[]}]},{"path":"/a/a_2","showName":"a_2","child":[]}]},{"path":"/temp","showName":"temp","child":[]},{"path":"/zookeeper","showName":"zookeeper","child":[{"path":"/zookeeper/config","showName":"config","child":[]},{"path":"/zookeeper/quota","showName":"quota","child":[]}]},{"path":"/d","showName":"d","child":[]}]}
```


### 单个节点的信息
- 节点信息再次作者定义三个信息
    1. stat: 状态
    1. data: 数据
    1. type: 节点类型
        1. 持久化节点
        1. 临时节点
        
        
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeInfo {

  private Stat stat;
  private String data;

  /**
   * 持久化节点,临时节点
   */
  private String type;

}
```

- 获取信息的方法

```json
  public Object info(String path) throws Exception {
    CuratorFramework build = CuratorFrameworkFactory.builder().connectString(ZkConfig.getIP_PORT())
        .sessionTimeoutMs(30000)
        .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
    build.start();
    // 数据
    Stat stat = new Stat();
    byte[] bytes = build.getData().storingStatIn(stat).forPath(path);

    String data;
    if (bytes != null && bytes.length > 0) {

      data = new String(bytes);
    } else {
      data = "";
    }
    build.close();
    return new NodeInfo(stat, data, nodeType(stat));
  }
```

- controller 就不在此进行贴代码了有兴趣的可以查看[zk-view](https://github.com/huifer/zk-view)


### 前端展示

- 前端不是很熟练,就直接贴一贴代码就过了. 后续各位如果需要做创建节点的操作可以自行开发.

```html
<!doctype html>
<html lang="zh">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>zk-view</title>
  <script src="https://cdn.jsdelivr.net/npm/vue"></script>
  <!-- 引入样式 -->
  <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
  <!-- 引入组件库 -->
  <script src="https://unpkg.com/element-ui/lib/index.js"></script>
  <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
</head>
<body>
<div id="app">
  <el-col :span="12">
    <el-tree width="200px" :data="og" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
  </el-col>

  <div>
    <el-col :span="12">
      <h3>状态</h3>
      {{message}}
      
      <h3>数据</h3>

      <el-input
          type="textarea"
          :rows="2"
          placeholder=""
          v-model="node_info.data">
      </el-input>

      <span>节点类型: {{node_type}}</span>

    </el-col>


  </div>
</div>

</body>

<script>

  var app = new Vue({
    el: '#app',
    data: {
      radio: '1',
      message: "hello",
      node_type: "",

      og: [],
      defaultProps: {
        children: 'child',
        label: 'showName'
      },
      node_info: {},
    },
    methods: {
      handleNodeClick: function (data) {
        console.log("当前选中的节点是", data.path)
        axios.get("/node/get/info", {params: {path: data.path}}).then(response => {
          this.node_info = response.data.data
          this.message = response.data.data.stat;
          this.node_type = response.data.data.type
        }).catch(function (error) {
          console.log(error);
        });

      },
      tree: function () {
        axios.get('/node/tree').then(response => {
          console.log("ogog");
          console.log(this.og);
          this.og = [response.data.data];
          console.log(this.og);

        }).catch(function (error) {
          console.log(error);
        });

      }
    },
    created: function () {
      this.tree();
      this.node_info = {
        state: {
          czxid: 2,
          mzxid: 0,
          ctime: 0,
          mtime: 0,
          version: 0,
          cversion: 0,
          aversion: 0,
          ephemeralOwner: 0,
          dataLength: 0,
          numChildren: 0,
          pzxid: 0
        },
        data: "数据",
        type: "临时节点"
      }

    }
  })


</script>
</html>
```