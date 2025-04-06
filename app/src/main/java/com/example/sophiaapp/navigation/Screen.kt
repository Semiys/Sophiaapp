package com.example.sophiaapp.navigation

import com.example.sophiaapp.utils.localization.AppStrings

sealed class Screen(
    val route: String,
    val showBottomBar: Boolean=true
    ){
    object Welcome: Screen(route="welcome_screen",showBottomBar=false)
    object SignUp: Screen(route="signup_screen",showBottomBar=false)
    object SignIn:Screen(route="signin_screen",showBottomBar=false)

    object Home:Screen(route="home_screen")
    object Library: Screen(route="library_screen")
    object Profile: Screen(route="profile_screen")
    object AboutDevelopers:Screen(route="about_developers_screen",showBottomBar=false)
    object AboutProject : Screen(route = "about_project_screen",showBottomBar=false)
    object PrivacyPolicy:Screen(route="privacy_policy_screen",showBottomBar=false)
    object TermsOfUse:Screen(route="terms_of_use_screen",showBottomBar=false)
    object CourseDetail:Screen(route="course_detail_screen/{courseId}",showBottomBar=false){
        fun createRoute(courseId:String):String="course_detail_screen/$courseId"
    }
    object CourseTheory:Screen(route="course_theory_screen/{courseId}",showBottomBar=false){
        fun createRoute(courseId:String):String="course_theory_screen/$courseId"
    }
    object CoursePractice:Screen(route="course_practice_screen/{courseId}",showBottomBar=false){
        fun createRoute(courseId:String):String="course_practice_screen/$courseId"
    }
    object MatchingGame:Screen(route="matching_game_screen",showBottomBar=false){
        fun createRoute(gameId:String):String="matching_game_screen/$gameId"
    }
    object FillInBlankGame:Screen(route="fill_in_blank_game/{id}/{courseId}",showBottomBar=false){
        fun createRoute(id:String, courseId:String):String="fill_in_blank_game/$id/$courseId"
    }
    object MultipleChoiceGame:Screen(route="multiple_choice_game/{id}/{courseId}",showBottomBar=false){
        fun createRoute(id:String, courseId:String):String="multiple_choice_game/$id/$courseId"
    }

    val title:String
        get()=when(this){
            is Welcome -> AppStrings.Welcome.SCREEN_TITLE
            is SignUp -> AppStrings.Auth.SIGNUP_TITLE
            is SignIn -> AppStrings.Auth.SIGNIN_TITLE
            is Home ->AppStrings.NavigationBar.HOME
            is Library -> AppStrings.NavigationBar.LIBRARY
            is Profile -> AppStrings.NavigationBar.PROFILE
            is AboutDevelopers -> AppStrings.About.SCREEN_TITLE
            is AboutProject -> AppStrings.About.PROJECT_TITLE
            is PrivacyPolicy -> AppStrings.Project.PRIVACY_POLICY
            is TermsOfUse -> AppStrings.Project.TERMS_OF_USE
            else -> ""
        }

}