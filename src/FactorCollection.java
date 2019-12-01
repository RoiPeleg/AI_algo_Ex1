public class FactorCollection {

    //attributes
    private Factor[] factor_collection;
    int size;

    //constructor

    //methods
    public void addFactor(Factor f) { //no importance to order of factors in the collection, think if its necessary...
        int newSize = this.size + 1;
        Factor[] newCollection = new Factor[newSize];
        for(int i=0; i<this.size; i++) {
            newCollection[i] = this.factor_collection[i];
        }
        newCollection[newSize - 1] = f;
        this.size = newSize;
    }

    private void removeOneValued() {
        int counter = 0; //number of factors to remove
        for(int i=0; i<this.size; i++) {
            if(this.factor_collection[i].isOneValued()) counter++;
        }
        if(counter == 0) //there is nothing to remove
            return;
        int newSize = this.size - counter;
        Factor[] newCollection = new Factor[newSize];
        for(int j=0; j<this.size; j++) {
            int n = 0; //the index on the new collection to add the factor
            while(n < newSize)
                if (!this.factor_collection[j].isOneValued()) {
                    newCollection[n] = this.factor_collection[j];
                    n++;
                }
        }
    }


}
