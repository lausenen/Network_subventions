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

    static class Connection {
        Knot head;
        Knot rear;
        int value;
        boolean visited = false;

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

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }
    }

    static public int Prim(Knot[] houselist, Knot s) {

        PriorityQueue<Knot> pq = new PriorityQueue();

        for (Knot knot : houselist) {
            pq.add(knot);
        }

        s.setKey(0);
        s.setPi(s);
        while (!pq.isEmpty()) {

            Knot extractedKnot = pq.remove();
            Knot currentKnot;
            Knot parentKnot;
            for (Connection connection : extractedKnot.getConnections()) {
                if(!connection.getHead().isMarked() && !connection.getRear().isMarked()) {
                    if (connection.getHead().equals(extractedKnot)) {
                        currentKnot = connection.getRear();
                        parentKnot = connection.getHead();
                    } else {
                        currentKnot = connection.getHead();
                        parentKnot = connection.getRear();
                    }

                    if (currentKnot.getKey() > connection.getValue()) {
                        currentKnot.setKey(connection.getValue());
                        currentKnot.setPi(parentKnot);
                        pq.remove(currentKnot);
                        pq.add(currentKnot);
                    }
                }
            }
            extractedKnot.setMarked(true);
        }




        //sets visible
            for (Knot knot : houses){
                for (Connection conn : knot.getConnections()){
                    if(knot!=knot.pi &&(knot.pi.equals(conn.getHead()) || knot.pi.equals(conn.getRear()))){
                        conn.setVisited(true);
                    }
                }
            }
        return 1;
    }

    public static int countMoney(){
        int result = 0;
        for(Knot knot : houses){
            for(Connection conn : knot.getConnections()){
                if(conn.isVisited() && conn.getRear().equals(knot)){
                    result = result + gov_contribution - conn.getValue();
                }
                else if(!conn.isVisited()&& conn.getRear().equals(knot) && conn.getValue()<gov_contribution){
                    conn.setVisited(true);
                    result = result + gov_contribution -conn.getValue();
                }
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
        Prim(houses, houses[0]);
        int result = countMoney();
        //System.out.println(result);
        if(result>0){
            System.out.println("YES");
        }
        else if(result<0){
            System.out.println("NO");
        }
    }


}
