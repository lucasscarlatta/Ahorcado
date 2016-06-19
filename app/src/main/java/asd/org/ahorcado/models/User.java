/**
 * Muber 2016. Copyright © All rights reserved.
 */
package asd.org.ahorcado.models;

public class User {

    private Long id;
    private String userName;
    private Coin coin;

    public User(Long id, String name) {
        this.id = id;
        this.userName = name;
    }
}