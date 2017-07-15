### 项目背景

> 现在的想法只是做一个自己的小网站，记录一些照片、写一些文章。在业余时间来弄这些东西，也是新学一些新的知识，了解新的框架，了解一个网站的搭建过程。

---

### 相关技术

- spring-boot、hibernate
- 数据库 ： MySQL 、 redis
- 前端模板引擎freemarker
- 前端框架采用jQuery + layerUI  [点击查看layerUI开发文档](http://www.layui.com/doc/)
- 存储使用腾讯的 COS存储 [点击查看COS存储Java开发文档](https://www.qcloud.com/document/product/436/6273)

---

### 项目模块说明

#### 账户模块
- 注册、登录、登出
- 个人资料设置、上传头像、密码忘记等功能

#### 相册模块
    （暂时就是相册吧，说不定以后就是个云盘了）
- 上传图片（文件）
- 下载图片
- 为了方便管理，加个文件夹
- 文件管理（重命名、删除等等）

#### 文章模块

- 写文章、删除文章、修改文章、分页查询、文章detail CRUD不说了。
- 文章的评论 、点赞

---

#### 部分接口或返回值说明

- 统一AJAX 返回值说明(即ResultInfo对象)

code|含义|数据类型|备注|
--|--|--|--|
success|是否请求成功|Boolean| |
data|返回的数据对象，具体可以参见接口返回数据类型| Object| |
resultCode|返回结果代码|String | 统一401表示未登录|
msg|如果请求失败返回的失败原因| String| | 
otherInfoList|其他返回信息|List(Array)| | 


- COS存储code返回值说明

code|含义|
--|--|
0|成功|
-1|输入参数错误, 例如输入的本地文件路径不存在, cos文件路径不符合规范|
-2|网络错误, 如404等|
-3|连接cos时发生异常，如连接超时|

- StorageGroup说明

name(bucketName)|含义|
--|--|
bubu|测试存储组，如果存储时不传，默认存在这里|
user|用户的相关信息，比如头像等|
photo|相册功能存储组|


---

#### 更新日志

- 2017/05/22 项目搭建、数据库配置、腾讯存储配置

- 2017/05/24 统一异常处理、velocity head 设置、设置了默认首页

- 2017/05/25 测试了文件上传的功能，用的是layerUI的组件，登录拦截完成

- 2017/05/26 文件下载接口添加、相册表构建

- 2017/05/30 上传文件大小限制调整 调整至50MB 在upload页面可以进行上传完毕即可预览图片；登录、注册、登出前端简单功能实现，暂时还没有做前端验证，验证都放在了后端，后面慢慢加上

- 2017/05/31 账户设置功能添加，相册上传后台代码实现；前端模板引擎改用freemarker，新增常量类

- 2017/06/01 批量文件上传接口，存储表 添加fileName字段，在下载文件的时候会用到。下载文件功能优化，登录优化（添加returnURL的支持），日志文件保存到本地，上传文件过程中的问题优化。

- 2017/06/03 密码MD5加密，加入了HTTP调用接口工具类、百度地图的API接口，可用于根据经纬度查询当前位置信息

- 2017/06/09 ORM框架改成了hibernate，Spring Data JPA 处理一些简单的逻辑是非常方便，但是执行复杂的动态SQL的时候就显得有点麻烦了，为了不让这个坑越来越深，我就毅然决然的把ORM框架换成了hibernate，目前在查询条件非常多的情况下，也有一套通用的查询方式，使用起来非常方便！

- 2017/06/12 写文章、修改文章等接口

- 2017/06/13 新增文章评论表

- 2017/06/14 文章编写改为markdown，markdown语法简单，用较少的编写量达到更好的展示效果，网站首页样式调整，首页终于有个样子了

- 2017/06/16 网站的样式大改版，现在越来越好看了，没以前那么简陋（丑陋）了，今天添加了邮件相关的东西，用于发送验证码，短信验证码以前也考虑过，但是好贵啊，不想花钱，就用邮箱验证码了。目前的邮箱是使用的我自己的QQ邮箱，我已经申请了网站的QQ邮箱，但是14天后才能使用。等着吧。

- 2017/06/22 redis新增、在线用户统计，不需要登录的URL放在数据库中管理

- 2017/07/15 手机版页面搭建