package com.example.Task_Manager.mapper;

public interface Mapper<A,B> {
    B mapTo(A a);
    A mapFrom(B b);
}
