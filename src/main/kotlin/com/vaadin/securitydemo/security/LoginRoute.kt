package com.vaadin.securitydemo.security

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.setErrorMessage
import com.vaadin.flow.component.login.AbstractLogin.ForgotPasswordEvent
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
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
@Route("login")
@PageTitle("Login")
@AnonymousAllowed
class LoginRoute : KComposite() {
    private lateinit var loginForm: LoginForm
    private val root = ui {
        verticalLayout {
            setSizeFull(); isPadding = false; content { center() }

            val loginI18n: LoginI18n = loginI18n {
                form.title = "VoK Security Demo"
                additionalInformation = "Log in as user/user or admin/admin"
            }
            loginForm = loginForm(loginI18n)
        }
    }

    init {
        loginForm.addLoginListener { e ->
            try {
                Session.loginService.login(e.username, e.password)
            } catch (e: LoginException) {
                log.warn("Login failed", e)
                loginForm.setErrorMessage("Login failed", e.message)
            } catch (e: Exception) {
                log.error("Internal error", e)
                loginForm.setErrorMessage("Internal error", e.message)
            }
        }
        loginForm.addForgotPasswordListener { e ->
            Notification.show( "Forget password email sent.")
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(LoginRoute::class.java)
    }
}