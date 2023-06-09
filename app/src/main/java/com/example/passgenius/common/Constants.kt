package com.example.passgenius.common

import com.example.passgenius.R
import com.example.passgenius.domain.models.CategoryItem

object Constants {

    const val API_KEY_LOGO = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiZmMyMGZmOGRlMDk5YTNmMTA2N2FkMGYwMjA2NjBlOTI0YWIzMDNmNzg4ZDZmNzEyMjRiNzQ1NmJkMDJlYzEwZjQzNzI1MzBkMTg4M2Y1MGYiLCJpYXQiOjE2ODIxNTc0NTQuNjkxMTgsIm5iZiI6MTY4MjE1NzQ1NC42OTExODEsImV4cCI6MTcxMzc3OTg1NC42ODYzNzgsInN1YiI6IjEwNTI1Iiwic2NvcGVzIjpbXX0.hCvdh6CFUFXK8LuAeu025dJIrhLx7fOAM6DuDNvS72bu2HcC8CAfWO2ZOLzBSvQSSaYjc5SCikT2AYOpG3wQg8jcBYPrdnt0YdP97M7Cg3jYS6bTYLDe7McI9_xd9pjdDKZ8Mu14TjZVEQtcLcIEiHXss1qkTR4dsEQbCcq05L89R-L6avzEiBc7nYfOqL7CWA2ST-P0TLhL7VD0m84XoAoUve9l6EeJZO1n_EJwLLVj4HCFR414gDDj6kTS7DM5L5zqA22vuT7MAfICzB9N7DeCOMy5bXz-1TiqBvoNFBKudWMhTGuhvoa4M-cEACK-tmXsfj3lbkcxyIF0ArZ4_K5vw8ohJMOY-e8E-JBmRzALAP6S8EbimADEJo4vtiKPaTgfU_niX9h2ic-n_q5olu5ebv2etynVsBq2umARQQfDTh6Sj0e_7-vvRnsFXU5Jqe3smX294uphCQcSVvUFLLJOksgEBw1YMBVyB9rfgCL7EaRv6OqBY7DweTrGZA1tpG-uQ0Ts38miq_z9LcUgUIdSdD7Z0euXJzlr1OKZ4Smhg5Npy4VEWZebdvO2XFlQ_YZDL4WNkbk3xEPyPwQVVK5t9QC9KGERQiVTVwvUIzPoOFb79GobQjz_IyUQG3dRtqwS8_lmFmCsJ3gp0eJEp6cUTvVnltgWddv2iICfQLs"
    const val BASE_URL_heroku = "https://pass-genius-api.herokuapp.com/"
    const val BASE_URL_localhost = "http://10.0.2.2:5000/"
    val TIME_LIST_APP_LOCK = arrayListOf(1,2,3,4,5,10,15,30,60,0)
    const val ITEM_PAGE_REQUEST_CODE = 0
    val categoriesList = listOf(
        CategoryItem("All", R.drawable.ic_outline_window_24, true),
        CategoryItem("Logins", R.drawable.ic_baseline_login_24, ),
        CategoryItem("Notes", R.drawable.icon_notes_black, ),
        CategoryItem("Identities", R.drawable.id_icon, ),
        CategoryItem("Payments", R.drawable.icon_businss_invest_money, ),
       )
}