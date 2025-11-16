import backend.POI;

public class User {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc;

    private int id;
    private String username;
    private String password;
    private String email;
    private List<POI> poisPlaced;
    static int idIter = 0;

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.id = idIter;
        idIter++;
        this.poisPlaced = new List<POI>();
        this.doc = db.collection("User").document(username);
    }

    //getters/setters
    public getId(){return id;}

    public String getUsername(){return username;}
    public void setUsername(String usn){
        this.username = usn;
        doc.update({
            username : usn
        });
    }

    public String getPassword(){return password;}
    public void setPassword(String pass){
        this.password = pass;
        doc.update({
            password : pass
        });
    }

    public String getEmail(){return email;}
    public void setEmail(String email){
        this.email = email;
        doc.update({
            email : email
        });
    }

    public List<POI> getPoisPlaced(){return poisPlaced;}
    public void addPoi(POI p){poisPlaced.add(p);}
    public void removePoi(POI p){poisPlaced.delete(p);}

     public boolean addToDb(){
        try{
            doc.set(this);
            return true;
        }catch(Error e){
            Log.w(TAG, "Error writing document", e);
            return false;
        }
    }
}
