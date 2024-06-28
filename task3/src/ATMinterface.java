import java.util.Scanner;

public class ATMinterface {

    public static void main(String[] args) {
        User user = new User();
        Scanner s = new Scanner(System.in);
        boolean continueSession = true;

        System.out.println("**** Bank of Java ****");
        System.out.print("Enter username: ");
        String username = s.nextLine();
        System.out.print("Enter PIN: ");
        int pin = s.nextInt();

        if (Validation.validateUser(user, username, pin)) {
            while (continueSession) {
                System.out.print(
                        "\nChoose your option:\n\n1. Withdraw\n2. Deposit\n3. Check Balance\n4. Exit\nEnter your choice: ");
                int choice = s.nextInt();
                int amount;
                switch (choice) {
                    case 1:
                        System.out.print("\nEnter the amount you want to withdraw: ");
                        amount = s.nextInt();
                        if (Validation.validateTransactionPin(user, s)) {
                            ATM.withdraw(user, amount);
                        } else {
                            System.out.println("Invalid transaction PIN, transaction failed.");
                        }
                        break;
                    case 2:
                        System.out.print("\nEnter the amount you want to deposit: ");
                        amount = s.nextInt();
                        if (Validation.validateTransactionPin(user, s)) {
                            ATM.deposit(user, amount);
                        } else {
                            System.out.println("Invalid transaction PIN, transaction failed.");
                        }
                        break;
                    case 3:
                        ATM.checkBalance(user);
                        break;
                    case 4:
                        continueSession = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } else {
            System.out.println("Invalid credentials, logging out.");
        }

        s.close();
    }
}

class Validation {

    public static boolean validateUser(User user, String username, int pin) {
        return user.getUserName().equals(username) && user.getPin() == pin;
    }

    public static boolean validateTransactionPin(User user, Scanner s) {
        System.out.print("Enter transaction PIN: ");
        int transactionPin = s.nextInt();
        return transactionPin == user.getTransactionPin();
    }
}

class ATM {
    public static void withdraw(User user, int amount) {
        if (amount > user.getBalance()) {
            System.out.println("Insufficient balance.");
        } else {
            user.setBalance(user.getBalance() - amount);
            System.out.println("Withdrawal successful. New balance: " + user.getBalance());
        }
    }

    public static void deposit(User user, int amount) {
        if (amount > 0) {
            user.setBalance(user.getBalance() + amount);
            System.out.println("Deposit successful. New balance: " + user.getBalance());
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public static void checkBalance(User user) {
        System.out.println("Current balance: " + user.getBalance());
    }
}

class User {
    private String userName = "sneh";
    private int pin = 1154;
    private int transactionPin = 2212;
    private int balance = 10000;

    public String getUserName() {
        return userName;
    }

    public int getPin() {
        return pin;
    }

    public int getTransactionPin() {
        return transactionPin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        if (balance >= 0) {
            this.balance = balance;
        } else {
            System.out.println("Balance cannot be negative.");
        }
    }
}
