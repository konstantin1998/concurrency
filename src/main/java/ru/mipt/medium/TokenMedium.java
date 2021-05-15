package ru.mipt.medium;


import ru.mipt.Token;

public interface TokenMedium {
    void push(Token token) throws InterruptedException;
    Token poll() throws InterruptedException;
}
