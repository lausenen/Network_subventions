import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {


    static int connections;
    static int gov_contribution;
    static Knot[] houses;

    static class Knot implements Comparable<Knot> {

        int id;
        boolean visited = false;
        boolean marked = false;
        double key = Double.POSITIVE_INFINITY;
        Knot pi;
        ArrayList<Connection> connections = new ArrayList<>();

        public Knot(int number) {
            id = number;
        }

        public double getKey() {
            return key;
        }

        public void setKey(double key) {
            this.key = key;
        }

        public Knot getPi() {
            return pi;
        }

        public void setPi(Knot pi) {
            this.pi = pi;
        }

        public ArrayList<Connection> getConnections() {
            return connections;
        }

        public void addConnection(Connection connection) {
            this.connections.add(connection);
        }

        public Connection getConnection(int index) {
            return connections.get(index);
        }

        public int getSize() {
            return connections.size();
        }

        public int getId() {
            return id;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMarked(boolean marked) {
            this.marked = marked;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        @Override
        public int compareTo(Knot k) {
            if (Double.compare(this.key, k.key) == 0) {
                return 0;
            }
            if (Double.compare(this.key, k.key) < 0) {
                return -1;
            }
            return 1;
        }
    }

    /*    static class Pqueue{
            int minKey = 0;
            int maxKey = 0;
            ArrayList<Knot> sortArray = new ArrayList<>();
            public Knot exstractKnot(){
                sortQueue();
                Knot minElement = sortArray.get(0);
                return sortArray.remove(0);
            }

            public void sortQueue(){
                boolean notSorted = true;
                while(notSorted){
                notSorted = false;
                for (int i=0 ; i<sortArray.size()-2;i++){
                    Knot temp1 = sortArray.get(i);
                    Knot temp2 = sortArray.get(i+1);
                    if(Double.compare(sortArray.get(i).key , sortArray.get(i+1).key)> 0){
                        notSorted = true;
                        sortArray.remove(i);
                        sortArray.add(i,temp2);
                        sortArray.remove(i+1);
                        sortArray.add(i+1,temp1);

                    }
                }
                }
            }
            public void addKnot(Knot knot){
                if(knot.getKey()<minKey|| minKey==0){
                    minKey = (int)knot.getKey();
                }
                else if(knot.getKey()>maxKey|| maxKey==0){
                    maxKey = (int)knot.getKey();
                }
                sortArray.add(knot);
            }

            public int getMinKey() {
                return minKey;
            }

            public void setMinKey(int minKey) {
                this.minKey = minKey;
            }

            public int getMaxKey() {
                return maxKey;
            }

            public void setMaxKey(int maxKey) {
                this.maxKey = maxKey;
            }

            public boolean isEmpty(){
                if(sortArray.isEmpty()){
                    return true;
                }
                else return false;
            }
        }*/
    static class Connection {
        Knot head;
        Knot rear;
        int value;

        public Knot getHead() {
            return head;
        }

        public void setHead(Knot head) {
            this.head = head;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public Knot getRear() {
            return rear;
        }

        public void setRear(Knot rear) {
            this.rear = rear;
        }
    }

    static public int Prim(Knot[] houselist, Knot s) {
        int result = 0;

        PriorityQueue<Knot> pq = new PriorityQueue();

        for (Knot knot : houselist) {
            pq.add(knot);
        }

        s.setKey(0);
        Connection minCon = null;
        ArrayList<Connection> profitCon = new ArrayList<>();
        while (!pq.isEmpty()) {
            if(profitCon.size()!=0){
                for(Connection connection : profitCon){
                    result = result + gov_contribution - connection.getValue();
                }
                minCon = null;
                profitCon = new ArrayList<>();
            }
            Knot extractedKnot = pq.remove();
            Knot newElement = null;
            boolean allVisited = true;
            for (Connection connection : extractedKnot.getConnections()) {
                if(!connection.getRear().isMarked() && !connection.getHead().isMarked()){
                    if(connection.getRear().equals(extractedKnot)){
                        newElement = connection.getHead();
                    }
                    else{
                        newElement = connection.getRear();
                    }
                    if(!newElement.isVisited()){
                        allVisited = false;
                    }
                if((null == minCon )){
                    minCon = connection;
                    newElement.setVisited(true);
                }
                else if(connection.getValue()< minCon.getValue()){
                    profitCon.add(minCon);
                    minCon = connection;
                    newElement.setVisited(true);
                    }
                if(connection.getValue() < gov_contribution && connection!=minCon){
                    profitCon.add(connection);
                    newElement.setVisited(true);
                }
                if (newElement.getKey() > connection.getValue()) {
                    newElement.setKey(connection.getValue());
                    newElement.setPi(connection.getRear());
                    pq.remove(newElement);
                    pq.add(newElement);
                }
                }
            }
            extractedKnot.setMarked(true);
            extractedKnot.setVisited(true);
            if(null!=minCon && (!allVisited && minCon.getValue()>gov_contribution)){
                profitCon.add(minCon);
            }
            else if(null!=minCon && minCon.getValue()<gov_contribution){
                profitCon.add(minCon);
            }


        }
        return result;
    }


    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(in.readLine());
        houses = new Knot[n];

        int m = Integer.parseInt(in.readLine());
        connections = m;

        int v = Integer.parseInt(in.readLine());

        gov_contribution = v;
        int houseOne = 0;
        int houseTwo = 0;
        //special cases for 1 and 2 locations
        for (int i = 0; i < connections; i++) {
            StringTokenizer st = new StringTokenizer(in.readLine());

            int inputNumber = 0;
            Connection myConnection1 = new Connection();
            while (st.hasMoreTokens()) {
                int inputValue = Integer.parseInt(st.nextToken());

                inputNumber++;

                if (inputNumber == 1) {
                    houseOne = inputValue;
                    if (null == houses[houseOne]) {
                        houses[houseOne] = new Knot(houseOne);
                    }
                    myConnection1.setRear(houses[houseOne]);


                } else if (inputNumber == 2) {
                    houseTwo = inputValue;
                    if (null == houses[houseTwo]) {
                        houses[houseTwo] = new Knot(houseTwo);
                    }
                    myConnection1.setHead(houses[houseTwo]);
                } else if (inputNumber == 3) {
                    myConnection1.setValue(inputValue);
                    houses[houseOne].addConnection(myConnection1);
                    houses[houseTwo].addConnection(myConnection1);
                }
            }
        }
        int result = Prim(houses, houses[0]);
        //System.out.println(result);
        if(result>0){
            System.out.println("YES");
        }
        else if(result<0){
            System.out.println("NO");
        }
    }


}
