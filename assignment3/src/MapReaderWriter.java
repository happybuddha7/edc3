import javax.swing.*;
import java.io.*;
import java.nio.CharBuffer;
import java.util.Scanner;

public class MapReaderWriter implements MapIo {
    private Map map;
    public MapReaderWriter() {
    }
    //This class handles reading and writing map representations as
    //described in the practical specification
    public void scanLine (String line, Map m)throws MapFormatException{

    }
    //Read the description of a map from the
    //Reader r, and transfers it to Map, m.
    @Override
    public void read(Reader r, Map m) throws IOException, MapFormatException {
        BufferedReader br = new BufferedReader(r);
        String line="";
        try {
            while(line!=null) {
                line = br.readLine();
                if(line!=null){
                    Scanner scanner = new Scanner(line);
                    if (scanner.hasNext()){
                        String type = scanner.next();
                        //assumes the line has a certain structure
                        while(type.charAt(0)=='#'){
                            type=scanner.next();
                        }
                            if (type.equals("place")){
                                String place1 = scanner.next();
                                //   System.out.println("place1: "+place1);
                                String xPos = scanner.next();
                                int xInt = Integer.parseInt(xPos);
                                String yPos = scanner.next();
                                int yInt = Integer.parseInt(yPos);
                                m.newPlace(place1, xInt, yInt);
                            }
                            else if(type.equals("road")){
                                String place1=scanner.next();
                                // System.out.println("place1: "+place1);
                                String roadName=scanner.next();
                                String roadLength=scanner.next();
                                int lengthInt = Integer.parseInt(roadLength);
                                String place2=scanner.next();
                                Place firstPlace = null;
                                Place secondPlace = null;
                                for (Place place:m.getPlaces()){
                                    if (place.getName().equals(place1)){
                                        firstPlace=place;
                                        break;
                                    }
                                }
                                for (Place place:m.getPlaces()){
                                    if (place.getName().equals(place2)){
                                        secondPlace=place;
                                        break;
                                    }
                                }
                                m.newRoad(firstPlace, secondPlace, roadName, lengthInt);
                            }
                            else if (type.equals("start")){
                                String startPlaceString = scanner.next();
                                Place startPlace=null;
                                for (Place place:m.getPlaces()){
                                    if (place.getName().equals(startPlaceString)){
                                        startPlace=place;
                                        break;
                                    }
                                }
                                m.setStartPlace(startPlace);
                            }
                            else if (type.equals("end")){
                                String endPlaceString = scanner.next();
                                Place endPlace=null;
                                for (Place place:m.getPlaces()){
                                    if (place.getName().equals(endPlaceString)){
                                        endPlace=place;
                                        break;
                                    }
                                }
                                m.setEndPlace(endPlace);
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "Map format Error","Alert",JOptionPane.ERROR_MESSAGE);
                                throw new MapFormatException(0, "map format exception");
                            }
                    }
                    else {
                        System.out.println("end of file");
                    }
                    scanner.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MapFormatException e) {
            e.printStackTrace();
        }
        System.out.println("end of file");
    }
    //Write a representation of the Map, m, to the Writer w.
    @Override
    public void write(Writer w, Map m) throws IOException {
        try {
            for (Place place: m.getPlaces()){
                w.write("place "+place.getName()+" "+ place.getX()+ " "+place.getY());
                w.write(System.getProperty( "line.separator" ));
               // System.out.println("writing place:"+place.getName()+" "+ place.getX()+ " "+place.getY());
             }
        } catch (IOException e) {
            System.out.println("Places NULL");
        }
        w.write(System.getProperty( "line.separator" ));
        try {
            for (Road road: m.getRoads()){
                w.write("road "+road.firstPlace().getName()+ " "+road.roadName()+" "+ road.length()+" "+ road.secondPlace().getName());
                w.write(System.getProperty( "line.separator" ));
                //road firstPlace roadName length secondPlace
            }
        } catch (IOException e) {
            System.out.println("roads NULL");
        }
        if (m.getStartPlace()!=null){
            w.write("start "+m.getStartPlace().getName());
        }
        w.write(System.getProperty( "line.separator" ));
        if (m.getEndPlace()!=null){
            w.write("end "+m.getEndPlace().getName());
        }
        w.write(System.getProperty( "line.separator" ));
    }
}
