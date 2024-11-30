package com.vaadin.securitydemo.security

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.i18n
import com.github.mvysny.kaributools.setErrorMessage
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import eu.vaadinonkotlin.vaadin.Session
import org.slf4j.LoggerFactory
import javax.security.auth.login.LoginException

/**
 * The login view which simply shows the login form full-screen. Allows the user to log in. After the user has been logged in,
 * the page is refreshed which forces the MainLayout to reinitialize. However, now that the user is present in the session,
 * the reroute to login view no longer happens and the MainLayout is displayed on screen properly.
 */
@Route("login_v2")
@PageTitle("Login V2")
@AnonymousAllowed
class LoginRoute_v2 : KComposite() {
    private lateinit var loginOverlay: LoginOverlay
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false; content { center() }

            val i18n = LoginI18n.createDefault()

            val i18nHeader = LoginI18n.Header().apply {
                title = "Sovelluksen nimi"
                description = "Sovelluksen kuvaus"
            }
            i18n.header = i18nHeader

            val i18nForm = i18n.form.apply {
                title = "Kirjaudu sisään"
                username = "Käyttäjänimi"
                password = "Salasana"
                submit = "Kirjaudu sisään"
                forgotPassword = "Unohtuiko salasana?"
            }
            i18n.form = i18nForm

            val i18nErrorMessage = i18n.errorMessage.apply {
                title = "Väärä käyttäjätunnus tai salasana"
                message = "Tarkista että käyttäjätunnus ja salasana ovat oikein ja yritä uudestaan."
            }
            i18n.errorMessage = i18nErrorMessage

            i18n.additionalInformation = "Jos tarvitset lisätietoja käyttäjälle."

            loginOverlay = LoginOverlay().apply {
//                setI18n(i18n)

                description = "Built with ♥ by Vaadin"
 //               isOpened = true
                element.setAttribute("no-autofocus", "")

            }

        }
    }

    init {
        loginOverlay.addLoginListener { e ->
            try {
                Session.loginService.login(e.username, e.password)
            } catch (e: LoginException) {
                log.warn("Login failed", e)
                loginOverlay.setErrorMessage("Login failed", e.message)
            } catch (e: Exception) {
                log.error("Internal error", e)
                loginOverlay.setErrorMessage("Internal error", e.message)
            }
        }
        loginOverlay.addForgotPasswordListener { e ->
            Notification.show( "Forget password email sent.")
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(LoginRoute_v2::class.java)
    }
}