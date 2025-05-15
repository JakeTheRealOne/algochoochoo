# Algochoochoo
Find the best path in public transport networks.

## Dependencies

[Maven](https://maven.apache.org/) needs to be installed on your system, it manages all dependencies. We use the content of:

- univocity-parser
- JTS java topology suite
- algs4
- JavaFX
- swing
- JXMapViewer2

### On MacOS (with Homebrew)

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

TODO changer en jar

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
  - `time`: Minimize the arrival time at t
  - `trips`: Minimize the number of trip changes

**Default:** `time`

##### --gtfs-path

Set the GTFS directory path.

**Value**: `string`

**Default:** `src/main/resources/GTFS`

##### --foot-radius

Set the radius for footpath search (in meters). A higher radius will result in more memory and more runtime but will be more accurate. The program won't consider footpaths with a distance higher than the radius

**Values:** `positive integers`

**Default:** `500`

Executing this example for comparing two radius and inspect its influence on the results.

##### --foot-weight, --metro-weight, etc.

The rest of the flags are used to set the weights for each transport type (FOOT, METRO, TRAM, TRAIN and BUS). A higher weight will result in less trip of this type in the result

**Values:** doubles in the interval `(0, 10]`

**Default:** `1.0`

## GUI

To run and show the graphical interface:

```shell
export _JAVA_AWT_WM_NONREPARENTING=1
mvn javafx:run
```

Notes:
- `_JAVA_AWT_WM_NONREPARENTING` must be set to 1 on Wayland/XWayland systems to avoid display bugs.
- A decent internet connection is required for the GUI.

## Documentation

- Report: doc/rapport.pdf
- Javadoc: target/site/index.html

TODO ne pas oublier d'inclure le GTFS dans le rendu final (ou sur github)
TODO ajouter algs4 à maven
TODO ajouter du testing
TODO changer vers jar
TODO ajouter un ex pour chaque flag
TODO ajouter un ex de base
TODO ajouter un ex pour les cas spécifiques

## Testing

### Example from Projet_Algorithmique_2_2025.pdf

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Alveringem Nieuwe Herberg\" Aubange 10:30:00"
```

we get

```txt
Taking BUS 50 from Alveringem Nieuwe Herberg (10:39:00) to Veurne Station perron 1 (10:52:00)
Walking from Veurne Station perron 1 (10:52:00) to Furnes (10:55:25)
Taking TRAIN IC from Furnes (11:01:00) to De Pinte (11:58:00)
Taking TRAIN IC from De Pinte (12:00:00) to Gand-Saint-Pierre (12:06:00)
Taking TRAIN IC from Gand-Saint-Pierre (12:24:00) to Bruxelles-Midi (12:56:00)
Taking TRAIN S1 from Bruxelles-Midi (13:00:00) to Linkebeek (13:11:00)
Taking TRAIN S19 from Linkebeek (13:12:00) to Boondael (13:22:00)
Walking from Boondael (13:22:00) to BOONDAEL GARE (13:23:55)
Taking TRAM 8 from BOONDAEL GARE (13:25:00) to BOITSFORT GARE (13:29:14)
Walking from BOITSFORT GARE (13:29:14) to Boitsfort (13:31:51)
Taking TRAIN IC from Boitsfort (13:32:00) to Profondsart (13:43:00)
Taking TRAIN IC from Profondsart (13:43:00) to Ernage (13:57:00)
Taking TRAIN IC from Ernage (13:57:00) to Lonzée (14:02:00)
Taking TRAIN IC from Lonzée (14:03:00) to Saint-Denis-Bovesse (14:05:00)
Taking TRAIN IC from Saint-Denis-Bovesse (14:05:00) to Namur (14:17:00)
Taking TRAIN P from Namur (14:20:00) to Natoye (14:32:00)
Taking TRAIN P from Natoye (14:32:00) to Viville (15:42:00)
Taking TRAIN P from Viville (15:42:00) to Arlon (15:44:00)
Taking TRAIN BUS from Arlon (15:47:00) to Athus (16:14:00)
Taking TRAIN P from Athus (16:21:00) to Aubange (16:27:00)
```


### Changing the footpath radius

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Paris Nord (FR)\" \"Amsterdam Cs (NL)\" 08:00:00 --foot-radius=500"
```

and

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Paris Nord (FR)\" \"Amsterdam Cs (NL)\" 08:00:00 --foot-radius=5000"
```

we get (respectively):

```txt
Taking TRAM 7 from MONTGOMERY (08:02:00) to LEOPOLD III (08:09:00)
Taking TRAM 62 from LEOPOLD III (08:10:00) to DA VINCI (08:18:00)
Walking from DA VINCI (08:18:00) to Bordet (08:22:17)
Taking TRAIN S5 from Bordet (08:25:00) to Eppegem (08:39:00)
Taking TRAIN IC from Eppegem (08:40:00) to Anvers-Central (09:04:00)
Walking from Anvers-Central (09:04:00) to Antwerpen Diamant Metro (09:07:00)
Taking TRAM 2 from Antwerpen Diamant Metro (09:07:00) to Antwerpen Gasthuishoeve (09:15:00)
Taking TRAM 6 from Antwerpen Gasthuishoeve (09:15:00) to Antwerpen Station Luchtbal (09:20:00)
Walking from Antwerpen Station Luchtbal (09:20:00) to Anvers-Luchtbal (09:22:47)
Taking TRAIN ECD from Anvers-Luchtbal (09:32:00) to MEER-GRENS (09:48:00)
Taking TRAIN ECD from MEER-GRENS (09:55:00) to Rotterdam CS (NL) (10:19:00)
Taking TRAIN ECD from Rotterdam CS (NL) (10:20:00) to Schiphol (NL) (10:42:00)
Taking TRAIN ECD from Schiphol (NL) (22:51:00) to Amsterdam Cs (NL) (23:08:00)
```

```text
Taking TRAM 7 from MONTGOMERY (08:02:00) to LEOPOLD III (08:09:00)
Taking TRAM 62 from LEOPOLD III (08:10:00) to DA VINCI (08:18:00)
Walking from DA VINCI (08:18:00) to Bordet (08:22:17)
Taking TRAIN S5 from Bordet (08:25:00) to Eppegem (08:39:00)
Taking TRAIN IC from Eppegem (08:40:00) to Anvers-Central (09:04:00)
Walking from Anvers-Central (09:04:00) to Antwerpen Diamant Metro (09:07:00)
Taking TRAM 2 from Antwerpen Diamant Metro (09:07:00) to Antwerpen Gasthuishoeve (09:15:00)
Taking TRAM 6 from Antwerpen Gasthuishoeve (09:15:00) to Antwerpen Station Luchtbal (09:20:00)
Walking from Antwerpen Station Luchtbal (09:20:00) to Anvers-Luchtbal (09:22:47)
Taking TRAIN ECD from Anvers-Luchtbal (09:32:00) to MEER-GRENS (09:48:00)
Taking TRAIN ECD from MEER-GRENS (09:55:00) to Rotterdam CS (NL) (10:19:00)
Taking TRAIN ECD from Rotterdam CS (NL) (10:20:00) to Amsterdam Sud (10:48:00)
Walking from Amsterdam Sud (10:48:00) to Amsterdam Cs (NL) (11:54:21)
```

---

Observation:\
The path with `radius = 5000m` is 11 hours earlier then the path with `radius = 500m`.

### Changing the priority

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Knokke Casino\" \"NAMUR Square Arthur Masson\" 14:14:14 --priority=time"
```

and

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Knokke Casino\" \"NAMUR Square Arthur Masson\" 14:14:14 --priority=trips"
```

we get (respectively):

```text
Walking from Knokke Casino (14:14:14) to Knokke Meerlaan (14:20:38)
Taking BUS 41 from Knokke Meerlaan (14:21:00) to Knokke AZ Zeno (14:30:00)
Taking BUS 48 from Knokke AZ Zeno (14:35:00) to Heist Station (14:48:00)
Walking from Heist Station (14:48:00) to Heist (14:50:42)
Taking TRAIN IC from Heist (14:53:00) to Bruges-Saint-Pierre (15:03:00)
Taking TRAIN IC from Bruges-Saint-Pierre (15:04:00) to Bruges (15:07:00)
Taking TRAIN EXP from Bruges (15:08:00) to Anderlecht (15:56:00)
Taking TRAIN IC from Anderlecht (15:57:00) to Bruxelles-Midi (16:02:00)
Taking TRAIN S10 from Bruxelles-Midi (16:02:00) to Bruxelles-Central (16:06:00)
Walking from Bruxelles-Central (16:06:00) to GARE CENTRALE (16:09:36)
Taking METRO 1 from GARE CENTRALE (16:10:04) to MERODE (16:16:02)
Walking from MERODE (16:16:02) to Mérode (16:18:08)
Taking TRAIN S4 from Mérode (16:20:00) to Etterbeek (16:29:00)
Taking TRAIN IC from Etterbeek (16:30:00) to Ernage (16:58:00)
Taking TRAIN IC from Ernage (16:59:00) to Namur (17:17:00)
Walking from Namur (17:17:00) to NAMUR Gare des bus - Quai 7 (17:19:39)
Taking BUS 41 from NAMUR Gare des bus - Quai 7 (17:21:00) to NAMUR Square Arthur Masson (17:25:00)
```

```text
Walking from Knokke Casino (14:14:14) to Knokke Scharpoord (14:22:58)
Taking BUS 41 from Knokke Scharpoord (14:23:00) to Brugge Station perron C8 (15:10:00)
Walking from Brugge Station perron C8 (15:10:00) to Bruges (15:13:59)
Taking TRAIN IC from Bruges (15:18:00) to Bruxelles-Nord (16:48:00)
Taking TRAIN P from Bruxelles-Nord (16:52:00) to Namur (17:54:00)
Walking from Namur (17:54:00) to NAMUR Gare des bus - Quai 7 (17:56:39)
Taking BUS 4 from NAMUR Gare des bus - Quai 7 (17:58:00) to NAMUR Square Arthur Masson (18:00:00)
```

---

Observations:\
The second path is smaller but arrives later

### Changing the GTFS path
(the second GTFS dir contains TOC datas, see doc/rapport.pdf)

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="MERODE DELTA 12:20:00 --gtfs-path=src/main/resources/GTFS"
```

and

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="Québec Montréal 08:40:00 --gtfs-path=src/test/resources/GTFS"
```

we get (respectively):

```text
Taking METRO 5 from MERODE (12:24:19) to DELTA (12:29:27)
```

```text
Taking TRAIN A from Québec (09:20:00) to Ottawa Sud (09:45:00)
Walking from Ottawa Sud (09:45:00) to Ottawa Nord (09:47:08)
Taking TRAIN B from Ottawa Nord (10:05:00) to Montréal (11:30:00)
```

### Changing Weights

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Paris Nord (FR)\" Ostende 02:00:00"
```

and the same path but the user hates TRAINS and loves BUS

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"Paris Nord (FR)\" Ostende 02:00:00 --train-weight=1.7 --bus-weight=0.2"
```

we get (respectively):

```text
Taking TRAIN OTC from Paris Nord (FR) (07:45:00) to AULNOYE AYMERIES (F) (10:04:00)
Taking TRAIN OTC from AULNOYE AYMERIES (F) (10:17:00) to QUEVY-FRONTIERE (10:30:00)
Taking TRAIN OTC from QUEVY-FRONTIERE (10:30:00) to Quévy (10:31:00)
Taking TRAIN OTC from Quévy (10:31:00) to Frameries (10:37:00)
Taking TRAIN OTC from Frameries (10:37:00) to Mons (10:42:00)
Taking TRAIN OTC from Mons (10:45:00) to Bruxelles-Midi (11:21:00)
Taking TRAIN IC from Bruxelles-Midi (11:29:00) to Gand-Saint-Pierre (12:00:00)
Taking TRAIN IC from Gand-Saint-Pierre (12:00:00) to Oostkamp (12:23:00)
Taking TRAIN IC from Oostkamp (12:23:00) to Bruges (12:26:00)
Taking TRAIN IC from Bruges (12:38:00) to Ostende (12:51:00)
```

```text
Taking TRAIN OTC from Paris Nord (FR) (07:45:00) to Creil - Bât Voyageurs (08:17:00)
Taking TRAIN OTC from Creil - Bât Voyageurs (08:48:00) to AULNOYE AYMERIES (F) (10:08:00)
Taking TRAIN OTC from AULNOYE AYMERIES (F) (10:17:00) to QUEVY-FRONTIERE (10:30:00)
Taking TRAIN OTC from QUEVY-FRONTIERE (10:30:00) to Quévy (10:31:00)
Taking TRAIN OTC from Quévy (10:31:00) to Genly (10:36:00)
Taking TRAIN OTC from Genly (10:37:00) to Mons (10:42:00)
Taking TRAIN OTC from Mons (10:45:00) to Nimy (10:47:00)
Walking from Nimy (10:47:00) to NIMY Rue Gérard (10:52:44)
Taking BUS 15 from NIMY Rue Gérard (10:56:00) to SOIGNIES SNCB (11:18:00)
Walking from SOIGNIES SNCB (11:18:00) to Soignies (11:20:27)
Taking TRAIN IC from Soignies (11:28:00) to Braine-le-Comte (11:35:00)
Taking TRAIN IC from Braine-le-Comte (11:36:00) to Lembeek (11:46:00)
Walking from Lembeek (11:46:00) to Lembeek Sancta Maria (11:49:29)
Taking BUS 2 from Lembeek Sancta Maria (11:53:00) to Halle Bergensepoort (12:01:00)
Walking from Halle Bergensepoort (12:01:00) to Halle Bergensepoort (12:02:50)
Taking BUS R53 from Halle Bergensepoort (12:05:00) to Leerbeek Pajottenland (12:27:00)
Taking BUS R53 from Leerbeek Pajottenland (12:27:00) to Gooik Kwakenbeek (12:36:00)
Taking BUS R42 from Gooik Kwakenbeek (12:36:00) to Gooik Wijngaardstraat (12:37:00)
Taking BUS 625 from Gooik Wijngaardstraat (12:37:00) to Teralfene Portugeesstraat (13:06:00)
Taking BUS 213 from Teralfene Portugeesstraat (13:07:00) to Aalst Station perron 5 (13:26:00)
Walking from Aalst Station perron 5 (13:26:00) to Aalst Station perron 3 (13:27:46)
Taking BUS 20 from Aalst Station perron 3 (13:28:00) to Aalst Gentsestraat (13:32:00)
Taking BUS 20 from Aalst Gentsestraat (13:32:00) to Ledeberg P+R Ledeberg (Hundelgemsesteenweg) (14:12:00)
Taking BUS 9a from Ledeberg P+R Ledeberg (Hundelgemsesteenweg) (14:12:00) to Gent Jubileumlaan (Verenigde-Natieslaan) (14:28:00)
Taking BUS 9b from Gent Jubileumlaan (Verenigde-Natieslaan) (14:29:00) to Gent Peerstraat (14:35:00)
Taking BUS 9a from Gent Peerstraat (14:36:00) to Gent Westerbegraafplaats (14:40:00)
Taking BUS 60 from Gent Westerbegraafplaats (14:42:00) to Lovendegem Binnenslag (14:55:00)
Walking from Lovendegem Binnenslag (14:55:00) to Lovendegem Binnenslag (14:58:04)
Taking BUS 50 from Lovendegem Binnenslag (15:09:00) to Lovendegem Meienbroek (15:10:00)
Taking BUS 50 from Lovendegem Meienbroek (15:10:00) to Eeklo Station perron 1 (15:24:00)
Taking BUS 50 from Eeklo Station perron 1 (15:24:00) to Brugge Gentpoort (Ring) (16:14:00)
Taking BUS 4 from Brugge Gentpoort (Ring) (16:14:00) to Brugge 't Zand perron A2 (16:24:00)
Taking BUS 4 from Brugge 't Zand perron A2 (16:24:00) to Sint-Andries Lange Molenstraat (16:38:00)
Walking from Sint-Andries Lange Molenstraat (16:38:00) to Sint-Andries Lange Molenstraat (16:39:42)
Taking BUS 30 from Sint-Andries Lange Molenstraat (16:44:00) to Varsenare Zeeweg (16:47:00)
Walking from Varsenare Zeeweg (16:47:00) to Varsenare Zeeweg (16:49:51)
Taking BUS 312 from Varsenare Zeeweg (16:55:00) to Oostende Station perron 10 (17:54:00)
Walking from Oostende Station perron 10 (17:54:00) to Ostende (17:58:15)
```

---

Observations:

|                  | Path 1 | Path 2 (custom weights) |
|------------------|:------:|:-----------------------:|
| Bus total time   |   0    |          5h44           |
| Train total time |  4h30  |          2h35           |

### No stop found

This test checks if the algorithm handles invalid s or t names.

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="does_not_exist oooooo 00:00:00"
```

we get:

```text
Error: At least one existing source and one existing target is required
```

### Source == Target

This test checks that the algorithm handles s == t situation.

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="BEEKKANT BEEKKANT 00:00:00"
```

we get:

```text
Empty path
```

### Invalid time format

This test checks that the algorithm handles invalid time formats.

with

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="AUMALE DELTA 0e:00:00"
```

or 

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="AUMALE DELTA xxxxxx"
```

we get:

```text
Error: Invalid time format (HH:MM:SS) : [0e:00:00]
```

or

```text
Error: Invalid time format (HH:MM:SS) : [xxxxxx]
```

### Multiday

This test checks that the algorithm is multiday (find solutions on multiple days).

with 

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="Aubange Knokke 23:00:00"
```

we get:

```text
Taking TRAIN L from Aubange (05:51:00) to Libramont (06:51:00)
Taking TRAIN L from Libramont (06:54:00) to Forrières (07:19:00)
Taking TRAIN L from Forrières (07:19:00) to Rochefort-Jemelle (07:22:00)
Taking TRAIN L from Rochefort-Jemelle (07:23:00) to Marloie (07:28:00)
Taking TRAIN IC from Marloie (07:37:00) to Ciney (07:53:00)
Taking TRAIN IC from Ciney (07:56:00) to Assesse (08:02:00)
Taking TRAIN IC from Assesse (08:02:00) to Naninne (08:07:00)
Taking TRAIN EXP from Naninne (08:07:00) to Dave-Saint-Martin (08:08:00)
Taking TRAIN IC from Dave-Saint-Martin (08:08:00) to Jambes-Est (08:09:00)
Taking TRAIN IC from Jambes-Est (08:11:00) to Rhisnes (08:19:00)
Taking TRAIN IC from Rhisnes (08:19:00) to Ernage (08:32:00)
Taking TRAIN IC from Ernage (08:32:00) to Mont-Saint-Guibert (08:36:00)
Taking TRAIN IC from Mont-Saint-Guibert (08:36:00) to Ottignies (08:43:00)
Taking TRAIN IC from Ottignies (08:44:00) to Bruxelles-Luxembourg (09:05:00)
Walking from Bruxelles-Luxembourg (09:05:00) to LUXEMBOURG (09:07:52)
Taking BUS 38 from LUXEMBOURG (09:09:00) to TRONE (09:11:00)
Walking from TRONE (09:11:00) to TRONE (09:12:52)
Taking METRO 2 from TRONE (09:13:02) to GARE DU MIDI (09:18:51)
Walking from GARE DU MIDI (09:18:51) to Bruxelles-Midi (09:22:04)
Taking TRAIN IC from Bruxelles-Midi (09:29:00) to Gand-Saint-Pierre (10:00:00)
Taking TRAIN IC from Gand-Saint-Pierre (10:00:00) to Oostkamp (10:23:00)
Taking TRAIN IC from Oostkamp (10:23:00) to Bruges (10:28:00)
Taking TRAIN IC from Bruges (10:30:00) to Bruges-Saint-Pierre (10:33:00)
Taking TRAIN IC from Bruges-Saint-Pierre (10:57:00) to Knokke (11:15:00)
```

---

Observation:\
leaving at 23:00:00, we arrive the next day at 11:15:00.

### Full test

This test checks all the arguments at once.

with 

```shell
mvn clean && mvn compile && mvn exec:java -Dexec.args="\"VAN BEETHOVEN\" \"GEROMPONT Avenue des Déportés 74\" 04:13:00 --foot-radius=200 --priority=trips --gtfs-file=src/main/resources/GTFS --foot-weight=3 --bus-weight=1.5 --train-weight=1.1 --tram-weight=1.2"
```

we get:

```text
Taking TRAM 81 from VAN BEETHOVEN (05:30:59) to LA CHASSE (06:07:00)
Walking from LA CHASSE (06:07:00) to ETTERBEEK Avenue de la Chasse (Av. Auderghem) (06:09:10)
Taking BUS 543 from ETTERBEEK Avenue de la Chasse (Av. Auderghem) (06:40:00) to AUDERGHEM Herrmann-Debroux (06:50:00)
Taking BUS 543 from AUDERGHEM Herrmann-Debroux (06:52:00) to GRAND-ROSIERE-HOTTOMONT Centre (08:07:00)
Taking BUS 148 from GRAND-ROSIERE-HOTTOMONT Centre (09:02:00) to GEROMPONT Avenue des Déportés 74 (09:07:00)
```


## Context and authors

Bilal Vandenberge

INFOF203 Project 2024-25 (Université libre de Bruxelles).