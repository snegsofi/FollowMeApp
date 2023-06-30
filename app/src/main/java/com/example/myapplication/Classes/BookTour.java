package com.example.myapplication.Classes;

import java.text.Bidi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String dateStart;
    private String dateEnd;

    public BookTour(){}

    public BookTour(String id, String photo, String name, String description, int price, int duration, String place, int group, int toursSold, boolean isAvailable, String organizer, String direction, String dateStart, String dateEnd) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.place = place;
        this.group = group;
        this.toursSold = toursSold;
        this.isAvailable = isAvailable;
        this.organizer = organizer;
        this.direction = direction;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getDates() {
        return dates;
    }

    public int getDuration() {
        return duration;
    }

    public String getPlace() {
        return place;
    }

    public int getGroup() {
        return group;
    }

    public int getToursSold() {
        return toursSold;
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

    public String getId() {
        return id;
    }

    public String getDirection() {
        return direction;
    }

    private Date dateTime;

    // преобразование строки в дату
    public Date getDateTime() throws ParseException {
        dateTime = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(dateStart);
        return dateTime;
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



    public List<BookTour> load(){
        List<BookTour> tour=new ArrayList<>();
        tour.add(new BookTour("","","BIKE CAMP | Велотур для новичков и опытных путешественников","6 насыщенных дней в Калининграде. Велотрип по Прибалтике. Живописные маршруты, комфортные дистанции и новые велосипеды. Крутая развлекательная программа. Путешествия и вечеринки в весёлой компании. Ты классно проведешь время. Отдохнёшь активно. Проветришь голову на свежем воздухе. Прокатишься по самым живописным местам. Отдохнёшь в классной компании. Сделаешь крутые фотографии. Влюбишься в Калининград,",
                54900,6, "Калининград", 10,7,true,"Александр Ч","Велотур","23.06.2023","28.06.2023"));

        tour.add(new BookTour("","","Сахалин. Бухты охотского моря","Смешение культур, дикие и нетронутые ландшафты, огромные сопки... Сюда приезжают истинные авантюристы. Прокатимся на катере до заброшенного маяка Анива. Посетим Мыс Мраморный. Посетим Мыс Три Камня. Если повезет, понаблюдаем за тюленями. Устроим рыбалку на красную рыбу. Посетим Мыс Евстафия, одно из самых живописных мест на побережье. Посетим Мыс Птичий, где расположились множество созданных ветром и морскими волнами гротов. Сахалин входит в состав Сахалинской области — единственного субъекта России, расположенного целиком на островах. Отправляемся в путешествие, где искупаемся в Охотском море и увидим каменные арки, созданные ветрами. Будем гулять на тропинках среди скал и горных массивов. Устроим рыбалку на красную рыбу, устриц, морских ежей и гребешков. На Сахалине вы встретите целый мир удивительного и ощутите свободу дикой природы.",
                51700,9,"о.Сахалин",12,8,true,"Сергей Г","Поход","12.07.2023","20.07.2023"));
        tour.add(new BookTour("","","МЕГА ПОХОД по Приэльбрусью ✼ без рюкзаков","В этом треккинговом туре мы пройдём по самым популярным маршрутам Приэльбрусья, а также заглянем в живописные уголки редко посещаемые туристами. Каждое утро будем начинать с весёлой зарядки! Все походы доступны людям любого возраста, всегда рады семьям с детьми. Для детей хорошие скидки! Вечером играем в настольные игры, поем песни под гитару, отдыхаем. восхождение на Чегет, со спуском на канатной дороге потрясающие виды на Эльбрус и знаковые четырехтысячники Главного Кавказского хребта насыщенная треккинговая программа все походы без тяжелых рюкзаков пройдем самые красивые места Приэльбрусья однодневные треккинговые маршруты с лёгким рюкзаком съездим на термальные источники или попаримся в бане",
                44000,10,"Минеральные Воды", 10,7,true,"Михаил С","Поход","21.06.2023","30.06.2023"));

        return tour;

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
