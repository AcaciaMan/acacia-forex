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

import static com.forex.data.Calc.ONE_HUNDRED;
import com.forex.data.Rate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class LeastSqueares {

    public List<BigDecimal> observs = new ArrayList<>();

    public BigDecimal a = null;
    public BigDecimal b = null;

    public BigDecimal mX = null;
    public BigDecimal mY = null;

    public BigDecimal r = null;

    public BigDecimal x2 = null;
    public BigDecimal y2 = null;
    public BigDecimal xy = null;
    
    public BigDecimal percents = null;

    public BigDecimal meanX() {
        return new BigDecimal((double) (observs.size()-1) / 2);
    }

    public BigDecimal meanY() {
        BigDecimal result = new BigDecimal(0);

        for (BigDecimal bg : observs) {
            result = result.add(bg);
        }

        result = result.divide(new BigDecimal(observs.size()), 8, RoundingMode.HALF_UP);

        return result;
    }

    public void calculate() {
        mX = meanX();
        mY = meanY();

        calculateSums();

        b = xy.divide(x2, 8, RoundingMode.HALF_UP);
        a = mY.subtract(b.multiply(mX));
        BigDecimal TWO= new BigDecimal("2");
        
        percents = b.multiply(mX).multiply(TWO).multiply(ONE_HUNDRED).divide(a,2,RoundingMode.HALF_UP);

        // calculations
        // b = r*sy/sx;
        // b = xy/x2;
        // a = mY-b*mX;
        // r = sum((x-mX)*(y-mY))/sqrt(sum((x-mX)^2)*sum((y-mY)^2));
    }

    public void calculateSums() {
        xy = new BigDecimal(0);
        x2 = new BigDecimal(0);

        for (int i = 0; i < observs.size(); i++) {
            BigDecimal get = observs.get(i);
            xy = xy.add(get.subtract(mY).multiply((new BigDecimal(i)).subtract(mX)));
            x2 = x2.add((new BigDecimal(i)).subtract(mX).pow(2));
        }

    }
    
    public void loadRates(TreeSet<Rate> rates) {
        rates.forEach((Rate r1) -> {
            LeastSqueares.this.observs.add(r1.rate);
        });
    }
}
