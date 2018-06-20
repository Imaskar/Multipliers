package gq.imaskar.multipliers;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 *
 * @author imaskar
 */
@State(Scope.Benchmark)
public class MainClass {
  
  @Param({"10", "100000", "100000000"})
  public static int cap;
  
  private static final int[] MULTIPLIERS = {3, 17};
  
  @Benchmark
  public long checking(){
    return sumChecking(cap, MULTIPLIERS);
  }

  @Benchmark
  public long generating(){
    return sumGenerating(cap, MULTIPLIERS);
  }

//  @Benchmark
//  public long generateTwo(){
//    return sumGenerating(cap, MULTIPLIERS[0],MULTIPLIERS[1]);
//  }
  
  /**
   * This is not actually a benchmark, but we check that we compare the same 
   * things.
   * @return 1
   */
  @Benchmark
  public long correctness(){
    final long sumChecking = sumChecking(cap, MULTIPLIERS);
    final long sumGenerating = sumGenerating(cap, MULTIPLIERS);
//    final long sumOnlyTwoMultipliers = sumOnlyTwoMultipliers(cap, MULTIPLIERS[0], MULTIPLIERS[1]);
    if (sumChecking!=sumGenerating /*|| sumOnlyTwoMultipliers!=sumGenerating*/) throw new RuntimeException();
    return 1l;
  }
  
  private long sumChecking(int cap, int... multi){
    long sum = 0;
    if (multi == null || multi.length == 0) return sum;
    for (int i=1; i<cap; i++){
      for (int j : multi){
        if (i % j == 0) {
          sum+=i;
          break;
        }
      }
    }
    return sum;
  }
  
  private long sumGenerating(int cap, int... multi) {
    long sum = 0;
    int[] series = new int[multi.length];
    int min,minval;
    while (true) {
      min = 0;
      minval = series[0]+multi[0];
      for (int i = 1; i < series.length; i++) {
        final int currval = series[i]+multi[i];
        if (minval>currval) {
          min = i; 
          minval = currval;
        }
      }
      if (minval>=cap) break;
      boolean add = true;
      for (int i = 0; i < series.length; i++) {
        if (minval == series[i]) {
          add = false;
          break;
        }
      }
      if (add) sum+=minval;
      series[min] = minval;
    } 
    return sum;
  }
  
  /**
   * I initially thought this to be much faster than generic sumGenerating, but
   * it turns out that JVM is not smart (risky?) enough to put everything in
   * processor registers for turbo speed.
   * @param cap
   * @param a
   * @param b
   * @return 
   */
  private long sumOnlyTwoMultipliers(int cap, int a, int b) {
    long sum = 0;
    int seriesA = 0, seriesB = 0;
    while (true) {
      final int nextA = seriesA + a;
      final int nextB = seriesB + b;
      if (nextA < nextB) {
        if (nextA>=cap) break;
        if (nextA != seriesB) {
          sum += nextA;
        }
        seriesA = nextA;
      } else {
        if (nextB>=cap) break;
        if (nextB != seriesA) {
          sum += nextB;
        }
        seriesB = nextB;
      }
    }
    return sum;
  }
  
  
  
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(MainClass.class.getSimpleName())
        .timeUnit(TimeUnit.NANOSECONDS) // many of this options are not worknig for some reason
        .warmupTime(TimeValue.seconds(5))
        .warmupIterations(2)
        .measurementTime(TimeValue.seconds(10))
        .measurementIterations(5)
        .forks(5)
        .threads(1)
        .mode(Mode.Throughput)
        .shouldFailOnError(true)
        .shouldDoGC(true)
        .build();

    new Runner(opt).run();
  }
  
}
