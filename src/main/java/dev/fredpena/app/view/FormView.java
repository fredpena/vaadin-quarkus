package dev.fredpena.app.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.app.data.Person;
import dev.fredpena.app.service.PersonService;
import dev.fredpena.app.utils.NotificationUtil;

/**
 * @author me@fredpena.dev
 * @created 24/01/2024  - 11:41
 */
public class FormView {

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final EmailField email = new EmailField("Email");
    private final TextField phone = new TextField("Phone Number");
    private final DatePicker dateOfBirth = new DatePicker("Birthday");
    private final TextField occupation = new TextField("Occupation");
    private final ComboBox<String> fieldRole = new ComboBox<>("Role");
    private final Checkbox important = new Checkbox("Is important?");

    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final PersonService personService;
    private Person element;
    private final BeanValidationBinder<Person> binder;

    private Runnable actionRunnable;

    public FormView(PersonService personService) {
        this.personService = personService;

        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);

        cancel.addThemeVariants(ButtonVariant.LUMO_SMALL);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        fieldRole.setItems("Worker", "Supervisor", "Manager", "External");

        // Bind fields. This is where you'd define e.g. validation rules
        // Configure Form
        binder = new BeanValidationBinder<>(Person.class);

        // Bind fields. This where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        binder.forField(fieldRole)
                .asRequired("This field can not be blank.")
                .bind(Person::getRole, Person::setRole);

        save.addClickListener(this::saveOrUpdate);

        delete.addClickListener(this::delete);
    }

    private void saveOrUpdate(ClickEvent<Button> buttonClickEvent) {
        if (this.element == null) {
            this.element = new Person();
        }

        try {
            binder.writeBean(element);

            var confirmDialog = new ConfirmDialog();
            confirmDialog.setHeader("Unsaved changes");
            confirmDialog.setText("There are unsaved changes. Do you want to discard them or continue?");

            confirmDialog.setRejectable(true);
            confirmDialog.setRejectText("Discard");

            confirmDialog.setConfirmText("Continue");
            confirmDialog.addConfirmListener(event -> {
                personService.createOrUpdate(element);
                NotificationUtil.notificationSuccess("The transaction was successful.");
                actionRunnable.run();
            });

            confirmDialog.open();

        } catch (ValidationException validationException) {
            NotificationUtil.notificationError(validationException);
        }
    }

    private void delete(ClickEvent<Button> buttonClickEvent) {
        var confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Deleting record");
        confirmDialog.setText("You are deleting a record. Do you want to discard them or continue?");

        confirmDialog.setRejectable(true);
        confirmDialog.setRejectText("Discard");
        confirmDialog.setRejectButtonTheme("tertiary");

        confirmDialog.setConfirmText("Continue");
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.addConfirmListener(event -> {
            personService.delete(element.getCode());
            NotificationUtil.notificationSuccess("The record was deleted.");
            actionRunnable.run();
        });

        confirmDialog.open();

    }

    public Dialog createDialog(String title, Person element, Runnable reload) {
        this.element = element;
        binder.readBean(element);

        delete.setVisible(element != null);

        Dialog dialog = new Dialog();
        dialog.setCloseOnOutsideClick(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setOpened(true);
        dialog.setHeaderTitle(title);
        dialog.add(createFormLayout());
        dialog.getFooter().add(createButtonLayout());

        cancel.addClickListener(event -> {
            dialog.close();
            reload.run();
        });

        actionRunnable = () -> {
            dialog.close();
            reload.run();
        };

        return dialog;
    }

    private FormLayout createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, email, phone, dateOfBirth, occupation, fieldRole, important);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );
        formLayout.addClassNames(LumoUtility.MaxWidth.SCREEN_MEDIUM, LumoUtility.AlignSelf.CENTER);
        return formLayout;
    }

    private HorizontalLayout createButtonLayout() {
        var deleteDiv = new Div();
        deleteDiv.addClassNames(LumoUtility.Margin.End.AUTO);
        deleteDiv.add(delete);

        var buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.FlexWrap.WRAP, LumoUtility.JustifyContent.CENTER);
        buttonLayout.add(deleteDiv, cancel, save);
        buttonLayout.setAlignItems(FlexComponent.Alignment.END);
        return buttonLayout;
    }


}
