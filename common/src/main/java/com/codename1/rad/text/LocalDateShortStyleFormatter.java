/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.rad.text;

import com.codename1.l10n.L10NManager;
import com.codename1.l10n.ParseException;
import java.util.Date;

/**
 * A date formatter for formatting dates in the current locale in short format.
 * 
 * NOTE: Does not support parsing.
 * @author shannah
 */
public class LocalDateShortStyleFormatter implements DateFormatter {

    @Override
    public String format(Date date) {
        return L10NManager.getInstance().formatDateShortStyle(date);
    }

    @Override
    public Date parse(String date) throws ParseException {
        throw new ParseException("Failed to parse date.  This formatter doesn't support parsing.", 0);
    }

    @Override
    public boolean supportsParse() {
        return false;
    }
    
}
