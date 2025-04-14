package com.example.sophiaapp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.sophiaapp.presentation.screens.about.AboutDevelopersScreen
import com.example.sophiaapp.presentation.screens.about.AboutProjectScreen
import com.example.sophiaapp.presentation.screens.home.HomeScreen
import com.example.sophiaapp.presentation.screens.library.LibraryScreen
import com.example.sophiaapp.presentation.screens.profile.ProfileScreen
import com.example.sophiaapp.presentation.screens.welcome.WelcomeScreen
import com.example.sophiaapp.presentation.screens.auth.SignUpScreen
import com.example.sophiaapp.presentation.screens.auth.SignInScreen
import com.example.sophiaapp.presentation.screens.legal.TermsOfUseScreen
import com.example.sophiaapp.presentation.screens.legal.PrivacyPolicyScreen
import com.example.sophiaapp.presentation.screens.course.CourseDetailScreen
import com.example.sophiaapp.presentation.screens.course.CourseTheoryScreen
import com.example.sophiaapp.presentation.screens.course.CoursePracticeScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.sophiaapp.presentation.components.games.MatchingGameScreen
import com.example.sophiaapp.presentation.components.games.FillInBlankGameScreen
import com.example.sophiaapp.presentation.components.games.MultipleChoiceGameScreen
import com.example.sophiaapp.domain.models.MatchingGame

@Composable
fun SetupNavGraph(
    navController:NavHostController,
    paddingValues:PaddingValues=PaddingValues()
){
    NavHost(
        navController=navController,
        startDestination=Screen.Welcome.route
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onContinueClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                onRegisterClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                },
                onSignInClick = {
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }

        composable(route = Screen.SignIn.route) {
            SignInScreen(
                onSignInClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }


        composable(route = Screen.Home.route) {
            HomeScreen(
                paddingValues=paddingValues,
                navController = navController
            )
        }
        composable(route = Screen.Library.route) {
            LibraryScreen(
                paddingValues=paddingValues,
                navController=navController
            )
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                paddingValues=paddingValues
                )
        }
        composable(route = Screen.AboutDevelopers.route) {
            AboutDevelopersScreen(navController = navController)

        }
        composable(route = Screen.AboutProject.route) {
            AboutProjectScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
        composable(route=Screen.PrivacyPolicy.route){
            PrivacyPolicyScreen(navController=navController)
        }
        composable(route=Screen.TermsOfUse.route){
            TermsOfUseScreen(navController=navController)
        }
        composable(
            route=Screen.CourseDetail.route,
            arguments=listOf(navArgument("courseId"){type=NavType.StringType})
            ){backStackEntry ->
            val courseId=backStackEntry.arguments?.getString("courseId")?:""
            CourseDetailScreen(
                courseId=courseId,
                navController=navController
            )
        }
        composable(
            route=Screen.CourseTheory.route,
            arguments=listOf(navArgument("courseId"){type=NavType.StringType})
        ){backStackEntry ->
            val courseId=backStackEntry.arguments?.getString("courseId")?:""
            CourseTheoryScreen(
                courseId=courseId,
                navController=navController
            )
        }
        composable(
            route=Screen.CoursePractice.route,
            arguments=listOf(navArgument("courseId"){type=NavType.StringType})
        ){backStackEntry ->
            val courseId=backStackEntry.arguments?.getString("courseId")?:""
            CoursePracticeScreen(
                courseId=courseId,
                navController=navController
            )
        }
        composable(
            route = Screen.MatchingGame.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("courseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("id") ?: "connections_types"
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            
            MatchingGameScreen(
                gameId = gameId,
                navController = navController,
                courseId = courseId,
                onGameCompleted = { score, maxScore ->
                    // Обработка завершения игры
                }
            )
        }
        
        composable(
            route = Screen.FillInBlankGame.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("courseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("id") ?: "ancient_philosophy"
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            
            FillInBlankGameScreen(
                gameId = gameId,
                navController = navController,
                courseId = courseId,
                onGameCompleted = { score, maxScore ->
                    // Обработка завершения игры
                }
            )
        }
        
        composable(
            route = Screen.MultipleChoiceGame.route,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("courseId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("id") ?: "science_laws"
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            
            MultipleChoiceGameScreen(
                gameId = gameId,
                navController = navController,
                courseId = courseId,
                onGameCompleted = { score, maxScore ->
                    // Обработка завершения игры
                }
            )
        }
    }
}