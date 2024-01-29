package dev.fredpena.app.view.security;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.app.utils.NotificationUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author me@fredpena.dev
 * @created 27/01/2024  - 10:05
 */
@Slf4j
@Route(value = "")
@PageTitle("Login")
@AnonymousAllowed
@Uses(Icon.class)
public class LoginView extends HorizontalLayout {


    LoginView() {

        addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(createLoginLayout());
    }

    private Component createLoginLayout() {
        Section section = new Section();
        section.getStyle()
                .setWidth("calc(var(--lumo-size-m) * 10)")
                .setBackground("var(--lumo-base-color) linear-gradient(var(--lumo-tint-5pct), var(--lumo-tint-5pct))");

        section.addClassNames(LumoUtility.MaxWidth.FULL, LumoUtility.BoxSizing.BORDER, LumoUtility.Overflow.HIDDEN);
        section.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.BorderRadius.LARGE);
        section.addClassNames(LumoUtility.BoxShadow.SMALL, LumoUtility.Margin.SMALL, LumoUtility.Height.AUTO);

        Div brand = new Div();
        brand.getStyle()
                .setFlexShrink("0")
                .setFlexGrow("1")
                .setPadding("var(--lumo-space-l) var(--lumo-space-xl) var(--lumo-space-l) var(--lumo-space-l)")
                .setMinHeight("calc(var(--lumo-size-m) * 5)");

        brand.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.JustifyContent.END);
        brand.addClassNames(LumoUtility.Background.PRIMARY, LumoUtility.TextColor.PRIMARY_CONTRAST, LumoUtility.MinHeight.SCREEN);
        brand.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Overflow.HIDDEN);


        H1 loginH1 = new H1("Login");
        loginH1.getStyle()
                .setColor("inherit");

        loginH1.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.XXXLARGE);

        Paragraph paragraph = new Paragraph("Doesn`t have an account yet?");
        paragraph.getStyle()
                .setColor("var(--lumo-tint-70pct)")
                .setMarginInlineStart("0")
                .setMarginInlineEnd("0")
                .set("margin-block-start", "1em")
                .set("margin-block-end", "1em");

        paragraph.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);

        Anchor singUpLink = createAnchor("singup", "Sign Up");
        singUpLink.addClassNames(LumoUtility.TextColor.HEADER);

        HorizontalLayout paragraphLayout = new HorizontalLayout(paragraph, singUpLink);
        paragraphLayout.addClassNames(LumoUtility.FlexWrap.WRAP);

        brand.add(loginH1);
        brand.add(paragraphLayout);

        Div form = new Div();
        form.getStyle()
                .setColor("var(--lumo-tint-70pct)");

        form.addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Flex.SHRINK, LumoUtility.Display.FLEX);
        form.addClassNames(LumoUtility.FlexDirection.COLUMN, LumoUtility.BoxSizing.BORDER);

        final EmailField email = new EmailField("Email Address", "you@fredpena.dev");
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        email.addClassNames(LumoUtility.Margin.Top.SMALL);
        email.setRequiredIndicatorVisible(true);
        email.setClearButtonVisible(true);

        final PasswordField password = new PasswordField("Password", "Enter 6 character or more");
        password.setPrefixComponent(VaadinIcon.PASSWORD.create());
        password.addClassNames(LumoUtility.Margin.Top.SMALL);
        password.setRequiredIndicatorVisible(true);
        password.setClearButtonVisible(true);

        Anchor forgotPassword = createAnchor("", "Forgot Password?");
        forgotPassword.addClassNames(LumoUtility.AlignSelf.END);

        Checkbox rememberMe = new Checkbox("Remember me");
        rememberMe.addClassNames(LumoUtility.Margin.Top.SMALL);

        final Button ok = new Button("Login");
        ok.addClickShortcut(Key.ENTER);
        ok.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        Div hr = loginWith();

        form.add(email, password, forgotPassword, rememberMe, ok, hr, createButtonLayout());

        section.add(brand, form);

        return section;
    }

    private static Component createButtonLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassNames(LumoUtility.Padding.NONE, LumoUtility.Padding.NONE);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 250px
                new FormLayout.ResponsiveStep("250px", 2));

        final Button google = createButton("Google");

        final Button microsoft = createButton("Microsoft");

        final Button facebook = createButton("Facebook");

        final Button twitter = createButton("Twitter");

        final Button apple = createButton("Apple");

        final Button gitHub = createButton("GitHub");

        formLayout.add(google, microsoft, facebook, twitter, apple, gitHub);

        return formLayout;
    }

    private static Button createButton(String text) {
        final Button button = new Button(text, createIcon(text.toLowerCase()), click -> NotificationUtil.notificationWarning("Coming Soon!"));
        button.addClassNames(LumoUtility.TextColor.HEADER, LumoUtility.BoxShadow.XLARGE, LumoUtility.BorderRadius.LARGE);
        button.getStyle().setBackground("white");

        return button;
    }

    private static Component createIcon(String name) {
        Image image = new Image("images/idp/%s.png".formatted(name), "");
        image.setMaxWidth("25px");
        return image;
    }

    private static Div loginWith() {
        Span spanHr = new Span("or login with");
        spanHr.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Background.BASE, LumoUtility.TextColor.SECONDARY);
        spanHr.addClassNames(LumoUtility.Padding.Left.SMALL, LumoUtility.Padding.Right.SMALL);

        Div hr = new Div();
        hr.addClassNames(LumoUtility.Margin.Top.LARGE, LumoUtility.Margin.Bottom.LARGE);
        hr.addClassNames(LumoUtility.Width.FULL, LumoUtility.TextAlignment.CENTER, LumoUtility.Border.BOTTOM, LumoUtility.BorderColor.CONTRAST_30);
        hr.setHeight("15px");
        hr.add(spanHr);
        return hr;
    }

    private static Anchor createAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);

        anchor.getStyle()
                .setTextDecoration("underline")
                .set("margin-block-start", "1em")
                .set("margin-block-end", "1em");

        anchor.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);
        anchor.addClassNames(LumoUtility.FontWeight.SEMIBOLD);

        return anchor;
    }

}
