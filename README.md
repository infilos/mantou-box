# Mantou Box - 馒头盒子

## New Module

1. Add module package under `com.infilos.mantou.views`.
2. In module package, implement `NewModel`/`NewModule`/`NewService`/`NewView`/`NewView.fxml`.
3. In `com.infilos.mantou.MantouBoxApplication::startReactiveApp`, register the new module.

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
