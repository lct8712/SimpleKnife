# SimpleKnife

[ButterKnife](https://github.com/JakeWharton/butterknife) 源码学习

## Inject
inject module 在运行时动态解析注解。
支持 InjectView 和 InjectClick。

参考：https://blog.csdn.net/qq_23547831/article/details/51713824

## Bind
bind moudle 在编译时用 APT 生成代码，运行时调用生成的代码。

支持 BindView & BindOnClick & BindString。

参考：
  - [Android APT（编译时代码生成）最佳实践](https://joyrun.github.io/2016/07/19/AptHelloWorld/)
  - [使用编译时注解简单实现类似 ButterKnife 的效果 - CSDN博客](https://blog.csdn.net/u011240877/article/details/74490201)
