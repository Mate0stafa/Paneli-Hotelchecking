package com.example.paneli.Services;


import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DateService {

    public DateService() {
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public Integer calculateDayLength(String dateFrom, String dateTo) throws ParseException {
        Date dateFromDB = convertToDate(dateFrom);
        Date dateToDB = convertToDate(dateTo);

        LocalDate localDateFrom = convertToLocalDate(dateFromDB);
        LocalDate localDateTo = convertToLocalDate(dateToDB);

        Integer dayLength = 0;

        for (LocalDate date = localDateFrom; date.isBefore(localDateTo); date = date.plusDays(1)) {
            dayLength++;
        }

        return dayLength;
    }


    public Integer getDateBetweenDates(Date dateFromDB, Date dateToDB){
        LocalDate localDateFrom = convertToLocalDate(dateFromDB);
        LocalDate localDateTo = convertToLocalDate(dateToDB);

        Integer dayLength = 0;

        for (LocalDate date = localDateFrom; date.isBefore(localDateTo); date = date.plusDays(1)) {
            dayLength++;
        }

        return dayLength;
    }


    public Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public LocalDate convertToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Boolean isTodayDate(Date date){
        Date today = new Date();
        if (date.getYear()==today.getYear()&&date.getMonth()==today.getMonth()&&date.getDay()==today.getDay()){
            return true;
        }else return false;
    }


    public Date formatDate(String date) throws ParseException {
        System.out.println(date);
        date = date.replaceAll("/", "-")+" "+"00:00:00";
        java.util.Date dateFormated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        return dateFormated;
    }

    public Date formatDate1(String date) throws ParseException {
        return new SimpleDateFormat("MM/dd/yyyy").parse(date);
    }

    public String stringifyDate(LocalDate localDate){
        System.out.println("viti: "+localDate.getYear());
        return "04/25/2022";
    }

    public Date convertToDate(String date) throws ParseException {
        String sDate1= date + " 00:00:00";
        Date date1=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(sDate1);
        return date1;
    }

    public String getTodayDate(){

        Date tod = new Date();

        LocalDate localDate = convertToLocalDate(tod);
//        Date date = convertToDate(localDate);
        String dataSot = localDate.toString();
        String muaji = dataSot.substring(5, 7);
        String data = dataSot.substring(8, 10);
        String viti = dataSot.substring(0, 4);
        String daaata = muaji+"/"+data+"/"+viti;
        return daaata;
    }

    //data + 1 muaj - 1 dite
    public String getDateOneMonthFromNow() {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1).minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return oneMonthLater.format(formatter);
    }



    public String convertToCorrectFormat(String dataa){
        String muaji = dataa.substring(0,2);
        String data = dataa.substring(3,5);
        String viti = dataa.substring(6,10);
        String daaata = viti+"-"+muaji+"-"+data;
        return daaata;
    }

    public List<Date> findDatesBetween(Date startDate, Date endDate) {
        // Create a Calendar instance for the start date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        List<Date> dateList = new ArrayList<>();

        // Loop to generate dates between start and end
        while (calendar.getTime().before(endDate)) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dateList;
    }

    public Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.parse(dateString);
    }

    public Date get2DaysFromNow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        return calendar.getTime();
    }

    public long daysBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long days = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return days;
    }

    public Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public Date todayDateWithoutTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public  Date getTomorrow(){
        LocalDate today = LocalDate.now().plusDays(1);
        return Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
