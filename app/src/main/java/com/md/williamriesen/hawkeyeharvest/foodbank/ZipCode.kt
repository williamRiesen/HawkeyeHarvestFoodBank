package com.md.williamriesen.hawkeyeharvest.foodbank

class ZipCode(val zip: Int, val city: String, val county: String) {

}


class ZipCodeIndex() {

    val zipCodeList = listOf<ZipCode>(
        ZipCode(50006,"Alden", "Hardin"),
        ZipCode(50071, "Dows", "Wright"),
        ZipCode(50126, "Iowa Falls", "Hardin"),
        ZipCode(50401, "Mason City", "Cerro Gordo"),
        ZipCode(50402,"Mason City","Cerro Gordo"),
        ZipCode(50420,"Alexander","Franklin"),
        ZipCode(50421,"Belmond","Wright"),
        ZipCode(50423,"Britt","Hancock"),
        ZipCode(50424,"Buffalo Center","Winnebago"),
        ZipCode(50426,"Carpenter","Mitchell"),
        ZipCode(50427,"Chapin","Franklin"),
        ZipCode(50428,"Clear Lake","Cerro Gordo"),
        ZipCode(50430,"Corwith","Hancock"),
        ZipCode(50431,"Coulter","Franklin"),
        ZipCode(50432,"Crystal Lake","Hancock"),
        ZipCode(50433,"Dougherty","Cerro Gordo"),
        ZipCode(50434,"Fertile","Worth"),
        ZipCode(50435,"Floyd","Floyd"),
        ZipCode(50436,"Forest City","Winnebago"),
        ZipCode(50438,"Garner","Hancock"),
        ZipCode(50439,"Goodell","Hancock"),
        ZipCode(50440,"Grafton","Worth"),
        ZipCode(50441,"Hampton","Franklin"),
        ZipCode(50444,"Hanlontown","Worth"),
        ZipCode(50446,"Joice","Worth"),
        ZipCode(50447,"Kanawha","Hancock"),
        ZipCode(50448,"Kensett","Worth"),
        ZipCode(50449,"Klemme","Hancock"),
        ZipCode(50450,"Lake Mills","Winnebago"),
        ZipCode(50451,"Lakota","Kossuth"),
        ZipCode(50452,"Latimer","Franklin"),
        ZipCode(50453,"Leland","Winnebago"),
        ZipCode(50454,"Little Cedar","Mitchell"),
        ZipCode(50455,"McIntire","Mitchell"),
        ZipCode(50456,"Manly","Worth"),
        ZipCode(50457,"Meservey","Cerro Gordo"),
        ZipCode(50458,"Nora Springs","Floyd"),
        ZipCode(50459,"Northwood","Worth"),
        ZipCode(50460,"Orchard","Mitchell"),
        ZipCode(50461,"Osage","Mitchell"),
        ZipCode(50464,"Plymouth","Cerro Gordo"),
        ZipCode(50465,"Rake","Winnebago"),
        ZipCode(50466,"Riceville","Howard"),
        ZipCode(50467,"Rock Falls","Cerro Gordo"),
        ZipCode(50468,"Rockford","Floyd"),
        ZipCode(50469,"Rockwell","Cerro Gordo"),
        ZipCode(50470,"Rowan","Wright"),
        ZipCode(50471,"Rudd","Floyd"),
        ZipCode(50472,"St. Ansgar","Mitchell"),
        ZipCode(50473,"Scarville","Winnebago"),
        ZipCode(50475,"Sheffield","Franklin"),
        ZipCode(50476,"Stacyville","Mitchell"),
        ZipCode(50477,"Swaledale","Cerro Gordo"),
        ZipCode(50478,"Thompson","Winnebago"),
        ZipCode(50479,"Thornton","Cerro Gordo"),
        ZipCode(50480,"Titonka","Kossuth"),
        ZipCode(50481,"Toeterville","Mitchell"),
        ZipCode(50482,"Ventura","Cerro Gordo"),
        ZipCode(50483,"Wesley","Kossuth"),
        ZipCode(50484,"Woden","Hancock"),
        ZipCode(50511,"Algona","Kossuth"),
        ZipCode(50525, "Clarion", "Wright"),
        ZipCode(50542, "Goldfield", "Wright"),
        ZipCode(50603,"Alta Vista", "Chickasaw"),
        ZipCode(50517, "Bancroft","Kossuth"),
        ZipCode(50556, "Leyard","Kossuth"),
        ZipCode(50559, "Lone Rock","Kossuth"),
        ZipCode(50533, "Eagle Grove", "Wright"),
        ZipCode(50539, "Fenton", "Kossuth"),
        ZipCode(50590, "Swea City", "Kossuth"),
        ZipCode(50595, "Webster City", "Kossuth"),
        ZipCode(50611, "Bristow", "Hamilton"),
        ZipCode(50616, "Charles City", "Floyd"),
        ZipCode(50625,"Dumont", "Butler"),
        ZipCode(50627,"Eldora", "Hardin"),
        ZipCode(50633,"Geneva", "Franklin"),
        ZipCode(50645,"Iona", "Chickasaw"),
        ZipCode(50658,"Nashua", "Chickasaw"),
        ZipCode(50659,"New Hampton", "Chickasaw"),
        ZipCode(50653,"Marble Rock", "Floyd"),
        ZipCode(51556, "Madole", "Harrison"),
        ZipCode(52136,"Cresco", "Howard")
    )

    fun lookUpCity(zip: Int): String {
        val thisZip = zipCodeList.find {
            it.zip == zip
        }
        return thisZip?.city ?: ""
    }

    fun lookUpCounty(zip: Int): String {
        val thisZip = zipCodeList.find {
            it.zip == zip
        }
        return thisZip?.county ?: ""
    }
}