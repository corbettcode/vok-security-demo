package com.vaadin.securitydemo.components

import com.github.vok.karibudsl.flow.VaadinDsl
import com.github.vok.karibudsl.flow.init
import com.vaadin.flow.component.*
import com.vaadin.flow.component.dependency.HtmlImport
import com.vaadin.flow.component.icon.VaadinIcons

/**
 * Creates an icon component that displays the given `icon` from given `collection`.
 * @param collection the icon collection
 * @param icon the icon name
 */
@Tag("paper-icon-button")
@HtmlImport("frontend://bower_components/paper-icon-button/paper-icon-button.html")
class PaperIconButton(collection: String, icon: String) : Component(), ClickNotifier<PaperIconButton> {
    /**
     * Creates an icon component that displays given Vaadin [icon].
     */
    constructor(icon: VaadinIcons) : this("vaadin", icon.name.toLowerCase().replace('_', '-'))
    init {
        element.setAttribute("icon", "$collection:$icon")
    }
}

fun (@VaadinDsl HasComponents).paperIconButton(icon: VaadinIcons, block: (@VaadinDsl PaperIconButton).() -> Unit = {}) = init(
    PaperIconButton(icon), block)
