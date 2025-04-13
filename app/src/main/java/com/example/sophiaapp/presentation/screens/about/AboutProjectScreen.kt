package com.example.sophiaapp.presentation.screens.about

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.sophiaapp.navigation.Screen
import com.example.sophiaapp.utils.localization.AppStrings
import com.example.sophiaapp.presentation.common.components.LongTextCard
import com.example.sophiaapp.R
import android.content.Intent
import android.net.Uri
import com.example.sophiaapp.presentation.common.components.BackButton
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.remember


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutProjectScreen(
    navController: NavHostController,
    paddingValues: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.About.PROJECT_TITLE) },
                navigationIcon = {
                    BackButton(onClick={navController.popBackStack()})
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SectionTitle(title = AppStrings.Project.ABOUT_APP)
                LongTextCard(
                    text=AppStrings.About.APP_DESCRIPTION_PROJECT,
                    backgroundRes=R.drawable.card_background_not,
                    verticalPadding=28
                )
                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle(title = AppStrings.Project.TECHNICAL_INFO)
                InfoCard {
                    ProjectInfoRow(
                        label = AppStrings.Project.VERSION,
                        value = "1.0.0"
                    )
                    ProjectInfoRow(
                        label = AppStrings.Project.PLATFORM,
                        value = "Android"
                    )
                    ProjectInfoRow(
                        label = AppStrings.Project.MIN_ANDROID,
                        value = "8.0 (API 26)"
                    )


                }
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(title = AppStrings.Project.LEGAL_INFO)
                ClickableInfoCard(
                    text = AppStrings.Project.PRIVACY_POLICY,
                    onClick = { navController.navigate(Screen.PrivacyPolicy.route) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ClickableInfoCard(
                    text = AppStrings.Project.TERMS_OF_USE,
                    onClick = {navController.navigate(Screen.TermsOfUse.route) }
                )
                Spacer(modifier = Modifier.height(16.dp))

                SectionTitle(title = AppStrings.Project.SOURCES)
                SourcesCard()

                SectionTitle(title = AppStrings.Project.CONTACTS)
                InfoCard {
                    ProjectInfoRow(
                        label = AppStrings.Project.EMAIL,
                        value = AppStrings.Project.EMAILMY
                    )

                }


                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    )
                    {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(AppStrings.Project.REPGITHUB)
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(56.dp)

                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.github),
                                contentDescription = "GitHub Repository",
                                modifier = Modifier.size(48.dp)
                            )


                        }
                        Spacer(modifier=Modifier.height(4.dp))
                        Text(
                            text=AppStrings.Project.REPPROJECT,
                            style=MaterialTheme.typography.bodyMedium,
                            textAlign=TextAlign.Center
                        )
                    }
                    Column(
                        horizontalAlignment=Alignment.CenterHorizontally
                    ){
                        FloatingActionButton(
                            onClick={
                                val intent=Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(AppStrings.Project.WEBSITEUL)
                                )
                                context.startActivity(intent)
                            },
                            modifier=Modifier
                                .size(56.dp)

                        ) {
                            Icon(
                                painter=painterResource(id=R.drawable.ulstulogoto),
                                contentDescription = "Сайт Университета",
                                modifier=Modifier.size(48.dp)
                            )

                        }
                        Spacer(modifier=Modifier.height(4.dp))
                        Text(
                            text=AppStrings.Project.WEBSITE,
                            style=MaterialTheme.typography.bodyMedium,
                            textAlign=TextAlign.Center

                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally

                    )
                    {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(AppStrings.Project.ULSTUSOPHIA)
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(56.dp)

                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.github),
                                contentDescription = "Преподаватель",
                                modifier = Modifier.size(48.dp)
                            )


                        }
                        Spacer(modifier=Modifier.height(4.dp))
                        Text(
                            text=AppStrings.Project.ULSTUTEACHER,
                            style=MaterialTheme.typography.bodyMedium,
                            textAlign=TextAlign.Center
                        )
                    }


                }
                Spacer(modifier=Modifier.height(16.dp))
            }

        }
    }
}


@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )

}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    textContent: String? = null,
    content: @Composable ColumnScope.() -> Unit = {}

) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)


    ) {
        Image(
            painter = painterResource(id = R.drawable.card_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds

        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (textContent != null) {
                Text(
                    text = textContent,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            } else {
                content()
            }
        }
    }

}


@Composable
private fun ProjectInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ClickableInfoCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.card_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }

}
@Composable
private fun SourcesCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.card_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LinkifiedText(
                text = AppStrings.Project.SOURCES_CONTENT,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun LinkifiedText(
    text: String,
    style: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uriHandler = remember { android.net.Uri.parse("") }
    val linkColor = MaterialTheme.colorScheme.primary

    val annotatedText = buildAnnotatedString {
        val urlRegex = "(https?://[^\\s]+)".toRegex()
        val matches = urlRegex.findAll(text)

        var lastIndex = 0
        for (match in matches) {
            // Добавляем обычный текст до ссылки
            append(text.substring(lastIndex, match.range.first))

            // Добавляем ссылку с аннотацией
            val url = match.value
            pushStringAnnotation(tag = "URL", annotation = url)
            withStyle(SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline)) {
                append(url)
            }
            pop()

            lastIndex = match.range.last + 1
        }

        // Добавляем оставшийся текст
        if (lastIndex < text.length) {
            append(text.substring(lastIndex))
        }
    }

    ClickableText(
        text = annotatedText,
        style = style,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(intent)
                }
        },
        modifier = modifier
    )
}
