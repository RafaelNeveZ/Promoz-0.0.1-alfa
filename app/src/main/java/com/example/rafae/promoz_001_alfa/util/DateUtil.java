package com.example.rafae.promoz_001_alfa.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vallux on 28/02/17.
 */

public class DateUtil {

    public static String DDMMYYYY = "dd/MM/yyyy";
    public static String YYYYMMDD_HHmmss = "yyyy-MM-dd HH:mm:ss";
    private static String SQLiteDateFormat = "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2}).*";

    private static Calendar calendar;

    public DateUtil() {
    }

    public static String SQLiteDateFormatToBrazilFormat(String date){

        Pattern padrao = Pattern.compile(SQLiteDateFormat);
        Matcher matcher = padrao.matcher(date);
        if(matcher.matches()){
            String BrazilFormatDate = matcher.group(3) + "/" + matcher.group(2) + "/" + matcher.group(1);
            return BrazilFormatDate;
        }

        return date;
    }

    /**
     * Método que retorna o dia do mês utilizando a classe Calendar
     * @return int Dia
     */
    public static int day(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Método que retorna o mês do ano utilizando a classe Calendar
     * @return int Mês
     */
    public static int month(){
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    /**
     * Método que retorna o ano utilizando a classe Calendar
     * @return int Ano
     */
    public static int year(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * Método que retorna uma data como String utilizando a classe Calendar
     * @param day
     * @param month
     * @param year
     * @param format
     * @return date as String
     */
    public static String returnDate(int day, int month, int year, String format){

        calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat(format);
        String formattedDate = df.format(date);
        //DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);

        return formattedDate;
    }

    /**
     * Método que retorna uma data utilizando a classe Calendar
     * @param day
     * @param month
     * @param year
     * @return Date
     */
    public static Date returnDate(int day, int month, int year){

        calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();

        return date;
    }

    /**
     * Método que retorna uma data como String utilizando a classe Calendar
     * @param date
     * @return date as String
     */
    public static String returnDate(Date date){

        SimpleDateFormat df = new SimpleDateFormat(DDMMYYYY);
        String formattedDate = df.format(date);

        return formattedDate;
    }
}
