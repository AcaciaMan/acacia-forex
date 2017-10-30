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

import static com.forex.data.Calc.ONE_HUNDRED;
import com.forex.leastsquares.LeastSqueares;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.TreeSet;

public class CurrencyPair implements Comparable<CurrencyPair> {

    protected Currency cur;
    protected Currency ref;
    protected TreeSet<Rate> rates = new TreeSet<Rate>();
    protected CalcPeriodEnum periodEnum;

    protected CurrencyPair cpAll;
    protected Rate rateMin;
    protected Rate rateMax;
    protected Integer percents;
    protected LeastSqueares ls;

    public CurrencyPair() {

        periodEnum = CalcPeriodEnum.ALL;

    }

    @Override
    public int compareTo(CurrencyPair o) {
        if (!(this.cur.compareTo(o.cur) == 0)) {
            return this.cur.compareTo(o.cur);
        }
        return this.ref.compareTo(o.ref);
    }

    @Override
    public String toString() {
        return this.cur.toString() + this.ref.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public Rate getMin() {
        Rate result = rates.first();
        // go through rates and search for minimum rate
        for (Rate r : rates) {
            if (result.rate == null) {
                result = r;
            } else if (r.rate == null) {
                continue;
            } else if (result.rate.compareTo(r.rate) > 0) {
                result = r;
            }
        }

        return result;
    }

    public Rate getMax() {
        Rate result = rates.first();
        // go through rates and search for minimum rate
        for (Rate r : rates) {
            if (result.rate == null) {
                result = r;
            } else if (r.rate == null) {
                continue;
            } else if (result.rate.compareTo(r.rate) < 0) {
                result = r;
            }
        }

        return result;
    }

    public void loadRates(TreeSet<Rate> ratesAll) {
        LocalDate startDate = findStart(ratesAll.last());

        for (Rate r : ratesAll) {
            if (startDate.isBefore(r.refDate) || startDate.isEqual(r.refDate)) {
                rates.add(r);
            }
        }

    }

    public LocalDate findStart(Rate lastRate) {
        // find date
        // get year
        // get 01.01 of that year
        // get -4 years of that date

        Integer year = lastRate.refDate.getYear();
        LocalDate result = LocalDate.of(year - 30, 1, 1);

        return result;

    }

    /**
     * @return the cur
     */
    public Currency getCur() {
        return cur;
    }

    /**
     * @param cur the cur to set
     */
    public void setCur(Currency cur) {
        this.cur = cur;
    }

    /**
     * @return the ref
     */
    public Currency getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(Currency ref) {
        this.ref = ref;
    }

    /**
     * @return the rates
     */
    public TreeSet<Rate> getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(TreeSet<Rate> rates) {
        this.rates = rates;
    }

    /**
     * @return the periodEnum
     */
    public CalcPeriodEnum getPeriodEnum() {
        return periodEnum;
    }

    /**
     * @param periodEnum the periodEnum to set
     */
    public void setPeriodEnum(CalcPeriodEnum periodEnum) {
        this.periodEnum = periodEnum;
    }

    /**
     * @return the cpAll
     */
    public CurrencyPair getCpAll() {
        return cpAll;
    }

    /**
     * @param cpAll the cpAll to set
     */
    public void setCpAll(CurrencyPair cpAll) {
        this.cpAll = cpAll;
        
        
        this.cur = cpAll.cur;
        this.ref = cpAll.ref;

    }

    /**
     * @return the rateMin
     */
    public Rate getRateMin() {
        return rateMin;
    }

    /**
     * @param rateMin the rateMin to set
     */
    public void setRateMin(Rate rateMin) {
        this.rateMin = rateMin;
    }

    /**
     * @return the rateMax
     */
    public Rate getRateMax() {
        return rateMax;
    }

    /**
     * @param rateMax the rateMax to set
     */
    public void setRateMax(Rate rateMax) {
        this.rateMax = rateMax;
    }

    /**
     * @return the percents
     */
    public Integer getPercents() {
        return percents;
    }

    /**
     * @param percents the percents to set
     */
    public void setPercents(Integer percents) {
        this.percents = percents;
    }

    /**
     * @return the ls
     */
    public LeastSqueares getLs() {
        return ls;
    }

    /**
     * @param ls the ls to set
     */
    public void setLs(LeastSqueares ls) {
        this.ls = ls;
    }

    public void calculate() {
        
            loadRates(cpAll.getRates());

            rateMin = getMin();
            rateMax = getMax();

            percents = rateMin.rate.multiply(ONE_HUNDRED).divide(rateMax.rate, 0, RoundingMode.HALF_UP).intValue(); //Also tried with RoundingMode.UP

            ls = new LeastSqueares();
            ls.loadRates(rates);
            ls.calculate();

    }

}
