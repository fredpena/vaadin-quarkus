package dev.fredpena.app.view.security;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.app.data.User;
import dev.fredpena.app.exception.FirebaseException;
import dev.fredpena.app.service.FirebaseService;
import dev.fredpena.app.utils.NotificationUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author me@fredpena.dev
 * @created 28/01/2024  - 13:58
 */
@Slf4j
@Route(value = "singup")
@PageTitle("Sign Up")
@AnonymousAllowed
@Uses(Icon.class)
public class SingUpView extends HorizontalLayout {

    private final transient FirebaseService firebaseService;
    private final Binder<User> binder;

    private static final String VALIDATION_MESSAGE = "This field can not be blank";


    SingUpView(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;

        // Bind fields. This is where you'd define e.g. validation rules
        binder = new BeanValidationBinder<>(User.class);

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


        H1 loginH1 = new H1("Sign Up");
        loginH1.getStyle()
                .setColor("inherit");

        loginH1.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.XXXLARGE);

        Paragraph paragraph = new Paragraph("Create your account by filling the form below");
        paragraph.getStyle()
                .setColor("var(--lumo-tint-70pct)")
                .setMarginInlineStart("0")
                .setMarginInlineEnd("0")
                .set("margin-block-start", "1em")
                .set("margin-block-end", "1em");

        paragraph.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);

        brand.add(loginH1);
        brand.add(paragraph);

        Div form = new Div();
        form.getStyle()
                .setColor("var(--lumo-tint-70pct)");

        form.addClassNames(LumoUtility.Padding.LARGE, LumoUtility.Flex.SHRINK, LumoUtility.Display.FLEX);
        form.addClassNames(LumoUtility.FlexDirection.COLUMN, LumoUtility.BoxSizing.BORDER);

        final EmailField email = new EmailField("Email Address", "you@fredpena.dev");
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        final PasswordField password = new PasswordField("Password", "Enter 6 character or more");
        password.setPrefixComponent(VaadinIcon.PASSWORD.create());

        final PasswordField passwordRepeat = new PasswordField("Confirm password", "Enter 6 character or more");
        passwordRepeat.setPrefixComponent(VaadinIcon.PASSWORD.create());


        final TextField displayName = new TextField("Your name", "Enter your name");
        displayName.setPrefixComponent(VaadinIcon.USER_CARD.create());

        final TextField phone = new TextField("Phone number", "Enter your phone number");
        phone.setPrefixComponent(VaadinIcon.PHONE.create());


        Arrays.asList(email, password, passwordRepeat, displayName, phone)
                .forEach(c -> {
                    c.setRequiredIndicatorVisible(true);
                    c.setClearButtonVisible(true);
                });

        Checkbox agree = new Checkbox();
        agree.addClassNames(LumoUtility.Margin.Top.SMALL);

        Anchor termsConditions = createAnchor("", "Terms and conditions");
        Anchor privacyPolices = createAnchor("", "Privacy Polices");

        HorizontalLayout agreeLLayout = new HorizontalLayout(agree, createParagraph("I agree with all"), termsConditions,
                createParagraph("and"), privacyPolices, createParagraph("of fredpena.dev"));
        agreeLLayout.addClassNames(LumoUtility.FlexWrap.WRAP, LumoUtility.Margin.MEDIUM, LumoUtility.AlignItems.BASELINE);
        agreeLLayout.getStyle().set("gap", "2%");


        binder.forField(email)
                .asRequired(VALIDATION_MESSAGE)
                .withValidator(v -> v.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"), "Enter a valid email address")
                .bind(User::getEmail, User::setEmail);

        binder.forField(password)
                .asRequired(VALIDATION_MESSAGE)
                .withValidator(v -> v.length() >= 6, "Password must be at least 6 characters long")
                .withValidator(v -> password.getValue().equals(passwordRepeat.getValue()), "Passwords must match")
                .bind(User::getPassword, User::setPassword);

        binder.forField(passwordRepeat)
                .asRequired(VALIDATION_MESSAGE)
                .withValidator(v -> v.length() >= 6, "Password must be at least 6 characters long")
                .withValidator(v -> password.getValue().equals(passwordRepeat.getValue()), "Passwords must match")
                .bind(User::getPasswordRepeat, User::setPasswordRepeat);

        binder.forField(displayName)
                .asRequired(VALIDATION_MESSAGE)
                .bind(User::getDisplayName, User::setDisplayName);

        binder.forField(phone)
                .asRequired(VALIDATION_MESSAGE)
                .withValidator(v -> v.startsWith("+"), "Phone number must be a valid, E.164 compliant identifier starting with a '+' sign")
                .bind(User::getPhone, User::setPhone);

        binder.forField(agree)
                .withValidator(v -> v, "You must accept the Terms and conditions and Privacy Policies")
                .bind(User::isAgree, User::setAgree);

        final Button ok = new Button("Sign Up");
        ok.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        ok.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        ok.addClickListener(click -> {

            try {
                User user = new User();
                user.setPhotoUrl("https://gravatar.com/userimage/56671691/9cf4e9eccc0327d8a05340d03a9631d6.jpeg?size=256");

                binder.writeBean(user);

                firebaseService.createUser(user);

                UI.getCurrent().navigate(LoginView.class);
            } catch (FirebaseException ex) {
                NotificationUtil.notificationError(ex.getMessage());
            } catch (ValidationException ex) {
                NotificationUtil.notificationError(ex);
            }
        });

        final Button login = new Button("Login", click -> UI.getCurrent().navigate(LoginView.class));
        login.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_SMALL);


        form.add(email, password, passwordRepeat, displayName, phone, agreeLLayout, ok, login);

        section.add(brand, form);

        return section;
    }

    private static Paragraph createParagraph(String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.addClassNames(LumoUtility.FontSize.SMALL);
        paragraph.getStyle()
                .setMargin("0")
                .setColor("var(--vaadin-input-field-label-color, var(--lumo-secondary-text-color))")
                .setMarginInlineStart("0")
                .setMarginInlineEnd("0");

        return paragraph;
    }

    private static Anchor createAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);
        anchor.addClassNames(LumoUtility.FontSize.SMALL);
        anchor.getStyle()
                .setMarginTop("2px")
                .setMarginInlineStart("0")
                .setMarginInlineEnd("0");

        anchor.addClassNames(LumoUtility.LineHeight.SMALL, LumoUtility.Margin.Bottom.NONE, LumoUtility.Display.BLOCK);

        return anchor;
    }


}
