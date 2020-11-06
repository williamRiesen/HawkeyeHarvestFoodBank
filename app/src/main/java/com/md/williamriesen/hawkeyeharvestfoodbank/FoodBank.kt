package com.md.williamriesen.hawkeyeharvestfoodbank

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class FoodBank {

    private fun makeDate(month: Int, day: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun createTimePoint(date: Date, hour24: Int, minute: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, hour24)
        calendar.set(Calendar.MINUTE, minute)
        return calendar.time
    }

    private val holidaysList = listOf<Date>(
        makeDate(Calendar.NOVEMBER, 26, 2020),
        makeDate(Calendar.NOVEMBER, 27, 2020),
        makeDate(Calendar.DECEMBER, 24, 2020),
        makeDate(Calendar.DECEMBER, 25, 2020),
        makeDate(Calendar.DECEMBER, 31, 2020),
        makeDate(Calendar.JANUARY, 1, 2021)
    )

    fun getCurrentDateWithoutTime(): Date {
        val calendar = Calendar.getInstance()
        val thisMonth = calendar.get(Calendar.MONTH)
        val thisDay = calendar.get(Calendar.DAY_OF_MONTH)
        val thisYear = calendar.get(Calendar.YEAR)
//        return makeDate(Calendar.OCTOBER,25,2020)  // this line for debugging: set today to be what you want.
        return makeDate(thisMonth, thisDay, thisYear)
    }

    fun getFakeNowForDebugging(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = getCurrentDateWithoutTime()
        calendar.set(Calendar.HOUR_OF_DAY, 14)
        calendar.set(Calendar.MINUTE, 15)
        return Date(calendar.timeInMillis)
    }

    private fun isWeekend(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val isSaturday = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        val isSunday = (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        return isSaturday || isSunday
    }

    val isOpen: Boolean
    get()=true //for debugging
//        get() {
//            val today = getCurrentDateWithoutTime()
//            val now = Date()
//            return (
//                    !isWeekend(today) &&
//                            !holidaysList.contains(today) &&
//                            now > openingTime &&
//                            now < closingTime
//                    )
//        }

    val isTakingNextDayOrders: Boolean
    get() {
        val today = getCurrentDateWithoutTime()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, +1)
        val tomorrow = calendar.time as Date
        return isOpenOn(tomorrow)
    }

    val nextDayOpen: Date
    get() {
        val today = getCurrentDateWithoutTime()
        val calendar = Calendar.getInstance()
        calendar.time = today
        while (!isOpenOn(calendar.time)){
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.time
    }

    val nextDayTakingOrders: Date
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = nextDayOpen
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    private fun isOpenOn(date: Date) = !isWeekend(date) && !holidaysList.contains(date)


    private val openingTime = createTimePoint(getCurrentDateWithoutTime(), openingHour24, openingMinute)
    private val closingTime = createTimePoint(getCurrentDateWithoutTime(), closingHour24, closingMinute)

    fun sendCategoriesListToFireStore() {
        val categoriesListing = CategoriesListing()
        categoriesListing.categoriesListingName = "myCategories"
        categoriesListing.categories = listOf(
            Category(1, "Meat", 2, 0),
            Category(2, "Protein", 1, 2),
            Category(3, "Vegetables", 2, 0),
            Category(4, "Fruits", 2, 0),
            Category(5, "Beans", 2, 0),
            Category(6, "Soups", 2, 0),
            Category(7, "Processed Tomato", 2, 1),
            Category(8, "Cereals", 1, 0),
            Category(9, "Sides", 1, 0),
            Category(10, "Pasta", 1, 0),
            Category(11, "Meal Helper", 1, 0),
            Category(12, "Extra", 1, 0),
            Category(13, "Nonedibles", 1, 2),
            Category(14, "Bottom Bar", 0, 0)
        )
        val db = FirebaseFirestore.getInstance()

        db.collection("categories").document("categories").set(categoriesListing)
    }

    fun sendObjectCatalogToFireStore() {
        val myObjectCatalog = ObjectCatalog()
        myObjectCatalog.catalogName = "myCatalog"
        myObjectCatalog.itemList = listOf(
            Item(8, "Pork Chops 1lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            Item(9, "Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100, false, 1),
            Item(10, "Ground Beef 1lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            Item(11, "Sliced Cooked Ham 2lb (Limit 1)", "Meat", 1, 1, 100, false, 1),
            Item(12, "Sliced Cotto Salami 2lb (Limit 1)", "Meat", 1, 1, 100, false, 1),
            Item(13, "Whole Chicken 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            Item(14, "Chicken Legs 5lb", "Meat", 1, 100, 100, false, 1),
            Item(3, "Large Ham 10lb (3pts, Limit 1)", "Meat", 3, 1, 100, true, 1),
            Item(4, "Catfish Fillets 2lb (2pts, Limit 2)", "Meat", 2, 2, 100, true, 1),
            Item(5, "Pork Loin 4lb (2pts)", "Meat", 2, 100, 100, false, 1),
            Item(6, "Chicken Thighs 5lb (2pts, Limit 2)", "Meat", 2, 2, 100, false, 1),
            Item(7, "Pork Shoulder Roast 6lb (2pts)", "Meat", 2, 100, 100, false, 1),
            Item(2, "Cooked Chicken Fajita 5lb (4pts)", "Meat", 4, 100, 100, false, 1),
            Item(1, "Cooked Chicken Fillets 5lb (4pts)", "Meat", 4, 100, 100, false, 1),
            Item(89, "Cooked Chicken Patties 5lb (4pts, Limit 1)", "Meat", 4, 1, 100, true, 1),
            Item(105,"Canned Pork","Meat",1,100,100,true,1),
            Item(
                90,
                "Boneless Skinless Chicken Breasts (4pts, Limit 1)",
                "Meat",
                4,
                1,
                100,
                true,
                1
            ),
            Item(106,"Canned Beef","Meat",1,100,100,true,1),
            Item(109, "Chicken Leg Quarters 10# (Limit 1","Meat",4,1,100,true,1),

            Item(91, "McDonald's Chicken Tenders 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            Item(93, "Small Ham 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            Item(94, "Bacon 1lb (Limit 2)", "Meat", 1, 2, 100, true, 1),

            Item(15, "Spaghetti / Meatballs", "Protein", 1, 100, 100, true, 2),
            Item(16, "Tuna", "Protein", 1, 100, 100, true, 2),
            Item(92, "Salmon", "Protein", 1, 100, 100, true, 2),
            Item(17, "Beef Ravioli", "Protein", 1, 100, 100, true, 2),
            Item(18, "Chicken", "Protein", 1, 100, 100, true, 2),
            Item(19, "Peanut Butter", "Protein", 1, 100, 100, true, 2),
            Item(20, "Beef Stew", "Protein", 1, 100, 100, true, 2),

            Item(21, "Carrots", "Vegetables", 1, 100, 100, true, 3),
            Item(22, "Potatoes", "Vegetables", 1, 100, 100, true, 3),
            Item(23, "Corn", "Vegetables", 1, 100, 100, true, 3),
            Item(24, "Green Beans", "Vegetables", 1, 100, 100, true, 3),
            Item(95, "Mixed Vegetables", "Vegetables", 1, 100, 100, true, 3),
            Item(96, "Peas", "Vegetables", 1, 100, 100, true, 3),

            Item(25, "Peaches", "Fruits", 1, 100, 100, true, 4),
            Item(26, "Pears", "Fruits", 1, 100, 100, true, 4),
            Item(27, "Cranberry Juice", "Fruits", 1, 100, 100, true, 4),
            Item(28, "Dried Cranberries", "Fruits", 1, 100, 100, true, 4),
            Item(29, "Raisins", "Fruits", 1, 100, 100, true, 4),
            Item(30, "Apricots", "Fruits", 1, 100, 100, true, 4),
            Item(31, "Mandarin Oranges", "Fruits", 1, 100, 100, true, 4),
            Item(32, "Applesauce", "Fruits", 1, 100, 100, true, 4),
            Item(33, "Pineapple", "Fruits", 1, 100, 100, true, 4),
            Item(34, "Grape Juice", "Fruits", 1, 100, 100, true, 4),

            Item(35, "Pinto", "Beans", 1, 100, 100, true, 5),
            Item(36, "Refried", "Beans", 1, 100, 100, true, 5),
            Item(37, "Chili", "Beans", 1, 100, 100, true, 5),
            Item(38, "Pork & Beans", "Beans", 1, 100, 100, true, 5),
            Item(39, "Kidney", "Beans", 1, 100, 100, false, 5),
            Item(40, "Black Beans", "Beans", 1, 100, 100, true, 5),
            Item(41, "Dried Lentils", "Beans", 1, 100, 100, true, 5),
            Item(42, "Dried Red Beans", "Beans", 1, 100, 100, true, 5),
            Item(43, "Dried Black Beans", "Beans", 1, 100, 100, true, 5),
            Item(44, "Dried Pinto Beans", "Beans", 1, 100, 100, true, 5),
            Item(97, "Red Beans ", "Beans", 1, 100, 100, true, 5),
            Item(98, "Chickpeas", "Beans", 1, 100, 100, true, 5),

            Item(45, "Chicken Broth", "Soups", 1, 100, 100, true, 6),
            Item(46, "Vegetarian", "Soups", 1, 100, 100, true, 6),
            Item(47, "Tomato", "Soups", 1, 100, 100, true, 6),
            Item(48, "Cream of Chicken", "Soups", 1, 100, 100, true, 6),
            Item(49, "Cream of Mushroom", "Soups", 1, 100, 100, true, 6),
            Item(50, "Chicken Noodle", "Soups", 1, 100, 100, true, 6),
            Item(51, "Chicken/Dumpling", "Soups", 1, 100, 100, false, 6),

            Item(52, "Sloppy Joe Mix", "Processed Tomato", 1, 100, 100, true, 7),
            Item(53, "Tomato Sauce", "Processed Tomato", 1, 100, 100, true, 7),
            Item(54, "Diced Tomatoes", "Processed Tomato", 1, 100, 100, true, 7),
            Item(55, "Pasta Sauce", "Processed Tomato", 1, 100, 100, true, 7),
            Item(106,"Tomato Veg Juice 5 cans (Limit-2)","Processed Tomato",1,2,100,true,7),

            Item(56, "Oatmeal", "Cereals", 1, 100, 100, true, 8),
            Item(57, "Oatmeal Packets", "Cereals", 1, 100, 100, true, 8),
            Item(58, "Toasted Oats", "Cereals", 1, 100, 100, false, 8),
            Item(59, "Corn Flakes", "Cereals", 1, 100, 100, true, 8),
            Item(60, "Crisp Rice", "Cereals", 1, 100, 100, true, 8),
            Item(99, "Cheerios", "Cereals", 1, 100, 100, true, 8),
            Item(107, "Corn Biscuits (CHex)", "Cereals",1,100,100,true,8),

            Item(61, "Stuffing", "Sides", 1, 100, 100, true, 9),
            Item(62, "Instant Potatoes", "Sides", 1, 100, 100, true, 9),
            Item(63, "White Rice", "Sides", 1, 100, 100, true, 9),
            Item(64, "Brown Rice", "Sides", 1, 100, 100, false, 9),

            Item(65, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            Item(66, "Rotini/Cellentani", "Pasta", 1, 100, 100, false, 10),
            Item(100, "Shells", "Pasta", 1, 100, 100, true, 10),
            Item(67, "Elbow Macaroni", "Pasta", 1, 100, 100, true, 10),
//            Item(68, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            Item(69, "Egg Noodles", "Pasta", 1, 100, 100, true, 10),
            Item(101, "Tri-Color Penne", "Pasta", 1, 100, 100, true, 10),

            Item(70, "Mac & Cheese", "Meal Helper", 1, 100, 100, true, 11),
            Item(71, "Spaghetti Rings", "Meal Helper", 1, 100, 100, true, 11),
            Item(72, "Lasagna Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(73, "Cheeseburger Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(74, "Beef Pasta Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(75, "Alfredo Chicken Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(76, "Strognaff Helper", "Meal Helper", 1, 100, 100, true, 11),
            Item(77, "Tuna Helper", "Meal Helper", 1, 100, 100, true, 11),

            Item(78, "Whole Wheat Flour", "Extra", 1, 100, 100, true, 12),
            Item(79, "Dry Milk", "Extra", 1, 100, 100, true, 12),
            Item(80, "Boxed Milk", "Extra", 1, 100, 100, true, 12),
            Item(81, "Vegetable Oil (Limit 2)", "Extra", 1, 2, 100, true, 12),
            Item(82, "Mini Pies (Cherry/Apple)", "Extra", 1, 100, 100, false, 12),
            Item(83, "Cheese Balls", "Extra", 1, 100, 100, false, 12),
            Item(102, "Baking Mix (Limit 2)", "Extra", 1, 2, 100, true, 12),
            Item(103, "Dried Fruit & Nuts (Limit 2)", "Extra", 1, 2, 100, true, 12),
            Item(104, "Ranch Dressing (Limit 1)", "Extra", 1, 1, 100, true, 12),
            Item(108,"White Flour (Limit 1)","Extra",1,1,100,true,12),

            Item(84, "Toothbrush", "Nonedibles", 1, 100, 100, true, 13),
            Item(85, "Toothpaste", "Nonedibles", 1, 100, 100, true, 13),
            Item(86, "Bar of Soap", "Nonedibles", 1, 100, 100, true, 13),
            Item(87, "Toilet Paper", "Nonedibles", 1, 100, 100, true, 13),
            Item(88, "Laundry Soap (Homemade- Limit 1)", "Nonedibles", 1, 1, 100, true, 13)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
    }



}