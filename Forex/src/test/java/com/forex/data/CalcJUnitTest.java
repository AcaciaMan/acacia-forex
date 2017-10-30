/*
 * Copyright 2017 Acacia Man
 * The program is distributed under the terms of the GNU General Public License
 * 
 * This file is part of acacia-forex.
 *
 * acacia-forex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * acacia-forex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with acacia-forex.  If not, see <http://www.gnu.org/licenses/>.
 */ 
package com.forex.data;

import com.forex.TestConfig;
import com.forex.data.CurrencyPair1Half;
import com.forex.data.Calc;
import com.forex.data.CurrencyPair5Years;
import com.forex.data.CurrencyPair;
import com.forex.data.Rate;
import com.forex.data.CurrencyPair1Year;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.forex.leastsquares.LeastSqueares;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)


public class CalcJUnitTest {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public CalcJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void loadECB() {
        Calc calc = new Calc();
        calc.loadECB();

        for (String s : calc.curs) {
            System.out.println("Currency: " + s);
        }

        System.out.println("Currency1: " + calc.curs[1]);
        System.out.println("mCurs: " + calc.mCurs);
        System.out.println("mCurPairs: " + calc.mCurPairs);
        System.out.println("ecbRates: " + calc.ecbRates.size());
        System.out.println("First rate: " + calc.ecbRates.first());
        System.out.println("Last rate: " + calc.ecbRates.last());
    }

    @Test
    public void printMinMax() {
        Calc calc = new Calc();
        calc.loadECB();

        for (String s : calc.mCurPairs.keySet()) {

            CurrencyPair cp = calc.mCurPairs.get(s);

            Rate rMin = cp.getMin();
            Rate rMax = cp.getMax();

            Integer i = rMin.rate.multiply(ONE_HUNDRED).divide(rMax.rate, 0, RoundingMode.HALF_UP).intValue(); //Also tried with RoundingMode.UP

            LeastSqueares ls = new LeastSqueares();
            ls.loadRates(cp.getRates());
            ls.calculate();

            System.out.println("CurrencyPair Min: " + rMin + " " + i + "%");
            System.out.println("CurrencyPair Max: " + rMax + " b:" + ls.b + " a:" + ls.a + " mX: " + ls.mX + " mY:" + ls.mY);
        }

    }

    @Test
    public void printMinMax5Years() {
        Calc calc = new Calc();
        calc.loadECB();

        for (String s : calc.mCurPairs.keySet()) {

            CurrencyPair cpAll = calc.mCurPairs.get(s);
            CurrencyPair5Years cp = new CurrencyPair5Years();
            cp.setCur(cpAll.getCur());
            cp.setRef(cpAll.getRef());
            cp.loadRates(cpAll.getRates());

            Rate rMin = cp.getMin();
            Rate rMax = cp.getMax();

            Integer i = rMin.rate.multiply(ONE_HUNDRED).divide(rMax.rate, 0, RoundingMode.HALF_UP).intValue(); //Also tried with RoundingMode.UP

            LeastSqueares ls = new LeastSqueares();
            ls.loadRates(cp.getRates());
            ls.calculate();

            System.out.println("CurrencyPair Min: " + rMin + " " + i + "%");
            System.out.println("CurrencyPair Max: " + rMax + " b:" + ls.b + " a:" + ls.a + " mX: " + ls.mX + " mY:" + ls.mY);
        }

    }

        @Test
    public void printMinMax1Year() {
        Calc calc = new Calc();
        calc.loadECB();

        for (String s : calc.mCurPairs.keySet()) {

            CurrencyPair cpAll = calc.mCurPairs.get(s);
            CurrencyPair1Year cp = new CurrencyPair1Year();
            cp.setCur(cpAll.getCur());
            cp.setRef(cpAll.getRef());
            cp.loadRates(cpAll.getRates());

            Rate rMin = cp.getMin();
            Rate rMax = cp.getMax();

            Integer i = rMin.rate.multiply(ONE_HUNDRED).divide(rMax.rate, 0, RoundingMode.HALF_UP).intValue(); //Also tried with RoundingMode.UP

            LeastSqueares ls = new LeastSqueares();
            ls.loadRates(cp.getRates());
            ls.calculate();

            System.out.println("CurrencyPair Min: " + rMin + " " + i + "%");
            System.out.println("CurrencyPair Max: " + rMax + " b:" + ls.b + " a:" + ls.a + " mX: " + ls.mX + " mY:" + ls.mY);
        }
    }
                @Test
    public void printMinMax1Half() {
        Calc calc = new Calc();
        calc.loadECB();

        for (String s : calc.mCurPairs.keySet()) {

            CurrencyPair cpAll = calc.mCurPairs.get(s);
            CurrencyPair1Half cp = new CurrencyPair1Half();
            cp.setCur(cpAll.getCur());
            cp.setRef(cpAll.getRef());
            cp.loadRates(cpAll.getRates());

            Rate rMin = cp.getMin();
            Rate rMax = cp.getMax();

            Integer i = rMin.rate.multiply(ONE_HUNDRED).divide(rMax.rate, 0, RoundingMode.HALF_UP).intValue(); //Also tried with RoundingMode.UP

            LeastSqueares ls = new LeastSqueares();
            ls.loadRates(cp.getRates());
            ls.calculate();

            System.out.println("CurrencyPair Min: " + rMin + " " + i + "%");
            System.out.println("CurrencyPair Max: " + rMax + " b:" + ls.b + " a:" + ls.a + " mX: " + ls.mX + " mY:" + ls.mY);
        }

        
    }
    
}
