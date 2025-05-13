# Algochoochoo
Find the best path in public transport networks.

## Dependencies

[Maven](https://maven.apache.org/) needs to be installed on your system, it manages all dependencies. We use the content of:

- univocity-parser
- algs4 (preinstalled)
- javaFX
- JXMapViewer2
- swing

#### On MacOS (with Homebrew)

```shell
brew install maven
```

### On Ubuntu/Debian

```shell
sudo apt install maven
```

### On Arch Linux

```shell
sudo pacman -S maven
```

### On NixOS

```shell
nix-shell -p maven
```

### On Windows

#### with Chocolatey

```shell
choco install maven
```

#### with Scoop

```shell
scoop install main/maven
```

## Compilation

```shell
mvn compile
```

## Execution

### CLI

```shell
mvn exec:java -Dexec.args="s t h [--priority] [--gtfs-path] [--foot-radius] [--foot-weight] [--metro-weight] [--train-weight] [--tram-weight] [--bus-weight]"
```

To find the best path from stop s to stop t at h

- s: The name of at least one stop in the network
- t: The name of at least one stop in the network
- h: A time in the format HH:MM:SS

#### Flags

##### --priority

Set the priority criteria of the algorithm.

**Values:**
  - time: Minimize the arrival time at t
  - trips: Minimize the number of trip changes

**Default:** time

**Example:**
```shell
mvn exec:java -Dexec.args="DELTA MERODE 14:00:00 --priority=time"
mvn exec:java -Dexec.args="DELTA MERODE 14:00:00 --priority=trips"
```

##### --gtfs-path

Set the GTFS directory path.

Value: string

**Default:** src/main/resources/GTFS

**Example:**
```shell
mvn exec:java -Dexec.args="DELTA MERODE 08:00:00 --priority=src/main/resources/GTFS"
mvn exec:java -Dexec.args="Québec Montréal 12:50:00 --priority=src/test/resources/GTFS"
```

##### --foot-radius

Set the radius for footpath search (in meters). A higher radius will result in more memory and more runtime but will be more accurate. The program won't consider footpaths with a distance higher than the radius

**Values:** positive integers

**Default:** 500

**Example:**
```shell
mvn exec:java -Dexec.args="Aubange Knokke 07:30:00 --foot-radius=50"
mvn exec:java -Dexec.args="Aubange Knokke 07:30:00 --foot-radius=500"
```
Executing this example for comparing two radius and inspect its influence on the results.

##### --foot-weight, --metro-weight, etc.

The rest of the flags are used to set the weights for each transport type (FOOT, METRO, TRAM, TRAIN and BUS). A higher weight will result in less trip of this type in the result

**Values:** doubles in the interval (0, 10]

**Default:** 1.0

**Example:**
```shell
mvn exec:java -Dexec.args="\"Alveringem Nieuwe Herberg\" Aubange 10:30:00 --foot-weight=9 --bus-weight=2"
mvn exec:java -Dexec.args="\"Paris Nord (FR)\" \"Amsterdam Sud\" 11:11:11 --foot-weight=3 --bus-weight=1.5 --metro-weight=1.1 --train-weight=1.0 --tram-weight=1.3"
```

## GUI

To run and show the graphical interface:
```shell
mvn javafx:run
```

Note: A decent internet connection is required for the GUI.

## Documentation

- Report: doc/rapport.pdf
- Javadoc: target/site/index.html

TODO trier les connections par heure de départ
TODO passer à l'implementation par ajout incrémental
TODO ajouter le fiboheap
TODO ne pas oublier d'inclure le GTFS dans le rendu final (ou sur github)

# Context and authors

Bilal Vandenberge

INFOF203 Project 2024-25 (Université libre de Bruxelles).