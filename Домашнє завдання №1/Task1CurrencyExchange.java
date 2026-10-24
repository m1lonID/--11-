package m1lon;

public class Task1CurrencyExchange {
    public static void main(String[] args) {
        double usdAmount = 250.;
        final double EXCHANGE_RATE = 41.25;
        final double COMMISSION_PERCENT = 1.5;
        double uahAmount = usdAmount * EXCHANGE_RATE;

        double commission = uahAmount * COMMISSION_PERCENT / 100;
        double netAmount = uahAmount - commission;
        long cashAmount = Math.round(netAmount);
        System.out.println("============== КВИТАНЦІЯ ОБМІНУ ==============");
        System.out.println("Сума до обміну:      " + usdAmount + " USD");
        System.out.println("Курс валют:          " + EXCHANGE_RATE + " UAH/USD");
        System.out.println("Нараховано до комісії: " + uahAmount + " UAH");
        System.out.println("Комісія банку (1.5%):  " + commission + " UAH");
        System.out.println("-----------------------------------------------");
        System.out.println("Чиста сума до видачі: " + netAmount + " UAH");
        System.out.println("До видачі готівкою:   " + cashAmount + " UAH");
        System.out.println("===============================================");
    }
}





