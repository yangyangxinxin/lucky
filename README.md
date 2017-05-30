
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



