import java.util.*;


public class MapImpl implements Map{
    private Set<Place> Places;
    private Set<Road> Roads;
    private Place startPlace;
    private Place endPlace;
    private List<MapListener> listenerList;

    public int tripDistance=0;

    public boolean validRoadName(String name){
        boolean isValid=false;
        if (name==""){
            isValid=true;
            return isValid;}
        char charCheck =name.charAt(0);
        if (charCheck == '-'|| Character.isLetter(charCheck)){
            isValid=true;
        }
        return isValid;
    }
    public MapImpl(){
        this.Places=new HashSet<Place>();
        this.Roads=new HashSet<Road>();
        this.startPlace=null;
        this.endPlace=null;
        this.tripDistance=0;
    }

//Create a new Place and add it to this map
    //Return the new place
    //Throws IllegalArgumentException if:
    //  the name is not valid or is the same as that
    //  of an existing place
    //Note: A valid placeName begins with a letter, and is
    //followed by optional letters, digits, or underscore characters
    @Override
    public Place newPlace(String placeName, int xPos, int yPos) throws IllegalArgumentException {

        char charCheck =placeName.charAt(0);
        if (!Character.isLetter(charCheck)){
            throw new IllegalArgumentException();
        }
        PlaceImpl place = new PlaceImpl(placeName, xPos,yPos);
        this.Places.add(place);

        if(listenerList!=null) {
            for (MapListener m : listenerList) {
                m.placesChanged();
            }
        }

        return place;
    }
    //Remove a place from the map
    //If the place does not exist, returns without error
    @Override
    public void deletePlace(Place s) {
        if (s==null){
        return;
        }
        if( Places.contains(s)){
            this.Places.remove(s);
            if(listenerList!=null) {
                for(MapListener m: listenerList) {
                    m.placesChanged();
                }
            }
        }else{return;}
    }
    //Find and return the Place with the given name
    //If no place exists with given name, return NULL
    @Override
    public Place findPlace(String placeName) {
        for (Place place : Places){
            if (place.getName()==placeName){
                return place;
                }
        }
        return null;
    }
    //no special rules
    @Override
    public Set<Place> getPlaces() {
        return this.Places;
    }
    //Create a new Road and add it to this map
    //Returns the new road.
    //Throws IllegalArgumentException if:
    //  the firstPlace or secondPlace does not exist or
    //  the roadName is invalid or
    //  the length is negative
    //Note: A valid roadName is either the empty string, or starts
    //with a letter and is followed by optional letters and digits
    @Override
    public Road newRoad(Place from, Place to, String roadName, int length) throws IllegalArgumentException {
        if (!Places.contains(from)||!Places.contains(to)){
            throw new IllegalArgumentException();
        }
        if(!validRoadName(roadName)){
            System.out.println(roadName);
            throw new IllegalArgumentException();
        }
        if(length<0){
            throw new IllegalArgumentException();
        }
        RoadImpl road= new RoadImpl(from,to,roadName,length);
        //add to map
        Roads.add(road);
        //add to "from"
        from.toRoads().add(road);
        //add to "to"
        to.toRoads().add(road);
        if(listenerList!=null) {
            for (MapListener m : listenerList) {
                m.roadsChanged();
            }
        }
        return road;
    }
    //Remove a road r from the map
    //If the road does not exist, returns without error
    @Override
    public void deleteRoad(Road r) {
        if(!Roads.contains(r)){
        return;
        }
        this.Roads.remove(r);
        if(listenerList!=null) {
            for (MapListener m : listenerList) {
                m.roadsChanged();
            }
        }
    }
    //no special rules
    @Override
    public Set<Road> getRoads() {
        return this.Roads;
    }
    //Set the place p as the starting place
    //If p==null, unsets the starting place
    //Throws IllegalArgumentException if the place p is not in the map
    @Override
    public void setStartPlace(Place p) throws IllegalArgumentException {
        if (p==null){this.startPlace=null;}
        else if (Places.contains(p)){
            this.startPlace=p;
        }
        else {
            throw new IllegalArgumentException("bad start place");
        }
    }
    @Override
    public Place getStartPlace() {
        return this.startPlace;
    }

    //Set the place p as the ending place
    //If p==null, unsets the ending place
    //Throws IllegalArgumentException if the place p is not in the map
    @Override
    public void setEndPlace(Place p) throws IllegalArgumentException {
        if (p==null){this.startPlace=null;}
        if (Places.contains(p)){
            this.endPlace=p;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Place getEndPlace() {
        return this.endPlace;
    }

    //Causes the map to compute the shortest trip between the
    //"start" and "end" places
    //For each road on the shortest route, sets the "isChosen" property
    //to "true".
    //Returns the total distance of the trip.
    //Returns -1, if there is no route from start to end
    @Override
    public int getTripDistance() {
        int startX=0, startY=0;
        int endX=0, endY=0;
        startX=this.getStartPlace().getX();
        startY=this.getStartPlace().getY();
        endX=this.getEndPlace().getX();
        endY=this.getEndPlace().getY();
        return this.tripDistance;
    }
    //Return a string describing this map
    //Returns a string that contains (in this order):
    //for each place in the map, a line (terminated by \n)
    //  PLACE followed the toString result for that place
    //for each road in the map, a line (terminated by \n)
    //  ROAD followed the toString result for that road
    //if a starting place has been defined, a line containing
    //  START followed the name of the starting-place (terminated by \n)
    //if an ending place has been defined, a line containing
    //  END followed the name of the ending-place (terminated by \n)
    public String toString(){
    String mapString="";
    String placesString = "";
    String roadString="";
    String startString="";
    String endString="";
    for (Place place1: Places){
        placesString=placesString+"PLACE "+place1.toString()+"\n";
    }
    for (Road road1: Roads){
        roadString=roadString+"ROAD "+road1.toString()+"\n";
    }
    if(this.startPlace!=null){
        startString="START "+this.getEndPlace().getName()+"\n";
    }
    if(this.endPlace!=null){
        endString="END "+this.getEndPlace().getName()+"\n";
    }
        mapString=mapString+placesString+roadString+startString+endString;
    return mapString;
    }
    @Override
    public void addListener(MapListener ml) {
        listenerList.add(ml);
    }
    @Override
    public void deleteListener(MapListener ml)
    {
        if(ml!=null&&listenerList.contains(ml)){
            listenerList.remove(ml);
        }

    }

    public class PlaceImpl implements Place {
        public Set<Road> toRoads;
        public String name;
        public int x;
        public int y;
        private boolean startPlace;
        private boolean endPlace;
        private List<PlaceListener> placeListenerList;

        public PlaceImpl(String name, int x, int y){
            this.toRoads= new HashSet<Road>();
            this.name=name;
            this.x=x;
            this.y=y;
            this.startPlace=false;
            this.endPlace=false;
        }
        @Override
        public Set<Road> toRoads() {
            return this.toRoads;
        }
        //Return the road from this place to dest, if it exists
        //Returns null, if it does not
        @Override
        public Road roadTo(Place dest) {
           for (Road road : this.toRoads){
               if(road.firstPlace()==dest){
                   return road;
               }else if (road.secondPlace()==dest){
                   return road;
               }
           }
            return null;
        }
        @Override
        public void moveBy(int dx, int dy) {
            this.x=this.x+dx;
            this.y=this.y+dy;
        }
        @Override
        public String getName() {
            return this.name;
        }
        @Override
        public int getX() {
            return this.x;
        }
        @Override
        public int getY() {
            return this.y;
        }
        @Override
        public boolean isStartPlace() {
            return this.startPlace;
        }
        @Override
        public boolean isEndPlace() {
            return this.endPlace;
        }
        @Override
        public String toString(){
            String string= this.name+"("+this.getX()+","+this.getY()+")";
            return string;
        }
        @Override
        public void addListener(PlaceListener pl) {
            placeListenerList.add(pl);
        }
        @Override
        public void deleteListener(PlaceListener pl) {
            if(pl!=null&&placeListenerList.contains(pl))
                placeListenerList.remove(pl);
        }

    }
    public class RoadImpl implements Road {
        public Set<Place> places;
        public Place firstPlace;
        public Place secondPlace;
        public String roadNameString;
        public int roadLength;
        private List<RoadListener> roadListenerList;
        public RoadImpl(Place from, Place to, String roadName, int roadLength){
            this.places=new HashSet<Place>();
            this.places.add(from);
            this.firstPlace=from;
            this.places.add(to);
            this.secondPlace=to;
            this.roadNameString=roadName;
            this.roadLength=roadLength;
            this.sortPlaces();
        }
        public void sortPlaces(){
            for(int i = 0; i<this.firstPlace.getName().length(); i++){
                if (this.firstPlace.getName().charAt(i)<this.secondPlace.getName().charAt(i)){
                    //already alphabetical ahead
                    break;
                }else if (this.firstPlace.getName().charAt(i)>this.secondPlace.getName().charAt(i)){
                    //firstplace is alphabetically second, swap position with second place
                    Place tempPlace = this.firstPlace;
                    this.firstPlace=this.secondPlace;
                    this.secondPlace=tempPlace;
                    break;
                } else if (this.firstPlace.getName().charAt(i)==this.secondPlace.getName().charAt(i)){
                    //i++ called by loop
                }
            }
        }
        @Override
        public Place firstPlace() {
            this.sortPlaces();
            return this.firstPlace;
        }
        @Override
        public Place secondPlace() {
            this.sortPlaces();
            return this.secondPlace;
        }
        @Override
        public boolean isChosen() {
            return false;
        }
        @Override
        public String roadName() {
            return roadNameString;
        }
        @Override
        public int length() {
            return this.roadLength;
        }
        //Return a string containing information about this road
        //in the form (without quotes, of course!):
        //"firstPlace(roadName:length)secondPlace"
        @Override
        public String toString(){
            return this.firstPlace.getName()+"("+this.roadName()+":"+this.roadLength+")"+this.secondPlace().getName();}
        @Override
        public void addListener(RoadListener rl) {
            roadListenerList.add(rl);
        }
        @Override
        public void deleteListener(RoadListener rl) {
            if(rl!=null&roadListenerList.contains(rl)){
                roadListenerList.remove(rl);
            }
        }

    }
}