package dev.fredpena.app.utils;

import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.ValidationException;

/**
 * @author me@fredpena.dev
 * @created 27/01/2024  - 11:10
 */
public final class NotificationUtil {
    private NotificationUtil() {
    }

    public static void notificationError(String msg) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        notification(notification, msg);
    }

    public static void notificationSuccess(String msg) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        notification(notification, msg);
    }

    public static void notificationWarning(String msg) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_WARNING);

        notification(notification, msg);
    }

    private static void notification(Notification notification, String msg) {
        notification.setDuration(3000);
        notification.setPosition(Notification.Position.TOP_END);

        Icon icon = VaadinIcon.CHECK_CIRCLE.create();

        final Button closeButton = new Button(new Icon("lumo", "cross"), event -> notification.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.getStyle().setMargin("0 0 0 var(--lumo-space-l)");

        final HorizontalLayout layout = new HorizontalLayout(icon, new Text(msg), closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        notification.add(layout);
        notification.open();
    }

    public static void notificationError(ValidationException validationException) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        validationException.getFieldValidationErrors().forEach(err -> err.getMessage().ifPresent(msg2 -> {
            String label = ((HasLabel) err.getBinding().getField()).getLabel();

            notificationError(label != null ? label + " -> " + msg2 : msg2);
        }));

    }
}
