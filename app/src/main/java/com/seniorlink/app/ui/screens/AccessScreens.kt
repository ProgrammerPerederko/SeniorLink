package com.seniorlink.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seniorlink.app.R


// ============================================================
// CORES DO SENIORLINK
// ============================================================

private val Azul = Color(0xFF123B63)
private val AzulPrincipal = Color(0xFF06466B)
private val Verde = Color(0xFF159C8C)
private val Fundo = Color(0xFFF6FAFB)
private val Texto = Color(0xFF52677A)
private val Borda = Color(0xFFD7E3E8)


// ============================================================
// LOGIN
// ============================================================

@Composable
fun LoginSeniorLinkScreen(
    onEntrar: () -> Unit,
    onEntrarResponsavel: () -> Unit,
    onEntrarCuidador: () -> Unit,
    onCriarConta: () -> Unit,
    onEsqueciSenha: () -> Unit
) {

    var tipoAcesso by remember {
        mutableStateOf<String?>(null)
    }

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }

    var pin by remember {
        mutableStateOf("")
    }

    var mostrarSenha by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fundo)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )


            // ==================================================
            // LOGO
            // ==================================================

            Image(
                painter = painterResource(
                    id = R.drawable.seniorlink_logo
                ),
                contentDescription = "Logo SeniorLink",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )


            Spacer(
                modifier = Modifier.height(24.dp)
            )


            // ==================================================
            // ESCOLHA DO ACESSO
            // ==================================================

            if (tipoAcesso == null) {

                Text(
                    text = "Bem-vindo ao SeniorLink",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Como você vai usar o aplicativo?",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 17.sp,
                    color = Texto,
                    textAlign = TextAlign.Center
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )


                // ==================================================
                // SOU IDOSO
                // ==================================================

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            tipoAcesso = "idoso"
                        },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 5.dp
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.icon_idoso
                            ),
                            contentDescription = "Ícone do idoso",
                            modifier = Modifier.size(82.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(
                            modifier = Modifier.width(18.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Sou idoso",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrincipal
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Quero usar o SeniorLink",
                                fontSize = 14.sp,
                                color = Texto
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Verde
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // ==================================================
                // SOU RESPONSÁVEL
                // ==================================================

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            tipoAcesso = "responsavel"
                        },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 5.dp
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.icon_responsavel
                            ),
                            contentDescription = "Ícone do responsável",
                            modifier = Modifier.size(82.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(
                            modifier = Modifier.width(18.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Sou responsável",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrincipal
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Quero acompanhar alguém",
                                fontSize = 14.sp,
                                color = Texto
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Verde
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(18.dp)
                )


                // ==================================================
                // SOU CUIDADOR
                // ==================================================

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable {
                            tipoAcesso = "cuidador"
                        },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 5.dp
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                horizontal = 18.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                id = R.drawable.icon_cuidador
                            ),
                            contentDescription = "Ícone do cuidador",
                            modifier = Modifier.size(82.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(
                            modifier = Modifier.width(18.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Sou cuidador",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrincipal
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Cuido de uma pessoa idosa",
                                fontSize = 14.sp,
                                color = Texto
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Verde
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(28.dp)
                )


                // ==================================================
                // CRIAR CONTA
                // ==================================================

                TextButton(
                    onClick = onCriarConta
                ) {

                    Text(
                        text = "Criar uma conta",
                        color = Verde,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // ==================================================
                // AJUDA
                // ==================================================

                TextButton(
                    onClick = onEsqueciSenha
                ) {

                    Text(
                        text = "Preciso de ajuda para entrar",
                        color = AzulPrincipal,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }


            } else {

                // ==================================================
                // VOLTAR
                // ==================================================

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_voltar
                        ),
                        contentDescription = "Voltar",
                        modifier = Modifier
                            .size(52.dp)
                            .clickable {
                                tipoAcesso = null
                            },
                        contentScale = ContentScale.Fit
                    )
                }


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                // ==================================================
                // ACESSO DO IDOSO
                // ==================================================

                if (tipoAcesso == "idoso") {

                    Text(
                        text = "Acesso do idoso",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Digite seu PIN para entrar",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 17.sp,
                        color = Texto,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(28.dp)
                    )


                    OutlinedTextField(
                        value = pin,
                        onValueChange = { novoValor ->

                            if (
                                novoValor.length <= 4 &&
                                novoValor.all {
                                    it.isDigit()
                                }
                            ) {
                                pin = novoValor
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        singleLine = true,
                        label = {
                            Text(
                                text = "PIN de acesso",
                                fontSize = 16.sp
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite 4 números",
                                fontSize = 17.sp,
                                color = Texto
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation =
                            PasswordVisualTransformation(),
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul,
                            unfocusedTextColor = Azul,
                            focusedBorderColor = Verde,
                            unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde,
                            unfocusedLabelColor = Texto,
                            focusedPlaceholderColor = Texto,
                            unfocusedPlaceholderColor = Texto,
                            cursorColor = Verde,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )


                    Button(
                        onClick = onEntrar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulPrincipal
                        )
                    ) {

                        Text(
                            text = "Entrar",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )


                    TextButton(
                        onClick = onEsqueciSenha
                    ) {

                        Text(
                            text = "Preciso de ajuda para entrar",
                            color = AzulPrincipal,
                            fontSize = 14.sp
                        )
                    }


                } else if (tipoAcesso == "responsavel") {

                    // ==================================================
                    // ACESSO DO RESPONSÁVEL
                    // ==================================================

                    Text(
                        text = "Acesso do responsável",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Entre com seus dados",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 17.sp,
                        color = Texto,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        singleLine = true,
                        label = {
                            Text(
                                text = "E-mail",
                                fontSize = 15.sp
                            )
                        },
                        placeholder = {
                            Text(
                                text = "seuemail@exemplo.com",
                                fontSize = 16.sp,
                                color = Texto
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul,
                            unfocusedTextColor = Azul,
                            focusedBorderColor = Verde,
                            unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde,
                            unfocusedLabelColor = Texto,
                            focusedPlaceholderColor = Texto,
                            unfocusedPlaceholderColor = Texto,
                            cursorColor = Verde,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    OutlinedTextField(
                        value = senha,
                        onValueChange = {
                            senha = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Senha",
                                fontSize = 15.sp
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite sua senha",
                                fontSize = 16.sp,
                                color = Texto
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation =
                            if (mostrarSenha) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {

                            TextButton(
                                onClick = {
                                    mostrarSenha =
                                        !mostrarSenha
                                }
                            ) {

                                Text(
                                    text =
                                        if (mostrarSenha) {
                                            "OCULTAR"
                                        } else {
                                            "MOSTRAR"
                                        },
                                    color = Verde,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul,
                            unfocusedTextColor = Azul,
                            focusedBorderColor = Verde,
                            unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde,
                            unfocusedLabelColor = Texto,
                            focusedPlaceholderColor = Texto,
                            unfocusedPlaceholderColor = Texto,
                            cursorColor = Verde,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    TextButton(
                        onClick = onEsqueciSenha,
                        modifier = Modifier.align(
                            Alignment.End
                        )
                    ) {

                        Text(
                            text = "Esqueci minha senha",
                            color = AzulPrincipal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )


                    Button(
                        onClick = onEntrarResponsavel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulPrincipal
                        )
                    ) {

                        Text(
                            text = "Entrar",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }


                } else {

                    // ==================================================
                    // ACESSO DO CUIDADOR
                    // ==================================================

                    Text(
                        text = "Acesso do cuidador",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Entre com seus dados",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 17.sp,
                        color = Texto,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(26.dp)
                    )


                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        singleLine = true,
                        label = {
                            Text(
                                text = "E-mail",
                                fontSize = 15.sp
                            )
                        },
                        placeholder = {
                            Text(
                                text = "seuemail@exemplo.com",
                                fontSize = 16.sp,
                                color = Texto
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul,
                            unfocusedTextColor = Azul,
                            focusedBorderColor = Verde,
                            unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde,
                            unfocusedLabelColor = Texto,
                            focusedPlaceholderColor = Texto,
                            unfocusedPlaceholderColor = Texto,
                            cursorColor = Verde,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )


                    OutlinedTextField(
                        value = senha,
                        onValueChange = {
                            senha = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Senha",
                                fontSize = 15.sp
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Digite sua senha",
                                fontSize = 16.sp,
                                color = Texto
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType =
                                KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation =
                            if (mostrarSenha) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                        trailingIcon = {

                            TextButton(
                                onClick = {
                                    mostrarSenha =
                                        !mostrarSenha
                                }
                            ) {

                                Text(
                                    text =
                                        if (mostrarSenha) {
                                            "OCULTAR"
                                        } else {
                                            "MOSTRAR"
                                        },
                                    color = Verde,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Azul,
                            unfocusedTextColor = Azul,
                            focusedBorderColor = Verde,
                            unfocusedBorderColor = Borda,
                            focusedLabelColor = Verde,
                            unfocusedLabelColor = Texto,
                            focusedPlaceholderColor = Texto,
                            unfocusedPlaceholderColor = Texto,
                            cursorColor = Verde,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    TextButton(
                        onClick = onEsqueciSenha,
                        modifier = Modifier.align(
                            Alignment.End
                        )
                    ) {

                        Text(
                            text = "Esqueci minha senha",
                            color = AzulPrincipal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }


                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )


                    Button(
                        onClick = onEntrarCuidador,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AzulPrincipal
                        )
                    ) {

                        Text(
                            text = "Entrar",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(30.dp)
            )


            // ==================================================
            // RODAPÉ
            // ==================================================

            Text(
                text = "SeniorLink • Cuidado, autonomia e conexão",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )
        }
    }
}


// ============================================================
// CADASTRO
// ============================================================

@Composable
fun CadastroSeniorLinkScreen(
    onVoltar: () -> Unit,
    onCadastrar: () -> Unit
) {

    var nome by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }

    var confirmar by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.ic_voltar
            ),
            contentDescription = "Voltar",
            modifier = Modifier
                .size(52.dp)
                .clickable {
                    onVoltar()
                },
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Criar conta",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Azul
        )

        Text(
            text = "Preencha seus dados para começar.",
            color = Texto,
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )


        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = {
                Text("Nome completo")
            },
            placeholder = {
                Text(
                    "Digite seu nome",
                    color = Texto
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Azul,
                unfocusedTextColor = Azul,
                focusedBorderColor = Verde,
                unfocusedBorderColor = Borda,
                focusedLabelColor = Verde,
                unfocusedLabelColor = Texto,
                focusedPlaceholderColor = Texto,
                unfocusedPlaceholderColor = Texto,
                cursorColor = Verde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )


        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = {
                Text("E-mail")
            },
            placeholder = {
                Text(
                    "seuemail@exemplo.com",
                    color = Texto
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Azul,
                unfocusedTextColor = Azul,
                focusedBorderColor = Verde,
                unfocusedBorderColor = Borda,
                focusedLabelColor = Verde,
                unfocusedLabelColor = Texto,
                focusedPlaceholderColor = Texto,
                unfocusedPlaceholderColor = Texto,
                cursorColor = Verde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )


        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = {
                Text("Senha")
            },
            placeholder = {
                Text(
                    "Crie uma senha",
                    color = Texto
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            visualTransformation =
                PasswordVisualTransformation(),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Azul,
                unfocusedTextColor = Azul,
                focusedBorderColor = Verde,
                unfocusedBorderColor = Borda,
                focusedLabelColor = Verde,
                unfocusedLabelColor = Texto,
                focusedPlaceholderColor = Texto,
                unfocusedPlaceholderColor = Texto,
                cursorColor = Verde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )


        OutlinedTextField(
            value = confirmar,
            onValueChange = {
                confirmar = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            label = {
                Text("Confirmar senha")
            },
            placeholder = {
                Text(
                    "Digite a senha novamente",
                    color = Texto
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation =
                PasswordVisualTransformation(),
            shape = RoundedCornerShape(18.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Azul,
                unfocusedTextColor = Azul,
                focusedBorderColor = Verde,
                unfocusedBorderColor = Borda,
                focusedLabelColor = Verde,
                unfocusedLabelColor = Texto,
                focusedPlaceholderColor = Texto,
                unfocusedPlaceholderColor = Texto,
                cursorColor = Verde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )


        Text(
            text = "Você poderá definir depois se é idoso, responsável ou cuidador.",
            color = Texto,
            fontSize = 13.sp
        )


        Button(
            onClick = onCadastrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {

            Text(
                text = "Cadastrar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


// ============================================================
// BOAS-VINDAS
// ============================================================

@Composable
fun BoasVindasSeniorLinkScreen(
    onContinuar: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "💚",
            fontSize = 76.sp
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Tudo pronto!",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Azul,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Agora você faz parte do SeniorLink.",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 17.sp,
            color = Texto,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Button(
            onClick = onContinuar,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {

            Text(
                text = "Começar a usar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}