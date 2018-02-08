# 微信接入模块

## 进度

- [x] 用于正确响应微信发送的 Token 验证的接口
- [x] 接收消息后首先进行数据源验证。即校验签名值
- [x] 明文模式接收并保存消息
- [x] 兼容模式接收并保存消息
- [x] 安全模式接收并保存消息
- [ ] 保存消息开了线程，但并非传入的对象拷贝，而是引用，导致保存的数据可能并非原始数据
- [ ] 三种模式的消息处理方法间关系混乱，需要梳理
- [ ] odd 模块接入
    - [ ] odd 所需要的 User Id 暂时使用 FromUserName
    - [x] 添加测试功能
    - [x] 测试公众号：aiyouheiha

## 依赖

- [x] eureka service
    - 不启用可设置 `eureka.client.enabled` 为 `false`
- [x] MongoDB
    - 未使用嵌入式，依赖

