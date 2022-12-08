package org.mai.grubenkom.lab2;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Scanner;

@Data
@AllArgsConstructor
class Money {
    private int denomination;
    private int quantity;
}

public class Exchanger {
    ArrayList<Money> exchange = new ArrayList<>();

    private ArrayList<Money> _exchange(ArrayList<Money> exchange, int index, int sum, ArrayList<Money> lastSums, int lastSum) {
        if (exchange.size() == index) return null;

        for (int i = exchange.get(index).getQuantity(); i >= 0; i--) {
            if (sum == lastSum + exchange.get(index).getDenomination() * i) {
                lastSums.add(new Money(exchange.get(index).getDenomination(), i));
                return lastSums;
            }
            else if (sum > lastSum + exchange.get(index).getDenomination() * i) {
                ArrayList<Money> last = new ArrayList<>(lastSums);
                last.add(new Money(exchange.get(index).getDenomination(), i));
                ArrayList<Money> result = _exchange(exchange, index + 1, sum, last, lastSum + exchange.get(index).getDenomination() * i);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
    private void printArray(ArrayList<Money> arrayList) {
        arrayList.stream()
                .filter(money -> money.getQuantity() > 0)
                .forEach(money -> System.out.println("Номинал: " + money.getDenomination() +
                        ", Количество: " + money.getQuantity()));
    }

    public void doExchange() {
        int sum = configure();
        ArrayList<Money> resultOfExchange = _exchange(exchange, 0, sum, new ArrayList<>(), 0);
        if (resultOfExchange == null) {
            System.out.println("Размен невозможен");
        } else {
            System.out.printf("Размен для %d\n", sum);
            printArray(resultOfExchange);
        }
    }
    private int configure() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введи сумму для размена ");
        int sum = scanner.nextInt();

        exchange.add(new Money(7, 10));
        exchange.add(new Money(5, 10));
//        exchange.add(new Money(1, 10));

        printArray(exchange);
        return sum;
    }
}
