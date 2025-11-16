import backend.POI;

public class Main {
    

    public POI createPoi(long lat, long lon, int type){
        try{
            POI newP = new POI(lat, lon, type);
            newP.addToDb();
        }catch(Error e){
            throw e;
        }
    }

    

    public boolean deletePoi(POI p){
        for(DocumentReference doc : db.collection("User")){
            if(doc.get().getId() == p.getId()){
                doc.delete();
                return true;
            }
        }
    }
    return false;
}
