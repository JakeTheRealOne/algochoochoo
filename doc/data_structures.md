PRIMARY:

Stop (Id, Name, Lat, Long)
Route (Id, Name, Type)
Path (List<>)

INPUT:

- source: String
- target: String

- stops: List<Stop>
- routes: List<Route>
- times: List<Time>
- stop_dict: Map<String, int>
- route_dict: Map<String, int>
- time_dict: Map<String, int>

OUTPUT:

- solutions: List<Path>