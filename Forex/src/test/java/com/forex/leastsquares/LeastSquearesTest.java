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
package com.forex.leastsquares;

import com.forex.TestConfig;
import com.forex.leastsquares.LeastSqueares;
import java.math.BigDecimal;
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
public class LeastSquearesTest {
    
    public LeastSquearesTest() {
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
    public void calcMean () {
        
        LeastSqueares ls = new LeastSqueares();
        
        ls.observs.add(BigDecimal.ONE);
        
        System.out.println("MeanX 1: " + ls.meanX());
        System.out.println("MeanY 1: " + ls.meanY());
        
        ls.observs.add(BigDecimal.TEN);
        
        System.out.println("MeanX 10: " + ls.meanX());
        System.out.println("MeanY 10: " + ls.meanY());
        
        ls.observs.add(BigDecimal.TEN);
        
        System.out.println("MeanX 10: " + ls.meanX());
        System.out.println("MeanY 10: " + ls.meanY());
        
    }

    @Test
    public void calcAB () {
        
        LeastSqueares ls = new LeastSqueares();
        ls.observs.add(BigDecimal.ONE);
        ls.observs.add(BigDecimal.ONE);
        ls.observs.add(BigDecimal.ONE);
        ls.observs.add(BigDecimal.TEN);
        ls.observs.add(BigDecimal.TEN);
        ls.observs.add(BigDecimal.TEN);
        ls.calculate();
        
        System.out.println("mX: " + ls.mX);
        System.out.println("mY: " + ls.mY);
        System.out.println("A 10: " + ls.a);
        System.out.println("B 10: " + ls.b);
        
    }


    
}
