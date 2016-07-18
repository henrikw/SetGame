// SET is a registered trademark of Cannei, LLC. Used with permission from Set Enterprises, Inc.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SetGame {
    private static Random random = new Random(); // needs to be initialized for the tests
    private static long totalRounds = 0;
    private static long gamesWithOnly12Cards = 0;
    private static long[][] setCounter = new long[70][70]; // deck-size, table-size
    private static long[][] noSetCounter = new long[70][70]; // deck-size, table-size
    private static long[][] availableSets = new long[70][70]; // deck-size, table-size

    class Card implements Comparable {
        private int number;
        private int symbol;
        private int shading;
        private int colour;
        private float sortOrder;

        public Card(int nu, int sy, int sh, int co) {
            number = nu;
            symbol = sy;
            shading = sh;
            colour = co;
            sortOrder = random.nextFloat(); // used when shuffling the deck
        }
        public int compareTo(Object other) {
            if (this.sortOrder == ((Card) other).sortOrder)
                 return 0;
             else if ((this.sortOrder) > ((Card) other).sortOrder)
                 return 1;
             else
                 return -1;
        }

        public String toString() {
            return number + ":" + symbol + ":" + shading + ":" + colour; // + " - " + sortOrder;
        }
    }

    private ArrayList<Card> createShuffledDeck() {
        ArrayList<Card> cards = new ArrayList<Card>(81);
        for (int number=0; number<3; number++) {
            for (int symbol=0; symbol<3; symbol++) {
                for (int shading=0; shading<3; shading++) {
                    for (int colour=0; colour<3; colour++) {
                        cards.add(new Card(number, symbol, shading, colour));
                    }
                }
            }
        }
        Collections.sort(cards);
        return cards;
    }

    boolean isSet(Card a, Card b, Card c) {
        if (!((a.number == b.number) && (b.number == c.number) ||
                (a.number != b.number) && (a.number != c.number) && (b.number != c.number))) {
            return false;
        }
        if (!((a.symbol == b.symbol) && (b.symbol == c.symbol) ||
                (a.symbol != b.symbol) && (a.symbol != c.symbol) && (b.symbol != c.symbol))) {
            return false;
        }
        if (!((a.shading == b.shading) && (b.shading == c.shading) ||
                (a.shading != b.shading) && (a.shading != c.shading) && (b.shading != c.shading))) {
            return false;
        }
        if (!((a.colour == b.colour) && (b.colour == c.colour) ||
                (a.colour != b.colour) && (a.colour != c.colour) && (b.colour != c.colour))) {
            return false;
        }
        return true;
    }

   ArrayList<ArrayList<Card>> getAllSets(List<Card> cards, boolean findOnlyFirstSet) {
       ArrayList<ArrayList<Card>> result = new ArrayList<ArrayList<Card>>();
       if (cards == null) return result;
       int size = cards.size();
       for (int ai = 0; ai < size; ai++) {
           Card a = cards.get(ai);
           for (int bi = ai + 1; bi < size; bi++) {
               Card b = cards.get(bi);
               for (int ci = bi + 1; ci < size; ci++) {
                   Card c = cards.get(ci);
                   if (isSet(a, b, c)) {
                       ArrayList<Card> set = new ArrayList<Card>();
                       set.add(a);
                       set.add(b);
                       set.add(c);
                       result.add(set);
                       if (findOnlyFirstSet) return result;
                   }
               }
           }
       }
       return result;
   }

    ArrayList<Card> getSet(ArrayList<ArrayList<Card>> sets, int selectionMode) {
        if (sets.size() == 0) return new ArrayList<Card>();
        if (sets.size() == 1 || selectionMode == 1) return sets.get(0); // When only one, or if selection mode is "First found"
        ArrayList<ArrayList<Card>> pickFromThese = sets;
        if (selectionMode == 3) {
            pickFromThese = getMostSameSets(sets);
        }
         // pick randomly
        int randIndex = random.nextInt(pickFromThese.size());
        return pickFromThese.get(randIndex);
    }

    ArrayList<ArrayList<Card>> getMostSameSets(ArrayList<ArrayList<Card>> sets) {
        int bestSameProperties = 0;
        ArrayList<ArrayList<Card>> bestSets = new ArrayList<ArrayList<Card>>();
        for (ArrayList<Card> set : sets) {
            int sameProperties = getSameProperties(set);
            if (sameProperties > bestSameProperties) { // new best value
                bestSets.clear();
                bestSets.add(set);
                bestSameProperties = sameProperties;
            } else if (sameProperties == bestSameProperties) {
                bestSets.add(set);
            }  else {
                // this set is worse than currently best, just skip it
            }
        }
        return bestSets;
    }

    int getSameProperties(ArrayList<Card> set) {
        if (set.size() != 3) throw new IllegalArgumentException("the set must contain exactly 3 cards - got " + set.size());
        Card a = set.get(0);
        Card b = set.get(1);
        Card c = set.get(2);
        int sameProperties = 0;
        if (a.number == b.number && b.number == c.number) sameProperties++;
        if (a.symbol == b.symbol && b.symbol == c.symbol) sameProperties++;
        if (a.shading == b.shading && b.shading == c.shading) sameProperties++;
        if (a.colour == b.colour && b.colour == c.colour) sameProperties++;
        return sameProperties;
    }

    boolean setExists(List<Card> cards) {
        return getAllSets(cards, true).size() > 0;
    }

    void moveCards(ArrayList<Card> from, ArrayList<Card> to, int numberOfCards) {
        for (int i=0; i < numberOfCards; i++) {
            if (from.isEmpty()) break;
            to.add(from.remove(from.size() - 1));
        }
    }

    void removeCards(ArrayList<Card> toBeRemoved, ArrayList<Card> from) {
        for (Card card : toBeRemoved) {
           from.remove(card);
        }
    }

    void printCards(String text, ArrayList<Card> cards, int columns) {
        System.out.println(text);
        int columnCounter = 0;
        for (Card card : cards) {
            System.out.print(card + " ");
            columnCounter++;
            if (columnCounter >= columns) {
                System.out.println();
                columnCounter = 0;
            }
        }
        System.out.println();
    }

    private void play(ArrayList<Card> deck, int selectionMode, boolean debug) {
        int round = 0;
        boolean noSetEncountered = false;
        ArrayList<Card> table = new ArrayList<Card>();
        moveCards(deck, table, 12);
        while (!deck.isEmpty() || setExists(table)) {
            round++;
            totalRounds++;
            ArrayList<ArrayList<Card>> allSets = getAllSets(table, false);
            availableSets[deck.size()][table.size()] += allSets.size();
            ArrayList<Card> set = getSet(allSets, selectionMode);
            boolean setFound = set.size() > 0;
            if (debug) {
                System.out.println("Round " + round + " deck size=" + deck.size() + " table size=" + table.size());
                printCards("Table", table, 3);
                System.out.println("Found " + allSets.size() + " Sets:");
                int setCounter = 0;
                for (ArrayList<Card> availableSet : allSets) {
                    String chosen = availableSet.equals(set) ? " (chosen)" : "";
                    printCards("Set " + ++setCounter + chosen, availableSet, 1);
                }
                if (allSets.size() == 0) System.out.println("No Sets found!!!\n");
            }
            if (setFound) {
                setCounter[deck.size()][table.size()]++;
                removeCards(set, table);
            } else {
                noSetEncountered = true;
                noSetCounter[deck.size()][table.size()]++;
            }
            if ((setFound && table.size() < 12) || (!setFound)) {
                moveCards(deck, table, 3);
            }
        }
        // The deck is now empty, and no more sets are on the table. Update appropriate counter for no set
        noSetCounter[deck.size()][table.size()]++;
        if (!noSetEncountered) gamesWithOnly12Cards++;
    }

    private static void printResults(int tableSize) {
        System.out.println("In deck |   Set  | NoSet | Set:NoSet for " + tableSize + " | Avg # of Sets");
        System.out.println("--------+--------+-------+------------------+-------------");
        for (int i = 69; i >= 0; i-= 3) {
            long set = setCounter[i][tableSize];
            long noSet = noSetCounter[i][tableSize];
            String ratioString = "        oo";
            if (noSet > 0) {
                float ratio = (float)set / noSet;
                ratioString = String.format("%10.1f", ratio);
            }
            String avgNumSet = "  -";
            long sum = set + noSet;
            if (sum > 0) {
                avgNumSet = String.format("%3.2f", (float)availableSets[i][tableSize] / (set + noSet));
            }
            System.out.printf("   %4d |%7d |%6d |%s:1      | %s\n", i, set, noSet, ratioString, avgNumSet);
        }
        System.out.println();
    }

    private static void printHelp() {
        String  helpText = "\nSetGameSimulator simulates playing the game SET, and prints statistics from the simulation.\n" +
                "In particular it prints how many times no Set is present among the cards on the table.\n\n" +
                "Possible parameters:\n" +
                "-n x      x indicates the number of games to play (default is 10000)\n" +
                "-sm x     selection mode when there is more than one possible Set to pick from the table (default is 1):\n" +
                "          x=1 Pick the first found Set\n" +
                "          x=2 Pick randomly among the possible Sets\n" +
                "          x=3 Pick a Set where the most properties are the same\n" +
                "-seed x   x is the random number generator seed value. If none is given, a value is\n" +
                "          chosen and printed in the results. That way, the exact same simulation can\n" +
                "          be run again by giving the previous (printed) seed value as input argument\n" +
                "-debug    for each round in the game, prints the cards on the table, and the Set found\n" +
                "-h        help, prints this message and then stops the execution\n";
        System.out.println(helpText);
    }

    private static String selectionModeName(int selectionMode) {
        switch (selectionMode) {
            case 1: return "First found Set";
            case 2: return "Random among available Sets";
            case 3: return "Random among 'most similar' Sets";
            default: return "Invalid";
        }
    }

    public static String asPercent(float value) {
        return String.format("%.1f", value * 100);
    }

    public static void main(String[] args) {
        long runs = 10000;
        int selectionMode = 1;
        long seed = (System.nanoTime() % 1000000000);
        boolean debug = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-n")) {
                runs = Long.parseLong(args[++i]);
            } else if (args[i].equals("-sm")) {
                selectionMode = Integer.parseInt(args[++i]);
            } else if (args[i].equals("-seed")) {
                seed = Long.parseLong(args[++i]);
            } else if (args[i].equals("-debug")) {
                debug = true;
            } else if (args[i].equals("-h")) {
                printHelp();
                return;
            }
        }
        System.out.println("");
        random  = new Random(seed);
        long startTime = System.currentTimeMillis();
        for (long j = 0; j < runs; j++) {
            SetGame game = new SetGame();
            ArrayList<Card> deck = game.createShuffledDeck();
            game.play(deck, selectionMode, debug);
        }
        long duration = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("Total games played=" + runs +  ", total rounds=" + totalRounds + ", took " + duration + " seconds.");
        System.out.println("Games that never needed 15 cards on the table: " + gamesWithOnly12Cards + " (" +
                asPercent((float)gamesWithOnly12Cards / runs) + " percent of all games)");
        System.out.println("Games where no cards remain on the table at the end: " + noSetCounter[0][0] + " ("  +
                 asPercent((float)noSetCounter[0][0] / runs) + " percent of all games)");
        System.out.println("Selection mode=\"" + selectionModeName(selectionMode) +"\" (" +
                selectionMode + "), seed=" + seed + "\n");
        printResults(12);
        printResults(15);
        printResults(18);
        printResults(21);
    }
}
