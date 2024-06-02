package com.f4d4.general.tools;

public abstract class Artifact implements Validatable, Comparable<Artifact> {
    abstract public long getId();
    abstract public long getPrice();
}