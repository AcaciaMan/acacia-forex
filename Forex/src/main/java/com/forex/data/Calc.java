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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class Calc {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public TreeSet<Rate> ecbRates = new TreeSet<Rate>();
    public String[] curs;
    public HashMap<String, Currency> mCurs = new HashMap<String, Currency>();
    public HashMap<String, CurrencyPair> mCurPairs = new HashMap<String, CurrencyPair>();

    public void loadECB() {
        // open file C:\Work\forex\Rates\eurofxref-hist.csv
        InputStream is = Calc.class.getResourceAsStream("/eurofxref-hist.csv");

        
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

         // reads each line
        List<String> fileLines = new ArrayList<>();
        String l;
        try {
            while((l = br.readLine()) != null) {
                fileLines.add(l); 
            }
            is.close();
        } catch (IOException ex) {
            Logger.getLogger(Calc.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
        
        
            int i = 0;
            for (String s : fileLines) {
                i++;
                if (i == 1) {
                    Currency eurCurrency = new Currency();
                    eurCurrency.name = "EUR";

                    mCurs.put("EUR", eurCurrency);

                    curs = s.split(",");
                    //System.out.println(curs);

                    int j = 0;
                    for (String c : curs) {
                        j++;
                        if (j == 1) {
                            continue;
                        }
                        Currency currency = new Currency();
                        currency.name = c;
                        //System.out.println("=== currency: " + c);
                        mCurs.put(c, currency);

                        CurrencyPair cp = new CurrencyPair();
                        cp.setCur(eurCurrency);
                        cp.setRef(currency);
                        mCurPairs.put(cp.toString(), cp);
                    }

                } else {
                    String[] rates = s.split(",");
                    LocalDate d = null;
                    BigDecimal bd = null;
                    Rate rate;
                    CurrencyPair cp;
                    int j = 0;
                    for (String r : rates) {

                        if (j == 0) {
                            d = LocalDate.parse(r);
                        } else {
                            if ("N/A".equals(r)) {
                                bd = null;
                            } else {
                                bd = new BigDecimal(r);
                            }
                            if (bd != null) {
                                rate = new Rate();
                                cp = mCurPairs.get("EUR" + curs[j]);
                                rate.refDate = d;
                                rate.cp = cp;
                                rate.rate = bd;
                                cp.getRates().add(rate);
                                ecbRates.add(rate);

                            }
                        }
                        j++;
                    }
                }
            }

    }

    public void calcCurrencyPairStats(CalcPeriodEnum periodEnum, JTextArea jTextArea1, JTable jTable1) {

        DefaultTableModel tableModel = (DefaultTableModel) jTable1.getModel();

        for (String s : mCurPairs.keySet()) {

            CurrencyPair cpAll = mCurPairs.get(s);
            CurrencyPair cp = periodEnum.makeCurrencyPair();
            cp.setCpAll(cpAll);
            cp.calculate();

            //System.out.println("CurrencyPair Min: " + rMin + " " + i + "%");
            //System.out.println("CurrencyPair Max: " + rMax + " b:" + ls.b + " a:" + ls.a + " mX: " + ls.mX + " mY:" + ls.mY);
            jTextArea1.append("CurrencyPair Min:" + cp.getRateMin() + " Max:" + cp.getRateMax() + " " + cp.getPercents() + "% b%:" + cp.ls.percents  +" b:" + cp.ls.b + " a:" + cp.ls.a + " mX:" + cp.ls.mX + " mY:" + cp.ls.mY + "\n");
            tableModel.addRow(new Object[]{s, cp.getRateMin().toString(), cp.getRateMax().toString(), cp.getPercents().toString(), cp.ls.percents.toString()});
        }

    }

}
