package collections.compare.demo.cards.list;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import collections.compare.demo.cards.Card;
import collections.compare.demo.cards.Rank;
import collections.compare.demo.cards.Suit;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.ListValuedMap;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.multiset.HashMultiSet;

public class ApacheCommonsDeckOfCards
{
    private List<Card> cards;
    private MultiValuedMap<Suit, Card> cardsBySuit;

    public ApacheCommonsDeckOfCards()
    {
        this.cards = ListUtils.unmodifiableList(
                Card.streamCards().sorted().collect(Collectors.toList()));
        ListValuedMap<Suit, Card> cbs = MultiMapUtils.newListValuedHashMap();
        this.cards.forEach(card -> cbs.put(card.getSuit(), card));
        this.cardsBySuit = MultiMapUtils.unmodifiableMultiValuedMap(cbs);
    }

    public Deque<Card> shuffle(Random random)
    {
        List<Card> shuffled = new ArrayList<>(this.cards);
        Collections.shuffle(shuffled, random);
        Collections.shuffle(shuffled, random);
        Collections.shuffle(shuffled, random);
        ArrayDeque<Card> cards = new ArrayDeque<>();
        shuffled.forEach(cards::push);
        return cards;
    }

    public Set<Card> deal(Deque<Card> deque, int count)
    {
        Set<Card> hand = new HashSet<>();
        IntStream.range(0, count).forEach(i -> hand.add(deque.pop()));
        return hand;
    }

    public Card dealOneCard(Deque<Card> deque)
    {
        return deque.pop();
    }

    public List<Set<Card>> shuffleAndDeal(Random random, int hands, int cardsPerHand)
    {
        Deque<Card> shuffled = this.shuffle(random);
        return this.dealHands(shuffled, hands, cardsPerHand);
    }

    public List<Set<Card>> dealHands(Deque<Card> shuffled, int hands, int cardsPerHand)
    {
        return ListUtils.unmodifiableList(
                IntStream.range(0, hands)
                        .mapToObj(i -> this.deal(shuffled, cardsPerHand))
                        .collect(Collectors.toList()));
    }

    public List<Card> diamonds()
    {
        return MultiMapUtils.getValuesAsList(this.cardsBySuit, Suit.DIAMONDS);
    }

    public List<Card> hearts()
    {
        return MultiMapUtils.getValuesAsList(this.cardsBySuit, Suit.HEARTS);
    }

    public List<Card> spades()
    {
        return MultiMapUtils.getValuesAsList(this.cardsBySuit, Suit.SPADES);
    }

    public List<Card> clubs()
    {
        return MultiMapUtils.getValuesAsList(this.cardsBySuit, Suit.CLUBS);
    }

    public Bag<Suit> countsBySuit()
    {
        return this.cards.stream().map(Card::getSuit).collect(Collectors.toCollection(HashBag::new));
    }

    public MultiSet<Rank> countsByRank()
    {
        return this.cards.stream().map(Card::getRank).collect(Collectors.toCollection(HashMultiSet::new));
    }

    public List<Card> getCards()
    {
        return this.cards;
    }

    public MultiValuedMap<Suit, Card> getCardsBySuit()
    {
        return this.cardsBySuit;
    }
}
