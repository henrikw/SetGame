import junit.framework.TestCase;

import java.util.ArrayList;

public class TestSetGame extends TestCase {

    private SetGame game;

    public void setUp() {
        game = new SetGame();
    }

    // isSet

    public void testIsSet_isSet() {
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        SetGame.Card c = game.new Card(1, 0, 2, 1);
        assertTrue(game.isSet(a, b, c));
    }

    public void testIsSet_isNotSet() {
        SetGame.Card a = game.new Card(1, 0, 2, 2);
        SetGame.Card b = game.new Card(1, 0, 0, 0);
        SetGame.Card c = game.new Card(1, 0, 2, 1);
        assertFalse(game.isSet(a, b, c));
    }


    // findAllSets

    public void testFindAllSets_nullInput_givesEmptyResult() {
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(null, false);
        assertEquals(0, result.size());
    }

    public void testFindAllSets_emptyInput_givesEmptyResult() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(0, result.size());
    }

    public void testFindAllSets_twoCards_givesEmptyList() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        cards.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        cards.add(b);
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(0, result.size());
    }

    public void testFindAllSets_threeCardsNoSet_givesEmptyList() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        cards.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        cards.add(b);
        SetGame.Card c = game.new Card(1, 0, 1, 1);
        cards.add(c);
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(0, result.size());
    }

    public void testFindAllSets_threeCardsSet_givesCardsInResult() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        cards.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        cards.add(b);
        SetGame.Card c = game.new Card(1, 0, 2, 1);
        cards.add(c);
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(1, result.size());
        assertTrue(result.get(0).contains(a));
        assertTrue(result.get(0).contains(b));
        assertTrue(result.get(0).contains(c));
    }

    public void testFindAllSets_fourCardsSet_givesCardsInResult() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 1);
        cards.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        cards.add(b);
        SetGame.Card c = game.new Card(1, 0, 2, 1);
        cards.add(c);
        SetGame.Card d = game.new Card(0, 0, 2, 2);
        cards.add(d);
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(1, result.size());
        assertFalse(result.get(0).contains(a));
        assertTrue(result.get(0).contains(b));
        assertTrue(result.get(0).contains(c));
        assertTrue(result.get(0).contains(d));
    }

    public void testFindAllSets_twoSets_givesBothResult() {
        ArrayList<SetGame.Card> cards = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        cards.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 0);
        cards.add(b);
        SetGame.Card c = game.new Card(1, 0, 2, 1);
        cards.add(c);
        SetGame.Card d = game.new Card(0, 0, 1, 2);
        cards.add(d);
        SetGame.Card e = game.new Card(0, 0, 0, 2);
        cards.add(e);

        // Sets: a, b, c and a, d, e
        ArrayList<ArrayList<SetGame.Card>> result = game.findAllSets(cards, false);
        assertEquals(2, result.size());
        assertTrue(result.get(0).contains(a));
        assertTrue(result.get(0).contains(b));
        assertTrue(result.get(0).contains(c));

        assertTrue(result.get(1).contains(a));
        assertTrue(result.get(1).contains(d));
        assertTrue(result.get(1).contains(e));
    }


    // moveCards

    public void testMoveCards_emptyToEmptyFrom() {
        ArrayList<SetGame.Card> from = new ArrayList<SetGame.Card>();
        ArrayList<SetGame.Card> to = new ArrayList<SetGame.Card>();
        game.moveCards(from, to, 5);
        assertEquals(0, from.size());
        assertEquals(0, to.size());
    }

    public void testMoveCards_emptyFrom_toTheSame() {
        ArrayList<SetGame.Card> from = new ArrayList<SetGame.Card>();
        ArrayList<SetGame.Card> to = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        to.add(a);
        SetGame.Card b = game.new Card(1, 0, 1, 0);
        to.add(b);
        game.moveCards(from, to, 2);
        assertEquals(0, from.size());
        assertEquals(2, to.size());
    }

    public void testMoveCards_2inFrom_movedToTo() {
        ArrayList<SetGame.Card> from = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        from.add(a);
        SetGame.Card b = game.new Card(1, 0, 1, 0);
        from.add(b);
        SetGame.Card c = game.new Card(2, 1, 1, 0);
        from.add(c);
        ArrayList<SetGame.Card> to = new ArrayList<SetGame.Card>();
        SetGame.Card p = game.new Card(2, 0, 1, 2);
        to.add(p);
        SetGame.Card q = game.new Card(0, 0, 0, 0);
        to.add(q);
        assertEquals(3, from.size());
        assertEquals(2, to.size());
        assertTrue(from.contains(a));
        assertTrue(from.contains(b));
        assertTrue(from.contains(c));
        game.moveCards(from, to, 2);
        assertEquals(1, from.size());
        assertEquals(4, to.size());
        assertTrue(from.contains(a));
        assertTrue(to.contains(b));
        assertTrue(to.contains(c));
    }

    public void testMoveCards_3inFrom_movedToTo() {
        ArrayList<SetGame.Card> from = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 0, 2, 2);
        from.add(a);
        SetGame.Card b = game.new Card(1, 0, 1, 0);
        from.add(b);
        SetGame.Card c = game.new Card(2, 1, 1, 0);
        from.add(c);
        ArrayList<SetGame.Card> to = new ArrayList<SetGame.Card>();
        SetGame.Card p = game.new Card(2, 0, 1, 2);
        to.add(p);
        SetGame.Card q = game.new Card(0, 0, 0, 0);
        to.add(q);
        assertEquals(3, from.size());
        assertEquals(2, to.size());
        assertTrue(from.contains(a));
        assertTrue(from.contains(b));
        assertTrue(from.contains(c));
        game.moveCards(from, to, 7);
        assertEquals(0, from.size());
        assertEquals(5, to.size());
        assertTrue(to.contains(a));
        assertTrue(to.contains(b));
        assertTrue(to.contains(c));
        assertTrue(to.contains(p));
        assertTrue(to.contains(q));
    }


    // getSameProperties

    public void testGetSameProperties_fourDifferent_givesZero() {
        ArrayList<SetGame.Card> set = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 1, 2, 2);
        set.add(a);
        SetGame.Card b = game.new Card(1, 0, 1, 1);
        set.add(b);
        SetGame.Card c = game.new Card(2, 2, 0, 0);
        set.add(c);
        assertEquals(0, game.getSameProperties(set));
    }

    public void testGetSameProperties_threeDifferent_givesOne() {
        ArrayList<SetGame.Card> set = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(2, 1, 2, 2);
        set.add(a);
        SetGame.Card b = game.new Card(1, 0, 2, 1);
        set.add(b);
        SetGame.Card c = game.new Card(0, 2, 2, 0);
        set.add(c);
        assertEquals(1, game.getSameProperties(set));
    }

    public void testGetSameProperties_twoDifferent_givesTwo() {
        ArrayList<SetGame.Card> set = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(0, 1, 2, 2);
        set.add(a);
        SetGame.Card b = game.new Card(0, 0, 2, 1);
        set.add(b);
        SetGame.Card c = game.new Card(0, 2, 2, 0);
        set.add(c);
        assertEquals(2, game.getSameProperties(set));
    }

    public void testGetSameProperties_oneDifferent_givesThree() {
        ArrayList<SetGame.Card> set = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(2, 1, 2, 2);
        set.add(a);
        SetGame.Card b = game.new Card(1, 1, 2, 2);
        set.add(b);
        SetGame.Card c = game.new Card(0, 1, 2, 2);
        set.add(c);
        assertEquals(3, game.getSameProperties(set));
    }

    public void testGetSameProperties_zeroDifferent_givesFour() {
        // This can not happen in a real game - only one of each card exists
        ArrayList<SetGame.Card> set = new ArrayList<SetGame.Card>();
        SetGame.Card a = game.new Card(2, 0, 2, 1);
        set.add(a);
        SetGame.Card b = game.new Card(2, 0, 2, 1);
        set.add(b);
        SetGame.Card c = game.new Card(2, 0, 2, 1);
        set.add(c);
        assertEquals(4, game.getSameProperties(set));
    }


    // getMostSameSets

    public void testGetMostSameSets_oneOfThreeMostSame_givesOne() {
        SetGame.Card a, b, c, d, e, f, g, h, i;
        ArrayList<ArrayList<SetGame.Card>> sets = new ArrayList<ArrayList<SetGame.Card>>();
        ArrayList<SetGame.Card> set1 = new ArrayList<SetGame.Card>();
        a = game.new Card(2, 1, 2, 2);
        set1.add(a);
        b = game.new Card(1, 1, 0, 1);
        set1.add(b);
        c = game.new Card(0, 1, 1, 0);
        set1.add(c);
        assertEquals(1, game.getSameProperties(set1));
        sets.add(set1);

        ArrayList<SetGame.Card> set2 = new ArrayList<SetGame.Card>();
        d = game.new Card(2, 1, 2, 2);
        set2.add(d);
        e = game.new Card(1, 1, 2, 1);
        set2.add(e);
        f = game.new Card(0, 1, 2, 0);
        set2.add(f);
        assertEquals(2, game.getSameProperties(set2));
        sets.add(set2);

        ArrayList<SetGame.Card> set3 = new ArrayList<SetGame.Card>();
        g = game.new Card(2, 1, 2, 2);
        set3.add(g);
        h = game.new Card(1, 1, 2, 2);
        set3.add(h);
        i = game.new Card(0, 1, 2, 2);
        set3.add(i);
        assertEquals(3, game.getSameProperties(set3));
        sets.add(set3);

        ArrayList<ArrayList<SetGame.Card>> mostSameSets = game.getMostSameSets(sets);
        assertEquals(1, mostSameSets.size());
        assertEquals(set3, mostSameSets.get(0));
    }

    public void testGetMostSameSets_twoOfThreeMostSame_givesTwo() {
        SetGame.Card a, b, c, d, e, f, g, h, i;
        ArrayList<ArrayList<SetGame.Card>> sets = new ArrayList<ArrayList<SetGame.Card>>();
        ArrayList<SetGame.Card> set1 = new ArrayList<SetGame.Card>();
        a = game.new Card(2, 1, 2, 2);
        set1.add(a);
        b = game.new Card(0, 1, 2, 1);
        set1.add(b);
        c = game.new Card(1, 1, 2, 0);
        set1.add(c);
        assertEquals(2, game.getSameProperties(set1));
        sets.add(set1);

        ArrayList<SetGame.Card> set2 = new ArrayList<SetGame.Card>();
        d = game.new Card(2, 1, 2, 2);
        set2.add(d);
        e = game.new Card(1, 1, 0, 1);
        set2.add(e);
        f = game.new Card(0, 1, 1, 0);
        set2.add(f);
        assertEquals(1, game.getSameProperties(set2));
        sets.add(set2);

        ArrayList<SetGame.Card> set3 = new ArrayList<SetGame.Card>();
        g = game.new Card(2, 1, 1, 2);
        set3.add(g);
        h = game.new Card(1, 1, 1, 1);
        set3.add(h);
        i = game.new Card(0, 1, 1, 0);
        set3.add(i);
        assertEquals(2, game.getSameProperties(set3));
        sets.add(set3);

        ArrayList<ArrayList<SetGame.Card>> mostSameSets = game.getMostSameSets(sets);
        assertEquals(2, mostSameSets.size());
        assertEquals(set1, mostSameSets.get(0));
        assertEquals(set3, mostSameSets.get(1));
    }
}
