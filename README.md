# Multipliers
Two and a half different approaches at summing a series of multipliers.


To build:  

`mvn clean install`

To run:  

`java -jar Multipliers-1.0-SNAPSHOT.jar -i 2 -wi 2 -f 3 -t 1 -r 5 -w 1 --jvmArgs="-XX:+AggressiveOpts -Xmx50m -Xms50m"`

Example output for `multipliers == {3,17}`:

    Benchmark                  (cap)   Mode  Cnt         Score         Error  Units
    MainClass.checking            10  thrpt    6  21958529.208 ± 1199835.156  ops/s
    MainClass.checking        100000  thrpt    6      2151.927 ±     268.377  ops/s
    MainClass.checking     100000000  thrpt    6         1.632 ±       0.015  ops/s
    MainClass.correctness         10  thrpt    6  15862262.728 ±  785196.684  ops/s
    MainClass.correctness     100000  thrpt    6      1482.465 ±     146.793  ops/s
    MainClass.correctness  100000000  thrpt    6         1.289 ±       0.054  ops/s
    MainClass.generating          10  thrpt    6  77469755.890 ± 1387410.313  ops/s
    MainClass.generating      100000  thrpt    6      9937.275 ±    2316.086  ops/s
    MainClass.generating   100000000  thrpt    6         9.006 ±       1.870  ops/s
    
Here we can see, that at least for this set of multipliers generating is much faster.
