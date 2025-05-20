# Template for GTFS files

The parser of the project is reading network informations from a GTFS directory.

A valid GTFS directory contains a directory for each agency (or country or whatever) that contains 5 necessary CSV files: 'stops.csv', 'routes.csv', 'trips.csv', 'stop_times.csv'.

Each CSV file is structured with abreged GTFS columns. The order of the columns has to be respected.

'stops.csv' columns: (stop_id,stop_name,stop_lat,stop_lon). See the columns description [here](https://gtfs.org/documentation/schedule/reference/#stopstxt).

'routes.csv' columns: (route_id,route_short_name,route_long_name,route_type). See the columns description [here](https://gtfs.org/documentation/schedule/reference/#routestxt).

'trips.csv' columns: (trip_id,route_id). See the columns description [here](https://gtfs.org/documentation/schedule/reference/#tripstxt).

'stop_times.csv' columns: (trip_id,departure_time,stop_id,stop_sequence). See the columns description [here](https://gtfs.org/documentation/schedule/reference/#stop_timestxt).



## Exemple: Belgium GTFS datas

Belgium counts 4 main public transport agencies: STIB, SNCB, De Lijn and TEC. The GTFS directory structure will look like this:

```text
  GTFS
  ├── DELIJN
  │ ├── routes.csv
  │ ├── stops.csv
  │ ├── stop_times.csv
  │ └── trips.csv
  ├── SNCB
  │ ├── routes.csv
  │ ├── stops.csv
  │ ├── stop_times.csv
  │ └── trips.csv
  ├── STIB
  │ ├── routes.csv
  │ ├── stops.csv
  │ ├── stop_times.csv
  │ └── trips.csv
  └── TEC
  ├── routes.csv
  ├── stops.csv
  ├── stop_times.csv
  └── trips.csv
```

---

`stops.csv` of SNCB:

```csv
stop_id,stop_name,stop_lat,stop_lon
SNCB-8007817,Berlin Ostbahnhof (DE),52.5106,13.435
SNCB-8014008,Mannheim Hbf (DE),49.47948,8.468935
SNCB-8015190,Herzogenrath,50.8709,6.0944
SNCB-8015199,Aix-la-Chapelle Ouest,50.78078,6.07055
SNCB-8015345,Aachen Hbf (DE),50.76776,6.09139
  etc...
```

---

`routes.csv` of SNCB:

```csv
route_id,route_short_name,route_long_name,route_type
SNCB-1,IC,Courtrai -- Poperinge,TRAIN
SNCB-10,L,Gand-Saint-Pierre -- Renaix,TRAIN
SNCB-100,BUS,Liège-Guillemins -- Welkenraedt,TRAIN
SNCB-101,BUS,Landen -- Liège-Guillemins,TRAIN
  etc...
```

---

`trips.csv` of SNCB:

```csv
trip_id,route_id
SNCB-88____:007::8896008:8896735:8:647:20250328,SNCB-1
SNCB-88____:007::8821006:8821402:11:613:20250829,SNCB-2
SNCB-88____:007::8896008:8896735:8:751:20250328,SNCB-1
SNCB-88____:007::8892007:8896735:13:753:20250330,SNCB-3
SNCB-88____:007::8821006:8821402:11:713:20250829,SNCB-2
  etc...
```

---

`stop_times.csv` of SNCB:

```csv
trip_id,departure_time,stop_id,stop_sequence
SNCB-88____:007::8896008:8896735:8:647:20250328,06:04:00,SNCB-8896008,1
SNCB-88____:007::8896008:8896735:8:647:20250328,06:10:00,SNCB-8896388,2
SNCB-88____:007::8896008:8896735:8:647:20250328,06:14:00,SNCB-8896370,3
SNCB-88____:007::8896008:8896735:8:647:20250328,06:19:00,SNCB-8896305,4
SNCB-88____:007::8896008:8896735:8:647:20250328,06:24:00,SNCB-8896396,5
SNCB-88____:007::8896008:8896735:8:647:20250328,06:29:00,SNCB-8896412,6
SNCB-88____:007::8896008:8896735:8:647:20250328,06:39:00,SNCB-8896503,7
SNCB-88____:007::8896008:8896735:8:647:20250328,06:47:00,SNCB-8896735,8
SNCB-88____:007::8821006:8821402:11:613:20250829,05:36:00,SNCB-8821006,1
  etc...
```

## Use your customized GTFS data

You can use your own GTFS data by specifying the path at the execution of the program:

### CLI

Add the flag `--gtfs-path=YOUR_GTFS_DIR` in the execution command.

### ENV

Execute the command `update gtfs-path = YOUR_GTFS_DIR` in the environment.

### GUI

In the `Graph settings` in the left panel, there is a field "GTFS path" that takes the GTFS directory path.

