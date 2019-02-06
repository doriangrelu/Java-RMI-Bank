package Management;

import java.util.UUID;

public class Account {

    private int _id;
    private String _firstname;
    private String _lastname;
    private double _amount;
    private static int uuid = 100;


    public Account(String _lastname, String _firstname, double _amount) {
        this._id = Account.uuid;
        Account.uuid++;
        this._firstname = _firstname;
        this._lastname = _lastname;
        this._amount = _amount;
    }

    public int getId() {
        return _id;
    }

    public Account(String _firstname, String _lastname) {
        this(_firstname, _lastname, 0);
    }

    public String getFirstname() {
        return _firstname;
    }

    public String getLastname() {
        return _lastname;
    }

    public double getAmount() {
        return _amount;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this._amount += amount;
        return true;
    }

    public boolean getMoney(double amount) {
        double resultOperation = this._amount - amount;
        if (resultOperation < -100) {
            return false;
        }
        this._amount = resultOperation;
        return true;

    }

}
