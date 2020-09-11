package com.customer.component.panel.interfaces

/**
 * --------------------
 * | PanelSwitchLayout  |
 * |  ----------------  |
 * | |                | |
 * | |ContentContainer| |
 * | |                | |
 * |  ----------------  |
 * |  ----------------  |
 * | | PanelContainer | |
 * |  ----------------  |
 * --------------------
 * There are some rules that must be processed:
 *
 * 1. [com.lib.basiclib.panel.view.PanelSwitchLayout] must have only two children
 * [com.lib.basiclib.panel.view.content.IContentContainer] and [com.lib.basiclib.panel.view.PanelContainer]
 *
 * 2. [com.lib.basiclib.panel.view.content.IContentContainer] must set "edit_view" value to provide [android.widget.EditText]
 *
 * 3. [com.lib.basiclib.panel.view.PanelContainer] has some Children that are [com.lib.basiclib.panel.view.PanelView]
 * [com.lib.basiclib.panel.view.PanelView] must set "panel_layout" value to provide panelView and set "panel_trigger"  value to
 * specify layout for click to checkout panelView
 *
 */
interface ViewAssertion {
    fun assertView()
}