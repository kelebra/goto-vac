# goto-vac
Simple vacation sync

[![Build Status](https://travis-ci.org/kelebra/goto-vac.svg?branch=master)](https://travis-ci.org/kelebra/goto-vac)

## Running build locally

```scala
cd goto-vac
sbt
project backend
~reStart
```

## Publishing

```scala
sbt "project backend" clean assembly deployHeroku
```
