package com.example.myapplication;

public class LCG {
    private long a = 1103515245L;
    private long c = 12345L;
    private long m = (long)Math.pow(2, 31);
    private long seed;

    public LCG(long seed) {
        this.seed = seed & 0xFFFFFFFFL;
    }

    public int next() {
        seed = (seed * a + c) % m;
        return (int)seed;
    }
}
