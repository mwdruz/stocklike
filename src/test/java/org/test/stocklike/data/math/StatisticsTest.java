package org.test.stocklike.data.math;

import static io.vavr.API.Tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.test.stocklike.domain.entity.Hgram;
import org.test.stocklike.domain.entity.Price;

class StatisticsTest {
    private static final double BIN_ZERO = 0;
    private static final double BIN_ANY = 13125;
    private static final double BIN_OF_ONE = 1;
    private static final double BIN_COMPLEX = 1.5;
    
    private static final double PRICE15 = 15;
    private static final double PRICE16 = 16;
    private static final double PRICE17 = 17;
    private static final double PRICE18 = 18;
    private static final double PRICE19 = 19;
    private static final double PRICE20 = 20;
    private static final double PRICE35 = 35;
    
    private static final List<Price> EMPTY_DATA = List.of();
    private static final List<Price> SINGLE_DATA = List.of(new Price(PRICE15));
    private static final List<Price> ONE_BAR_DATA = List.of(new Price(PRICE15),
                                                            new Price(PRICE15),
                                                            new Price(PRICE15));
    private static final List<Price> TWO_BARS_BESIDE_DATA = List.of(new Price(PRICE15),
                                                                    new Price(PRICE15),
                                                                    new Price(PRICE15),
                                                                    new Price(PRICE15),
                                                                    new Price(PRICE15),
                                                                    new Price(PRICE15),
                                                                    new Price(PRICE16),
                                                                    new Price(PRICE16));
    private static final List<Price> TWO_BARS_SPACED_CLOSE_DATA = List.of(new Price(PRICE15),
                                                                          new Price(PRICE15),
                                                                          new Price(PRICE15),
                                                                          new Price(PRICE15),
                                                                          new Price(PRICE15),
                                                                          new Price(PRICE20),
                                                                          new Price(PRICE20),
                                                                          new Price(PRICE20));
    private static final List<Price> TWO_BARS_SPACED_FAR_DATA = List.of(new Price(PRICE15),
                                                                        new Price(PRICE15),
                                                                        new Price(PRICE35),
                                                                        new Price(PRICE35),
                                                                        new Price(PRICE35),
                                                                        new Price(PRICE35));
    
    private static final Hgram EMPTY_HGRAM = Hgram.fromList(BIN_ANY, List.of());
    private static final Hgram SINGLE_DATA_HGRAM =
            Hgram.fromList(BIN_ANY, List.of(Tuple(PRICE15, 1)));
    private static final Hgram ONE_BAR_HGRAM =
            Hgram.fromList(BIN_ANY, List.of(Tuple(PRICE15, 3)));
    private static final Hgram TWO_BARS_BESIDE_HGRAM =
            Hgram.fromList(BIN_OF_ONE, List.of(Tuple(PRICE15, 6),
                                               Tuple(PRICE16, 2)));
    
    private static final Hgram TWO_BARS_SPACED_CLOSE_HGRAM =
            Hgram.fromList(BIN_OF_ONE, List.of(Tuple(PRICE15, 5),
                                               Tuple(PRICE16, 0),
                                               Tuple(PRICE17, 0),
                                               Tuple(PRICE18, 0),
                                               Tuple(PRICE19, 0),
                                               Tuple(PRICE20, 3)));
    
    private static final Hgram TWO_BARS_SPACED_CLOSE_COMPLEX_BIN_HGRAM =
            Hgram.fromList(BIN_COMPLEX, List.of(Tuple(PRICE15, 5),
                                                Tuple(PRICE15 + BIN_COMPLEX, 0),
                                                Tuple(PRICE15 + 2 * BIN_COMPLEX, 0),
                                                Tuple(PRICE15 + 3 * BIN_COMPLEX, 3)));
    
    private static final double BIN_AUTO = (PRICE35 - PRICE15) / 10;
    private static final Hgram TWO_BARS_SPACED_FAR_BIN_AUTO_HGRAM =
            Hgram.fromList(BIN_AUTO, List.of(Tuple(PRICE15, 2),
                                             Tuple(PRICE15 + 1 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 2 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 3 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 4 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 5 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 6 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 7 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 8 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 9 * BIN_AUTO, 0),
                                             Tuple(PRICE15 + 10 * BIN_AUTO, 4)));
    
    private Statistics statistics;
    
    @BeforeEach
    void setUp() { statistics = new StatisticsImpl(); }
    
    @Test
    void emptyData()
    {
        statistics.loadData(EMPTY_DATA);
        assertEquals(EMPTY_HGRAM, statistics.getHgram(BIN_ANY));
    }
    
    @Test
    void singleData()
    {
        statistics.loadData(SINGLE_DATA);
        assertEquals(SINGLE_DATA_HGRAM, statistics.getHgram(BIN_ANY));
    }
    
    @Test
    void oneBarData()
    {
        statistics.loadData(ONE_BAR_DATA);
        assertEquals(ONE_BAR_HGRAM, statistics.getHgram(BIN_ANY));
    }
    
    @Test
    void twoBarsBesideData()
    {
        statistics.loadData(TWO_BARS_BESIDE_DATA);
        assertEquals(TWO_BARS_BESIDE_HGRAM, statistics.getHgram(BIN_OF_ONE));
    }
    
    @Test
    void twoBarSpacedCloseData()
    {
        statistics.loadData(TWO_BARS_SPACED_CLOSE_DATA);
        assertEquals(TWO_BARS_SPACED_CLOSE_HGRAM, statistics.getHgram(BIN_OF_ONE));
    }
    
    @Test()
    void twoBarSpacedCloseDataComplexBin()
    {
        statistics.loadData(TWO_BARS_SPACED_CLOSE_DATA);
        assertEquals(TWO_BARS_SPACED_CLOSE_COMPLEX_BIN_HGRAM, statistics.getHgram(BIN_COMPLEX));
    }
    
    @Test
    void autoBinForTwoBarSpacedCloseData()
    {
        statistics.loadData(TWO_BARS_SPACED_CLOSE_DATA);
        assertEquals(TWO_BARS_SPACED_CLOSE_HGRAM, statistics.getHgram(BIN_ZERO));
    }
    
    @Test
    void autoBinForTwoBarSpacedFarData()
    {
        statistics.loadData(TWO_BARS_SPACED_FAR_DATA);
        assertEquals(TWO_BARS_SPACED_FAR_BIN_AUTO_HGRAM, statistics.getHgram(BIN_ZERO));
    }
}
