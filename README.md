# Mantou Box - 馒头盒子

## New Module

- Add module package under `com.infilos.mantou.views`.
- In module package, implement `NewModule`/`NewService`/`NewView`/`NewView.fxml`.
- In `com.infilos.mantou.views.ViewFactory`, add provider of `NewModule` and `NewView`.
- In `com.infilos.mantou.MantouBoxApplication::buildWorkbench`, register the new module.

## Build Locally

- sdk use java 17.0.1.12.1-amzn
- mvn javafx:run
- mvn install -Ppackage

## Github Release

1. Push commit.
2. Push tag: `git tag v1.x` & `git push origin v1.x`
3. Create release manually on Github or with Github cli.

```sh
gh auth login
gh release create v1.x
```

## Features

## Relate Projects

- ReactiveDeskFX：编程框架
- WorkbenchFX：基础面板
- MaterialFX：界面组件
- PreferencesFX：配置功能组件
- 数据可视化：
  - tilesfx
  - jfreechart
  - tablesaw

