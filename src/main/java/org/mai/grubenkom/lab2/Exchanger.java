package org.mai.grubenkom.lab2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

@Data
@AllArgsConstructor
class Coin {
    private int denomination;
    private int quantity;
}

@Data
class ExchangeResult {
    private HashMap<Integer, Integer> exchangeMap;
    private boolean success;
    ExchangeResult() {
        exchangeMap = new HashMap<>();
        success = false;
    }
}

public class Exchanger {
    private final ArrayList<Coin> coinList;
    private final ExchangeResult exchangeResult;

    Exchanger() {
        coinList = new ArrayList<>();
        exchangeResult = new ExchangeResult();

        coinList.add(new Coin(5, 3));
        coinList.add(new Coin(10, 1));
        coinList.add(new Coin(1, 3));

        coinList.sort(Comparator.comparingInt(Coin::getDenomination).reversed());
    }
    private ExchangeResult _exchange(int amountOfMoney, int coinListIndex) {
        int countOfCoins = 0;
        int denomination = coinList.get(coinListIndex).getDenomination();
        int quantity = coinList.get(coinListIndex).getQuantity();

        while (amountOfMoney >= denomination && quantity != 0) {
            countOfCoins++;
            amountOfMoney -= denomination;
            quantity--;
        }

        if (countOfCoins != 0) {
            exchangeResult.getExchangeMap().put(denomination, countOfCoins);
        }
        if (amountOfMoney != 0 && coinList.size() > coinListIndex + 1) {
            return _exchange(amountOfMoney, coinListIndex + 1);
        } else if (amountOfMoney != 0 && coinList.size() <= coinListIndex + 1){
            exchangeResult.getExchangeMap().clear();
            exchangeResult.setSuccess(false);
            return exchangeResult;
        }
        exchangeResult.setSuccess(true);
        return exchangeResult;
    }

    public void exchange(int amountOfMoney) {
        System.out.printf("Change money in denominations of %d\n", amountOfMoney);
        System.out.println("Available for exchange:");
        coinList.forEach((coin) -> System.out.println(coin.getQuantity() + " coins in denominations of " + coin.getDenomination()));
        ExchangeResult result = _exchange(amountOfMoney, 0);
        if (exchangeResult.isSuccess()) {
            System.out.println("Exchanged!");
            result.getExchangeMap().forEach((key, value) -> System.out.println(value + " coins in denominations of " + key));
        } else {
            System.err.println("No exchange possible");
        }
    }
}