package com.md.williamriesen.hawkeyeharvest.foodbank

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.md.williamriesen.hawkeyeharvest.*
import java.util.*


class FoodBank {

    fun makeDate(month: Int, day: Int, year: Int): Date {
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
//        get() = true //for debugging
        get() {
            val today = getCurrentDateWithoutTime()
            val now = Date()
            return (
                    !isWeekend(today) &&
                            !holidaysList.contains(today) &&
                            now > openingTime &&
                            now < closingTime
                    )
        }

    val isTakingNextDayOrders: Boolean
        get() {
            val today = getCurrentDateWithoutTime()
            val calendar = Calendar.getInstance()
            calendar.time = today
            calendar.add(Calendar.DAY_OF_YEAR, +1)
            val tomorrow = calendar.time as Date
            return isOpenOn(tomorrow) && isBeforeFivePM()
        }
//        get() =  true


    fun isBeforeFivePM(): Boolean {
        val calendar = Calendar.getInstance()
        val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
        return hour24 < 17
//        return true
    }

    fun nextDayOpen(afterTomorrow: Boolean = false): Date {
        val today = getCurrentDateWithoutTime()
        val calendar = Calendar.getInstance()
        calendar.time = today
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        if (!isBeforeFivePM() || (afterTomorrow)) calendar.add(Calendar.DAY_OF_YEAR, 1)
        while (!isOpenOn(calendar.time)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.time
    }

    val monthTomorrow: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            return calendar.get(Calendar.MONTH)
        }

    fun nextDayTakingOrders(afterToday: Boolean = false): Date {
        val calendar = Calendar.getInstance()
        calendar.time = nextDayOpen(afterToday)
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    private fun isOpenOn(date: Date) = !isWeekend(date) && !holidaysList.contains(date)


    private val openingTime =
        createTimePoint(getCurrentDateWithoutTime(), openingHour24, openingMinute)
    private val closingTime =
        createTimePoint(getCurrentDateWithoutTime(), closingHour24, closingMinute)

    fun sendCategoriesListToFireStore() {
        val categoriesListing = CategoriesListing()
        categoriesListing.categoriesListingName = "myCategories"
        categoriesListing.categories = listOf(
            Category(1, "Meat", 2, 0),
            Category(2, "Protein", 2, 0),
            Category(3, "Vegetables", 2, 0),
            Category(4, "Fruits", 2, 0),
            Category(5, "Beans", 2, 0),
            Category(6, "Soups", 2, 0),
            Category(7, "Processed Tomato", 2, 0),
            Category(8, "Cereals", 1, 0),
            Category(9, "Sides", 1, 0),
            Category(10, "Pasta", 2, 0),
            Category(11, "Meal Helper", 2, 0),
            Category(12, "Extra", 2, 0),
            Category(13, "Nonedibles", 2, 0),
            Category(14, "Milk", 1, 0),
            Category(15, "Eggs", 0, 12),
            Category(16, "Pudding", 0, 3),
            Category(17, "Dairy (except milk)", 0, 1),
            Category(18, "Bread", 4, 0),
            Category(19, "Sweets", 2, 0),
            Category(20, "Fresh Produce", 0, 3),
            Category(21, "Bottom Bar", 0, 0)
        )
        val db = FirebaseFirestore.getInstance()

        db.collection("categories").document("categories").set(categoriesListing)
            .addOnSuccessListener {
                Log.d("TAG", "Categories updated.")
            }
    }

    fun sendObjectCatalogToFireStore() {
        val myObjectCatalog = ObjectCatalog()
        myObjectCatalog.catalogName = "myCatalog"
        myObjectCatalog.foodItemList = listOf(
            FoodItem(8, "Pork Chops 1lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            FoodItem(9, "Pork & Bacon Sausage 1.5lb", "Meat", 1, 100, 100, false, 1),
            FoodItem(10, "Ground Beef 2lb (Limit 2)", "Meat", 2, 2, 100, true, 1),
            FoodItem(11, "Sliced Cooked Ham 2lb (Limit 1)", "Meat", 1, 1, 100, false, 1),
            FoodItem(12, "Sliced Cotto Salami 2lb (Limit 1)", "Meat", 1, 1, 100, false, 1),
            FoodItem(13, "Whole Chicken 3lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            FoodItem(14, "Chicken Legs 5lb", "Meat", 1, 100, 100, false, 1),
            FoodItem(3, "Large Whole Ham 8lb (3pts, Limit 1)", "Meat", 3, 1, 100, true, 1),
            FoodItem(4, "Catfish Fillets 2lb (2pts, Limit 2)", "Meat", 2, 2, 100, true, 1),
            FoodItem(5, "Pork Loin 4lb (2pts)", "Meat", 2, 100, 100, true, 1),
            FoodItem(6, "Chicken Thighs 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(7, "Pork Shoulder Roast 6lb (2pts)", "Meat", 2, 100, 100, false, 1),
            FoodItem(2, "Cooked Chicken Fajita 5lb (4pts)", "Meat", 4, 100, 100, true, 1),
            FoodItem(1, "Cooked Chicken Fillets 5lb (4pts)", "Meat", 4, 100, 100, false, 1),
            FoodItem(89, "Cooked Chicken Patties 5lb (4pts, Limit 1)", "Meat", 4, 1, 100, false, 1),
            FoodItem(
                90,
                "Boneless Skinless Chicken Breasts (4pts, Limit 1)",
                "Meat",
                4,
                1,
                100,
                false,
                1
            ),
            FoodItem(109, "Chicken Leg Quarters 10lb (2 pt Limit 2)", "Meat", 2, 2, 100, true, 1),
            FoodItem(110, "Sliced Turkey Bologna 2lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            FoodItem(111, "Ground Pork 1lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(112, "Italian Hot Ham 5lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            FoodItem(113, "Chicken Fajita 5lb (4 pts, limit 1)", "Meat", 4, 1, 100, false, 1),
            FoodItem(91, "McDonald's Chicken Tenders 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(93, "Small Ham 3lb (2pts, Limit 2)", "Meat", 2, 2, 100, true, 1),
            FoodItem(94, "Bacon 1lb (Limit 2)", "Meat", 1, 2, 100, false, 1),
            FoodItem(143, "Sliced Chopped Ham 1lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(144, "Cooked Pork Patties 2lb (2pts, Limit 2)", "Meat", 2, 2, 100, true, 1),
            FoodItem(
                145,
                "Cooked Seasoned Pork Shredded 3lb (2pts, Limit 2)",
                "Meat",
                2,
                2,
                100,
                true,
                1
            ),
            FoodItem(150, "Pork Ribs 4lb (2 pts, Limit 2", "Meat", 2, 2, 100, true, 1),
            FoodItem(151, "Ground Turkey 2lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(152, "Chicken Drumsticks 3lb (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(153, "Medium Meat Pizza (Limit 2)", "Meat", 1, 2, 100, true, 1),
            FoodItem(154, "Pork Loin Roast 5.5lb (3 pts, Limit 1)", "Meat", 3, 1, 100, true, 1),


            FoodItem(15, "Spaghetti / Meatballs", "Protein", 1, 100, 100, true, 2),
            FoodItem(16, "Tuna", "Protein", 1, 100, 100, true, 2),
            FoodItem(92, "Salmon", "Protein", 1, 100, 100, true, 2),
            FoodItem(17, "Beef Ravioli", "Protein", 1, 100, 100, true, 2),
            FoodItem(18, "Chicken", "Protein", 1, 100, 100, true, 2),
            FoodItem(19, "Peanut Butter", "Protein", 1, 100, 100, true, 2),
            FoodItem(20, "Beef Stew", "Protein", 1, 100, 100, true, 2),
            FoodItem(105, "Canned Pork", "Protein", 1, 100, 100, true, 2),
            FoodItem(106, "Canned Beef", "Protein", 1, 100, 100, true, 2),

            FoodItem(21, "Carrots", "Vegetables", 1, 100, 100, true, 3),
            FoodItem(22, "Potatoes", "Vegetables", 1, 100, 100, true, 3),
            FoodItem(23, "Corn", "Vegetables", 1, 100, 100, true, 3),
            FoodItem(24, "Green Beans", "Vegetables", 1, 100, 100, true, 3),
            FoodItem(95, "Mixed Vegetables", "Vegetables", 1, 100, 100, true, 3),
            FoodItem(96, "Peas", "Vegetables", 1, 100, 100, true, 3),

            FoodItem(25, "Peaches", "Fruits", 1, 100, 100, true, 4),
            FoodItem(26, "Pears", "Fruits", 1, 100, 100, true, 4),
            FoodItem(27, "Cranberry Juice", "Fruits", 1, 100, 100, false, 4),
            FoodItem(28, "Dried Cranberries", "Fruits", 1, 100, 100, true, 4),
            FoodItem(29, "Raisins", "Fruits", 1, 100, 100, true, 4),
            FoodItem(30, "Apricots", "Fruits", 1, 100, 100, true, 4),
            FoodItem(31, "Mandarin Oranges", "Fruits", 1, 100, 100, true, 4),
            FoodItem(32, "Applesauce cups", "Fruits", 1, 100, 100, true, 4),
            FoodItem(33, "Pineapple", "Fruits", 1, 100, 100, true, 4),
            FoodItem(34, "Grape Juice", "Fruits", 1, 100, 100, false, 4),
            FoodItem(146, "Tropical Fruit", "Fruits", 1, 100, 100, true, 4),

            FoodItem(35, "Pinto", "Beans", 1, 100, 100, true, 5),
            FoodItem(36, "Refried", "Beans", 1, 100, 100, true, 5),
            FoodItem(37, "Chili", "Beans", 1, 100, 100, true, 5),
            FoodItem(38, "Pork & Beans", "Beans", 1, 100, 100, true, 5),
            FoodItem(39, "Baked", "Beans", 1, 100, 100, false, 5),
            FoodItem(40, "Black Beans", "Beans", 1, 100, 100, true, 5),
            FoodItem(41, "Dried Lentils", "Beans", 1, 100, 100, true, 5),
            FoodItem(42, "Dried Red Beans", "Beans", 1, 100, 100, true, 5),
            FoodItem(43, "Dried Black Beans", "Beans", 1, 100, 100, true, 5),
            FoodItem(44, "Dried Pinto Beans", "Beans", 1, 100, 100, true, 5),
            FoodItem(97, "Red Beans ", "Beans", 1, 100, 100, true, 5),
            FoodItem(98, "Chickpeas", "Beans", 1, 100, 100, false, 5),

            FoodItem(45, "Chicken Broth", "Soups", 1, 100, 100, true, 6),
            FoodItem(46, "Vegetarian", "Soups", 1, 100, 100, true, 6),
            FoodItem(47, "Tomato", "Soups", 1, 100, 100, true, 6),
            FoodItem(48, "Cream of Chicken", "Soups", 1, 100, 100, true, 6),
            FoodItem(49, "Cream of Mushroom", "Soups", 1, 100, 100, true, 6),
            FoodItem(50, "Chicken Noodle", "Soups", 1, 100, 100, true, 6),
            FoodItem(51, "Ramen Noodle", "Soups", 1, 100, 100, false, 6),

            FoodItem(52, "Sloppy Joe Mix", "Processed Tomato", 1, 100, 100, true, 7),
            FoodItem(53, "Tomato Sauce", "Processed Tomato", 1, 100, 100, true, 7),
            FoodItem(54, "Diced Tomatoes", "Processed Tomato", 1, 100, 100, true, 7),
            FoodItem(55, "Pasta Sauce", "Processed Tomato", 1, 100, 100, true, 7),
            FoodItem(
                106,
                "Tomato Veg Juice 5 cans (Limit 2)",
                "Processed Tomato",
                1,
                2,
                100,
                false,
                7
            ),
            FoodItem(157, "Whole peeled tomatoes", "Processed Tomato", 1, 100, 100, true, 7),

            FoodItem(56, "Oatmeal", "Cereals", 1, 100, 100, true, 8),
            FoodItem(57, "Oatmeal Packets", "Cereals", 1, 100, 100, true, 8),
            FoodItem(58, "Toasted Oats", "Cereals", 1, 100, 100, false, 8),
            FoodItem(59, "Corn Flakes", "Cereals", 1, 100, 100, true, 8),
            FoodItem(60, "Crisp Rice", "Cereals", 1, 100, 100, false, 8),
            FoodItem(99, "Cheerios", "Cereals", 1, 100, 100, true, 8),
            FoodItem(107, "Corn Biscuits (Chex)", "Cereals", 1, 100, 100, true, 8),

            FoodItem(61, "Stuffing", "Sides", 1, 100, 100, true, 9),
            FoodItem(62, "Instant Potatoes", "Sides", 1, 100, 100, true, 9),
            FoodItem(63, "White Rice", "Sides", 1, 100, 100, true, 9),
            FoodItem(64, "Brown Rice", "Sides", 1, 100, 100, false, 9),

            FoodItem(65, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            FoodItem(66, "Rotini/Cellentani", "Pasta", 1, 100, 100, true, 10),
            FoodItem(100, "Shells", "Pasta", 1, 100, 100, true, 10),
            FoodItem(67, "Elbow Macaroni", "Pasta", 1, 100, 100, true, 10),
//            FoodItem(68, "Spaghetti", "Pasta", 1, 100, 100, true, 10),
            FoodItem(69, "Egg Noodles", "Pasta", 1, 100, 100, true, 10),
            FoodItem(101, "Tri-Color Penne", "Pasta", 1, 100, 100, true, 10),

            FoodItem(70, "Mac & Cheese", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(71, "Spaghetti Rings", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(72, "Lasagna Helper", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(73, "Cheeseburger Helper", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(74, "Beef Pasta Helper", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(75, "Alfredo Chicken Helper", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(76, "Strognaff Helper", "Meal Helper", 1, 100, 100, true, 11),
            FoodItem(77, "Tuna Helper", "Meal Helper", 1, 100, 100, true, 11),

            FoodItem(78, "Whole Wheat Flour", "Extra", 1, 100, 100, true, 12),
            FoodItem(79, "Dry Milk", "Extra", 1, 100, 100, true, 12),
            FoodItem(80, "Boxed Milk", "Extra", 1, 100, 100, true, 12),
            FoodItem(81, "Vegetable Oil (Limit 1)", "Extra", 1, 1, 100, true, 12),
            FoodItem(82, "Mini Pies (Cherry/Apple)", "Extra", 1, 100, 100, false, 12),
            FoodItem(83, "Cheese Balls", "Extra", 1, 100, 100, false, 12),
            FoodItem(102, "Baking Mix (Limit 2)", "Extra", 1, 2, 100, true, 12),
            FoodItem(103, "Dried Fruit & Nuts (Limit 2)", "Extra", 1, 2, 100, false, 12),
            FoodItem(104, "Ranch Dressing (Limit 1)", "Extra", 1, 1, 100, false, 12),
            FoodItem(108, "White Flour (Limit 1)", "Extra", 1, 1, 100, false, 12),
            FoodItem(114, "Fruit by the Foot (Limit 1)", "Extra", 1, 1, 100, false, 12),
            FoodItem(115, "Fruit Snacks (Limit 1)", "Extra", 1, 1, 100, true, 12),
            FoodItem(158, "Sugar Cookie Decorating Kit", "Extra", 1, 100, 100, false, 12),
            FoodItem(116, "Selzer Water (6 pack)", "Extra", 1, 100, 100, false, 12),
            FoodItem(148, "Marshmallows", "Extra", 1, 100, 100, true, 12),
            FoodItem(149, "Diet Pepsi (4 pack, limit 2)", "Extra", 1, 2, 100, true, 12),

            FoodItem(117, "Little Bites", "Extra", 1, 100, 100, false, 12),
            FoodItem(118, "Donuts", "Extra", 1, 100, 100, false, 12),
            FoodItem(119, "Pound Cakes", "Extra", 1, 100, 100, false, 12),
            FoodItem(120, "Pick Me Up", "Extra", 1, 100, 100, false, 12),
            FoodItem(121, "Marshmallow Cookies", "Extra", 1, 100, 100, false, 12),
            FoodItem(122, "Chocolate Cupcakes", "Extra", 1, 100, 100, false, 12),

            FoodItem(84, "Toothbrush", "Nonedibles", 1, 100, 100, true, 13),
            FoodItem(85, "Toothpaste", "Nonedibles", 1, 100, 100, true, 13),
            FoodItem(86, "Bar of Soap", "Nonedibles", 1, 100, 100, true, 13),
            FoodItem(87, "Toilet Paper", "Nonedibles", 1, 100, 100, true, 13),
            FoodItem(88, "Laundry Soap (Homemade- Limit 1)", "Nonedibles", 1, 1, 100, true, 13),

            FoodItem(123, "Half Gallon Milk 1%", "Milk", 1, 100, 100, true, 14),
            FoodItem(124, "Dozen Eggs", "Eggs", 1, 12, 100, true, 15),
            FoodItem(
                125,
                "Sugar Free Chocolate Swirl Pudding (Limit 2)",
                "Pudding",
                1,
                2,
                100,
                false,
                16
            ),

            FoodItem(126, "Yogurt (9-5oz cups)", "Dairy (except milk)", 1, 100, 100, false, 17),
            FoodItem(127, "American Cheese (32 oz)", "Dairy (except milk)", 1, 100, 100, true, 17),
            FoodItem(128, "Cream Cheese (8 oz)", "Dairy (except milk)", 1, 100, 100, true, 17),
            FoodItem(146, "Butter 16 oz", "Dairy (except milk)", 1, 100, 100, true, 17),
            FoodItem(
                147,
                "Shredded Cheddar Cheese 32 oz",
                "Dairy (except milk)",
                1,
                100,
                100,
                true,
                17
            ),
            FoodItem(
                155,
                "Shredded Mozzarella Cheese 32 oz",
                "Dairy (except milk)",
                1,
                100,
                100,
                true,
                17
            ),
            FoodItem(
                148,
                "Pumpkin Rolls (need baking)",
                "Dairy (except milk)",
                1,
                100,
                100,
                true,
                17
            ),
            FoodItem(156, "Pie Crusts", "Dairy (except milk)", 1, 100, 100, true, 17),

            FoodItem(129, "Sliced wheat bread", "Bread", 1, 100, 100, true, 18),
            FoodItem(130, "Sliced white bread", "Bread", 1, 100, 100, true, 18),
            FoodItem(131, "Whole grain bread", "Bread", 1, 100, 100, true, 18),
            FoodItem(132, "Hot dog buns", "Bread", 1, 100, 100, true, 18),
            FoodItem(133, "Hamburger buns", "Bread", 1, 100, 100, true, 18),
            FoodItem(134, "English muffins", "Bread", 1, 100, 100, true, 18),
            FoodItem(135, "Bagels", "Bread", 1, 100, 100, true, 18),

            FoodItem(136, "Donuts", "Sweets", 1, 100, 100, true, 19),
            FoodItem(137, "Muffins", "Sweets", 1, 100, 100, true, 19),
            FoodItem(138, "Cinnamon rolls", "Sweets", 1, 100, 100, true, 19),
            FoodItem(139, "Donut holes", "Sweets", 1, 100, 100, true, 19),

            FoodItem(140, "Green vegetable mix", "Fresh Produce", 1, 100, 100, true, 20),
            FoodItem(141, "Red vegetable mix", "Fresh Produce", 1, 100, 100, true, 20),
            FoodItem(142, "Yellow vegetable mix", "Fresh Produce", 1, 100, 100, true, 20)
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("catalogs").document("objectCatalog").set(myObjectCatalog)
            .addOnSuccessListener {
                Log.d("TAG", "Catalog updated--.")
            }
    }


}