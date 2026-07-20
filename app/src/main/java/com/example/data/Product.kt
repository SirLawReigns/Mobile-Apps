package com.example.data

import com.example.R

data class ProductColor(
    val name: String,
    val hexValue: String
)

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val price: Double,
    val description: String,
    val sizes: List<String> = listOf("XS", "S", "M", "L", "XL"),
    val colors: List<ProductColor>,
    val defaultHexAccent: String,
    val imageRes: Int
)

object ProductData {
    val colors = listOf(
        ProductColor("Royal Gold", "#D4AF37"),
        ProductColor("Indigo Blue", "#1B2A4A"),
        ProductColor("Terracotta Clay", "#C36241"),
        ProductColor("Sunset Mustard", "#E3A857"),
        ProductColor("Onyx Black", "#1C1C1C"),
        ProductColor("Ivory White", "#F9F6F0"),
        ProductColor("Coral Red", "#E03C31")
    )

    val products = listOf(
        Product(
            id = "agbada_imperial",
            name = "Agbada Imperial Drape",
            category = "Outerwear",
            price = 150000.00,
            description = "An exquisite, grand three-piece traditional robe crafted from lightweight breathable wool and adorned with elaborate prestige metallic embroidery on the chest.",
            colors = listOf(colors[1], colors[0], colors[4]),
            defaultHexAccent = "#1B2A4A",
            imageRes = R.drawable.img_agbada_imperial
        ),
        Product(
            id = "asooke_jacket",
            name = "Aso-Oke Handwoven Jacket",
            category = "Outerwear",
            price = 85000.00,
            description = "A structured, modern tailored jacket crafted with heritage handwoven Yoruba Aso-Oke cotton textile. Extremely rich in texture and historical elegance.",
            colors = listOf(colors[2], colors[0], colors[4]),
            defaultHexAccent = "#C36241",
            imageRes = R.drawable.img_asooke_jacket
        ),
        Product(
            id = "ankara_skirt",
            name = "Ankara Pleated Midi Skirt",
            category = "Pants",
            price = 45000.00,
            description = "A versatile high-waisted pleated midi skirt celebrating premium African wax prints. Made from 100% fine cotton and designed with structured geometric pleating.",
            colors = listOf(colors[3], colors[2], colors[1]),
            defaultHexAccent = "#E3A857",
            imageRes = R.drawable.img_ankara_skirt
        ),
        Product(
            id = "kente_kaftan",
            name = "Kente Accent Kaftan",
            category = "Dresses",
            price = 95000.00,
            description = "A free-flowing, elegant modern kaftan crafted from organic cream cotton, featuring vibrant hand-stitched heritage Kente accent patterns on the sleeves.",
            colors = listOf(colors[5], colors[3], colors[0]),
            defaultHexAccent = "#F9F6F0",
            imageRes = R.drawable.img_kente_kaftan
        ),
        Product(
            id = "senator_suit",
            name = "Linen Senator Suit",
            category = "Shirts",
            price = 75000.00,
            description = "A contemporary two-piece set featuring a tailored long-sleeve Senator top with an off-center geometric embroidery detail, paired with matched tapered trousers.",
            colors = listOf(colors[4], colors[5], colors[2]),
            defaultHexAccent = "#1C1C1C",
            imageRes = R.drawable.img_senator_suit
        ),
        Product(
            id = "coral_cap",
            name = "Beaded Velvet Coral Cap",
            category = "Accessories",
            price = 30000.00,
            description = "A crown of culture. Premium velvet traditional cap meticulously hand-embroidered with authentic royal coral glass beads.",
            colors = listOf(colors[6], colors[4]),
            defaultHexAccent = "#E03C31",
            imageRes = R.drawable.img_coral_cap
        )
    )
}
