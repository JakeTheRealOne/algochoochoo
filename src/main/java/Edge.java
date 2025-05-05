// /**
//  * (Literally) A connection between two stops
//  *
//  * @author Bilal Vandenberge
//  */
// public class Edge {
//   // #### Public methods ####

//   /**
//    * Construct a new Edge object
//    *
//    * @param first The first element
//    * @param second The second element
//    * @param trip_ The id of the trip owning the connection
//    */
//   public Edge(TripElement first, TripElement second, Trip trip_) {
//     from = first.stop();
//     to = second.stop();
//     trip = trip_;
//     departure_time = first.departure_time();
//     arrival_time = second.departure_time();
//     if (arrival_time < departure_time) {
//       throw new IllegalArgumentException(
//           "Arrival time: " + beautiful_time(false)
//           + " < Departure time: " + beautiful_time(true));
//     }
//   }

//   // /**
//   //  * Construct a new Edge object for a footpath
//   //  *
//   //  * @param first The source stop of the footpath
//   //  * @param second The target stop of the footpath
//   //  * @param dep The departure time of the pedestrian
//   //  * @param dur The average duration to walk to destination
//   //  */
//   // public Edge(Stop first, Stop second, int dep, int dur) {
//   //   from = first;
//   //   to = second;
//   //   trip = null;
//   //   walking = true;
//   //   departure_time = dep;
//   //   arrival_time = dep + dur;
//   //   if (arrival_time < departure_time) {
//   //     throw new IllegalArgumentException(
//   //         "Arrival time: " + beautiful_time(false) + " < Departure time:
//   " +
//   // beautiful_time(true));
//   //   }
//   // }

//   /** Convert a Edge object to string */
//   @Override
//   public String toString() {
//     return "Edge(from " + from + ", to " + to + ")";
//   }

//   /**
//    * Get the english directive corresponding to the route type
//    *
//    * @return The directive
//    */
//   public String directive() {
//     Route route = (trip == null) ? null : trip.route();
//     return route == null ? "Walking"
//                          : "Taking " + route.agency() + " " + route.type()
//             + " " + route.short_name();
//   }

//   /**
//    * Get the name of the trip
//    *
//    * @return The trip name
//    */
//   public String trip_name() {
//     if (trip == null) {
//       return "";
//     } else {
//       Route route = trip.route();
//       return route.agency() + " " + route.type() + " " + route.short_name();
//     }
//   }

//   // #### Getters ####

//   /**
//    * Get the source stop of the connection
//    *
//    * @return The source stop
//    */
//   public Stop from() {
//     return from;
//   }

//   /**
//    * Get the target stop of the connection
//    *
//    * @return The target stop
//    */
//   public Stop to() {
//     return to;
//   }

//   /**
//    * Get the trip of the connection
//    *
//    * @return The trip
//    */
//   public Trip trip() {
//     return trip;
//   }

//   /**
//    * Get the departure time from the source stop
//    *
//    * @return The departure time
//    */
//   public int departure_time() {
//     return departure_time;
//   }

//   /**
//    * Get the arrival time to the target stop
//    *
//    * @return The arrival time
//    */
//   public int arrival_time() {
//     return arrival_time;
//   }

//   // /**
//   //  * Get if the connection is a transfer
//   //  *
//   //  * @return If the connection is a footpath
//   //  */
//   // public boolean is_footpath() {
//   //   return walking;
//   // }

//   // #### Attributes ####

//   Stop from;
//   Stop to;
//   Trip trip;
//   int departure_time;
//   int arrival_time;

//   // boolean walking;

//   // #### Private helpers ####

//   private String beautiful_time(boolean departure) {
//     int time = (departure ? departure_time : arrival_time);
//     return String.format(
//         "%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60);
//   }
// }
