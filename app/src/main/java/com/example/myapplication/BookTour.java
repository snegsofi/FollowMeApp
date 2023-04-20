package com.example.myapplication;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

// класс для работы с авторскими турами
public class BookTour implements Comparable<BookTour>{

    // поля, содержащие информацию об авторских турах
    private String id;
    private String photo;
    private String name;
    private String description;
    private int price;
    private String dates;
    private int duration;
    private String place;
    private int group;
    private int toursSold;
    private boolean isAvailable;
    private String organizer;
    private String direction;

    public BookTour(){}

    public BookTour(String id,String photo, String name, String description, int price, String dates, int duration, String place, int group, int toursSold, boolean isAvailable, String organizer, String direction) {
        this.id=id;
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dates = dates;
        this.duration = duration;
        this.place = place;
        this.group = group;
        this.toursSold = toursSold;
        this.isAvailable = isAvailable;
        this.organizer = organizer;
        this.direction=direction;
    }

    public BookTour(String photo, String name, String description, int price, String dates, int duration, String place, int group, int toursSold, boolean isAvailable, String organizer, String direction) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dates = dates;
        this.duration = duration;
        this.place = place;
        this.group = group;
        this.toursSold = toursSold;
        this.isAvailable = isAvailable;
        this.organizer = organizer;
        this.direction=direction;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getToursSold() {
        return toursSold;
    }

    public void setToursSold(int toursSold) {
        this.toursSold = toursSold;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    private Date dateTime;

    public Date getDateTime() throws ParseException {
        String[] parts1=getDates().split(("-"));
        String date=parts1[0];

        dateTime = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(date);

        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

    // метод, необходимый для сортировки туров по дате
    @Override
    public int compareTo(BookTour o) {
        try {
            return getDateTime().compareTo(o.getDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<BookTour> data(){
        List<BookTour> bookTours=new ArrayList<>();

        bookTours.add(new BookTour("p8.png","Знакомство с Карелией", "Хотите сменить обстановку и у вас всего два дня? Познакомьтесь с Карелией!",
                7590, "29.04.2023-30.04.2023", 2, "ПЕТРОЗАВОДСК, РОССИЯ", 50, 0, true, "Get In Russia", "Озера"));

        return bookTours;
    }

}


//bookTours.add(new BookTour("","Осетия и Ингушетия","Наш тур — это уникальная возможность посетить две республики за короткий срок и познакомиться с удивительным культурным и историческим наследием Осетии и Ингушетии.",
//        17760, "6-9 марта", 96, "ВЛАДИКАВКАЗ, РОССИЯ", 20, 0, true, "Baltson"));
//
//        bookTours.add(new BookTour("","Тайны древнего Иристона","Уникальная возможность познакомиться с двумя живописными республиками Северного Кавказа!",
//        20228, "9-13 марта", 120, "РОССИЯ, ГРУЗИЯ, ЮЖНАЯ ОСЕТИЯ, АЗИЯ", 20, 0, true, "Baltson"));
//
//        bookTours.add(new BookTour("","Тур выходного дня из Барнаула «Алтайский букет»", "Едем в Горный Алтай в самый яркий период — цветение огоньков.",
//        13800, "26-28 мая", 72, "БАРНАУЛ, РОССИЯ", 18, 0, true, "AltayEnergy"));
//
//        bookTours.add(new BookTour("","Тур по Чуйскому тракту до «алтайского Марса»", "Это 2 дневный тур для тех, у кого мало времени, а посмотреть хочется побольше!",
//        14900, "23-24 марта", 48, "РОССИЯ", 12, 0, true, "Наталья Замалдинова"));
//
//        bookTours.add(new BookTour("","Чемальские каникулы", "5 ярких дней, наполненных экскурсионной программой, с проживанием на уютной базе в с. Аскат. Чемальский район — идеальное место для отдыха, сочетающее в себе близкое расположение к популярным туристическим объектам с тишиной и уединённостью.",
//        23900, "1-5 июня", 120, "РОССИЯ", 6, 0, true, "AltayEnergy"));
//
//        bookTours.add(new BookTour("","Восхождение на Святой Нос с видом на Байкал", "Этот тур позволит вам насладиться Байкальским колоритом, познакомиться с удивительной природой озера, Забайкальского национального парка. Побывать на высокогорном плато крупнейшего на Байкале полуострова — Святой Нос (1740 м).",
//        8700, "9-11 июня", 42, "УЛАН-УДЭ, РОССИЯ", 15, 0, true, "Аяятревел Байкал"));
