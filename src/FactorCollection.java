import java.util.ArrayList;

public class FactorCollection {

    //attributes
    private ArrayList<Factor> factor_collection;
    int size;

    //constructor
    //probably using the automatically default constructor ...

    //methods
    public void addFactor(Factor f) {
        factor_collection.add(f);
        size++;
    }

    private void removeOneValued() {
        int index = 0;
        while(index < this.size) {
            if(this.factor_collection.get(index).isOneValued()) {
                factor_collection.remove(index);
                index--; //because all the other elements following it move down one index.
                size --;
            }
            index++;
        }
    }


}
