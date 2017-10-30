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

public enum CalcPeriodEnum {
    ALL ("All") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair();
        }
        
    },
    YEAR5 ("5 Years") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair5Years();
        }
    },
    YEAR1 ("1 Year") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair1Year();
        }
    },
    HALF ("1 Half") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair1Half();
        }
    },
    QUARTER ("1 Quarter") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair1Quarter();
        }
    },
    MONTH ("1 Month") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair1Month();
        }
    },
    WEEK ("1 Week") {
        @Override
        public CurrencyPair makeCurrencyPair() {
            return new CurrencyPair1Week();
        }
    };
    
    
    private final String name;       

    private CalcPeriodEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return name.equals(otherName);
    }

    @Override
    public String toString() {
       return this.name;
    }
    
    public abstract CurrencyPair makeCurrencyPair();
}
