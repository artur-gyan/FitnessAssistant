package org.example;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;
import java.time.ZonedDateTime;

public class TcxLoader {
    public Activity load(String filePath) {

        try {
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            ActivityFactory activityFactory = new ActivityFactory();

            Activity activity = null;

            NodeList activityNodes = doc.getElementsByTagName("Activity");
            /// ACTIVITIES
            if (activityNodes.getLength() > 0) {
                Element activitiesElement = (Element) activityNodes.item(0);
                String sport = activitiesElement.getAttribute("Sport");
                String id = "";
                if (activitiesElement.getElementsByTagName("Id").getLength() > 0) {
                    id = activitiesElement.getElementsByTagName("Id").item(0).getTextContent();
                }
                /// Δημιρουγια αντικειμενου
                activity = activityFactory.getOrCreateActivity(sport, id);

                /// LAPS
                NodeList lapNodes = activitiesElement.getElementsByTagName("Lap");
                for (int i = 0; i < lapNodes.getLength(); i++) {
                    Element lapElement = (Element) lapNodes.item(i);
                    Lap lap = new Lap();
                    /// StartTime
                    String startTimeString = lapElement.getAttribute("StartTime");
                    ZonedDateTime startTime = ZonedDateTime.parse(startTimeString);
                    lap.setStartTime(startTime);
                    /// StartTime

                    /// TotalTimeSeconds
                    if ( lapElement.getElementsByTagName("TotalTimeSeconds").getLength() > 0) {
                        String totalTimeSecondsString = lapElement.getElementsByTagName("TotalTimeSeconds").item(0).getTextContent();
                        double totalTimeSeconds = Double.parseDouble(totalTimeSecondsString);
                        lap.setTotalTimeSeconds(totalTimeSeconds);
                    }
                    /// TotalTimeSeconds
                    if ( lapElement.getElementsByTagName("DistanceMeters").getLength() > 0) {
                        String totalDistanceMetersString = lapElement.getElementsByTagName("DistanceMeters").item(0).getTextContent();
                        double totalDistanceMeters = Double.parseDouble(totalDistanceMetersString);
                        lap.setDistanceMeters(totalDistanceMeters);
                    }
                    /// TotalTimeSeconds
                    ///  MaximumHeartRateBpm
                    if ( lapElement.getElementsByTagName("MaximumHeartRateBpm").getLength() > 0) {
                        Element maximumHeartRateBpmElement = (Element) lapElement.getElementsByTagName("MaximumHeartRateBpm").item(0);
                        NodeList maximumHeartRateBpmValueNodes = maximumHeartRateBpmElement.getElementsByTagName("Value");
                        if (maximumHeartRateBpmValueNodes.getLength() > 0) {
                            Element maximumHeartRateBpmValueElement = (Element) maximumHeartRateBpmValueNodes.item(0);
                            String maximumHeartRateBpmString = maximumHeartRateBpmValueElement.getTextContent();
                            int maximumHeartRateBpm = Integer.parseInt(maximumHeartRateBpmString);
                            lap.setMaximumHeartRate(maximumHeartRateBpm);
                        }
                    }
                    ///  MaximumHeartRateBpm
                    if ( lapElement.getElementsByTagName("AverageHeartRateBpm").getLength() > 0) {
                        Element averageHeartRateBpmElement = (Element) lapElement.getElementsByTagName("AverageHeartRateBpm").item(0);
                        NodeList averageHeartRateBpmValueNodes = averageHeartRateBpmElement.getElementsByTagName("Value");
                        if (averageHeartRateBpmValueNodes.getLength() > 0) {
                            Element averageHeartRateBpmValueElement = (Element) averageHeartRateBpmValueNodes.item(0);
                            String averageHeartRateBpmString = averageHeartRateBpmValueElement.getTextContent();
                            int averageHeartRateBpm = Integer.parseInt(averageHeartRateBpmString);
                            lap.setAverageHeartRate(averageHeartRateBpm);
                        }
                    }
                    /// MaxSpeed
                    if ( lapElement.getElementsByTagName("MaximumSpeed").getLength() > 0) {
                        String maximumSpeedString = lapElement.getElementsByTagName("MaximumSpeed").item(0).getTextContent();
                        double maximumSpeed = Double.parseDouble(maximumSpeedString);
                        lap.setMaximumSpeed(maximumSpeed);
                    }
                    /// MaxSpeed

                    /// Calories
                    if ( lapElement.getElementsByTagName("Calories").getLength() > 0) {
                        String caloriesString = lapElement.getElementsByTagName("Calories").item(0).getTextContent();
                        int calories = Integer.parseInt(caloriesString);
                        lap.setCalories(calories);
                    }
                    /// Calories

                    /// Intensity
                    if ( lapElement.getElementsByTagName("Intensity").getLength() > 0) {
                        String intensityString = lapElement.getElementsByTagName("Intensity").item(0).getTextContent();
                        lap.setIntensity(intensityString);
                    }
                    /// Intensity

                    if ( lapElement.getElementsByTagName("TriggerMethod").getLength() > 0) {
                        String triggerMethodString = lapElement.getElementsByTagName("TriggerMethod").item(0).getTextContent();
                        lap.setTriggerMethod(triggerMethodString);
                    }

                    activity.addLap(lap);

                    /// TRACKS
                    NodeList trackNodes = lapElement.getElementsByTagName("Track");
                    for (int j = 0; j < trackNodes.getLength(); j++){
                        Element trackElement = (Element) trackNodes.item(j);

                        Track track = new Track();
                        lap.addTrack(track);

                        /// TRACKPOINTS
                        NodeList trackPointNodes = trackElement.getElementsByTagName("Trackpoint");
                        for (int k = 0; k < trackPointNodes.getLength(); k++) {

                            Element trackPointElement = (Element) trackPointNodes.item(k);
                            TrackPoint trackPoint = new TrackPoint();
                            ///  TimeStamp
                            String timeString = trackPointElement.getElementsByTagName("Time").item(0).getTextContent();
                            ZonedDateTime timeStamp = ZonedDateTime.parse(timeString);
                            trackPoint.setTimeStamp(timeStamp);
                            /// TimeStamp

                            /// Altitude
                            if (trackPointElement.getElementsByTagName("AltitudeMeters").getLength() > 0) {
                                String altitudeString = trackPointElement.getElementsByTagName("AltitudeMeters").item(0).getTextContent();
                                Double altitudeMeters = Double.parseDouble(altitudeString);
                                trackPoint.setAltitude(altitudeMeters);
                            }
                            /// Altitude

                            /// Distance
                            if (trackPointElement.getElementsByTagName("DistanceMeters").getLength() > 0){
                                String distanceString = trackPointElement.getElementsByTagName("DistanceMeters").item(0).getTextContent();
                                Double distanceMeters = Double.parseDouble(distanceString);
                                trackPoint.setDistanceMeters(distanceMeters);
                            }
                            /// Distance

                            /// HeartRate
                            NodeList heartRBNodes = trackPointElement.getElementsByTagName("HeartRateBpm");
                            if (heartRBNodes.getLength() > 0) {
                                Element heartRBElement = (Element) heartRBNodes.item(0);
                                NodeList heartRBValueNodes = heartRBElement.getElementsByTagName("Value");
                                if (heartRBValueNodes.getLength() > 0){
                                    Element heartRBValueElement = (Element) heartRBValueNodes.item(0);
                                    String heartRateString = heartRBValueElement.getTextContent();
                                    Integer heartRate = Integer.parseInt(heartRateString);
                                    trackPoint.setHeartRate(heartRate);
                                }
                            }
                            /// HeartRate

                            /// Speed και RunCadence
                            NodeList extensionsNodes = trackPointElement.getElementsByTagName("Extensions");
                            if (extensionsNodes.getLength() > 0) {
                                Element extensionsElement = (Element) extensionsNodes.item(0);
                                NodeList TPXNodes = extensionsElement.getElementsByTagName("ns3:TPX");
                                if (TPXNodes.getLength() > 0) {
                                    /// Speed
                                    Element TPXElement = (Element) TPXNodes.item(0);
                                    NodeList SpeedNodes = TPXElement.getElementsByTagName("ns3:Speed");
                                    if (SpeedNodes.getLength() > 0) {
                                        Element SpeedElement = (Element) SpeedNodes.item(0);
                                        String speedString = SpeedElement.getTextContent();
                                        Double speed = Double.parseDouble(speedString);
                                        trackPoint.setSpeed(speed);
                                    }
                                    /// Speed

                                    /// RunCadence
                                    NodeList CadenceNodes = TPXElement.getElementsByTagName("ns3:RunCadence");
                                    if (CadenceNodes.getLength() > 0) {
                                        Element CadenceElement = (Element) CadenceNodes.item(0);
                                        String cadenceString = CadenceElement.getTextContent();
                                        Integer cadence = Integer.parseInt(cadenceString);
                                        trackPoint.setRunCadence(cadence);
                                    }
                                    /// RunCadence

                                }
                            }
                            /// Speed και RunCadence

                            ///  Position
                            NodeList positionNodes = trackPointElement.getElementsByTagName("Position");
                            if (positionNodes.getLength() > 0) {
                                Element positionElement = (Element) positionNodes.item(0);
                                ///  Latitude
                                NodeList latitudeNodes = positionElement.getElementsByTagName("LatitudeDegrees");
                                if (latitudeNodes.getLength() > 0) {
                                    Element latitudeElement = (Element) latitudeNodes.item(0);
                                    String latitudeString = latitudeElement.getTextContent();
                                    double latitude = Double.parseDouble(latitudeString);
                                    trackPoint.setLatitude(latitude);
                                }
                                ///  Latitude

                                /// Longitude
                                NodeList longitudeNodes = positionElement.getElementsByTagName("LongitudeDegrees");
                                if (longitudeNodes.getLength() > 0) {
                                    Element longitudeElement = (Element) longitudeNodes.item(0);
                                    String longitudeString = longitudeElement.getTextContent();
                                    double longitude = Double.parseDouble(longitudeString);
                                    trackPoint.setLongitude(longitude);
                                }
                                /// Longitude
                            }
                            ///  Position
                            track.addTrackPoint(trackPoint);
                        }
                    }

                }

            }

            return activity;
        } catch (Exception e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
}
