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

import java.math.BigDecimal;
import java.time.LocalDate;

public class Rate implements Comparable<Rate>{
    public CurrencyPair cp;
    public LocalDate refDate;
    public BigDecimal rate;

    @Override
    public int compareTo(Rate r) {
        if (!(this.cp.compareTo(r.cp) == 0))
            return this.cp.compareTo(r.cp);
        return this.refDate.compareTo(r.refDate);


    }

    @Override
    public String toString() {
        return this.cp.toString() + " " + this.refDate + " " + this.rate;
    }
    
}
