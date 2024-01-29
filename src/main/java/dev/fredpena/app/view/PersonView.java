package dev.fredpena.app.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.fredpena.app.data.Person;
import dev.fredpena.app.service.PersonService;
import io.quarkus.panache.common.Page;

/**
 * @author me@fredpena.dev
 * @created 24/01/2024  - 11:41
 */

@PageTitle("List - Person")
@Route(value = "person")
@Uses(Icon.class)
public class PersonView extends VerticalLayout {

    private final TextField searchField = new TextField();
    private Grid<Person> grid;
    private final transient PersonService personService;
    private final transient FormView formView;

    public PersonView(PersonService personService) {
        this.personService = personService;
        this.formView = new FormView(personService);

        setSizeFull();
        add(createTopBar(), createGrid());
    }


    private void edit(Person r) {
        formView.createDialog("Edit Person", r, this::refreshGrid);
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }


    private HorizontalLayout createTopBar() {
        searchField.focus();
        searchField.setPlaceholder("Search...");
        searchField.setClearButtonVisible(true);
        searchField.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(e -> refreshGrid());

        // Action buttons
        final Button resetBtn = new Button("Clear", VaadinIcon.FILE_REMOVE.create());
        resetBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        resetBtn.addClickShortcut(Key.ESCAPE);
        resetBtn.setTooltipText("Esc");
        resetBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        resetBtn.addClickListener(e -> {
            searchField.clear();
            refreshGrid();
        });

        final Button createBtn = new Button("New", VaadinIcon.PLUS.create());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        // A shortcut to click  on the button by pressing ctrl + N
        createBtn.addFocusShortcut(Key.KEY_N, KeyModifier.CONTROL);
        createBtn.setTooltipText("Add new");
        createBtn.addClickListener(e -> formView.createDialog("New Person", null, PersonView.this::refreshGrid));

        HorizontalLayout layout = new HorizontalLayout(searchField, resetBtn, createBtn);
        layout.setFlexGrow(1, searchField);
        layout.setWidthFull();
        return layout;
    }

    private Component createGrid() {
        grid = new Grid<>(Person.class, false);
        grid.addColumn(createNameRenderer()).setHeader("Person").setAutoWidth(true);
        grid.addColumn(createContactRenderer()).setHeader("Contact").setAutoWidth(true);
        grid.addColumn(new LocalDateRenderer<>(Person::getDateOfBirth, "MMM d, YYYY")).setHeader("Birth date").setAutoWidth(true);
        grid.addColumn(Person::getOccupation).setHeader("Occupation").setAutoWidth(true);
        grid.addColumn(Person::getRole).setHeader("Role").setAutoWidth(true);
        grid.addComponentColumn(c -> createPermissionIcon(c.isImportant())).setHeader("Is important").setAutoWidth(true);
        grid.addColumn(createActionButton(this::edit)).setHeader("Action");

        grid.setItems(query -> personService.listAll(Page.of(query.getPage(), query.getPageSize()), searchField.getValue()).stream());

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }


    private static Renderer<Person> createActionButton(SerializableConsumer<Person> consumer) {
        return new ComponentRenderer<>(Button::new, (button, p) -> {
            button.addThemeVariants(ButtonVariant.LUMO_SMALL);
            button.addClickListener(e -> consumer.accept(p));
            button.setIcon(LumoIcon.EDIT.create());
        });
    }

    private static Icon createPermissionIcon(boolean isImportant) {
        Icon icon;
        if (isImportant) {
            icon = createIcon(VaadinIcon.CHECK, "Yes");
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = createIcon(VaadinIcon.CLOSE_SMALL, "No");
            icon.getElement().getThemeList().add("badge error");
        }
        return icon;
    }

    private static Icon createIcon(VaadinIcon vaadinIcon, String label) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        return icon;
    }

    private static Renderer<Person> createNameRenderer() {
        return LitRenderer.<Person>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                        + "<vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.lastName}\" alt=\"User avatar\"></vaadin-avatar>"
                        + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                        + "    <span style=\"font-size: var(--lumo-font-size-xs); color: var(--lumo-primary-text-color);\">"
                        + "      ${item.firstName}" + "    </span>"
                        + "    <span style=\"font-size: var(--lumo-font-size-m); \">"
                        + "      ${item.lastName}" + "    </span>"
                        + "  </vaadin-vertical-layout>"
                        + "</vaadin-horizontal-layout>")
                .withProperty("pictureUrl", Person::getFirstName)
                .withProperty("firstName", Person::getFirstName)
                .withProperty("lastName", Person::getLastName);
    }

    private static Renderer<Person> createContactRenderer() {
        return LitRenderer.<Person>of(
                        "<vaadin-vertical-layout style=\"width: 100%; font-size: var(--lumo-font-size-s); line-height: var(--lumo-line-height-m);\">"
                        + " <a href=\"mailto:${item.email}\" style=\"display: ${item.emailDisplay}; align-items: center;\">"
                        + "     <vaadin-icon icon=\"vaadin:${item.emailIcon}\" style=\"margin-inline-end: var(--lumo-space-xs); width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s);\"></vaadin-icon>"
                        + "     <span>${item.email}</span>"
                        + " </a>"
                        + " "
                        + " <a href=\"tel:${item.phone}\" style=\"display: ${item.phoneDisplay}; align-items: center;\">"
                        + "     <vaadin-icon icon=\"vaadin:${item.phoneIcon}\" style=\"margin-inline-end: var(--lumo-space-xs); width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s);\"></vaadin-icon>"
                        + "     <span>${item.phone}</span>"
                        + " </a>"
                        + "</vaadin-vertical-layout>")
                .withProperty("email", r -> r.getEmail() != null ? r.getEmail() : "")
                .withProperty("emailIcon", r -> r.getEmail() != null ? "envelope" : "")
                .withProperty("emailDisplay", r -> r.getEmail() != null ? "flex" : "none")
                .withProperty("phone", r -> r.getPhone() != null ? r.getPhone() : "")
                .withProperty("phoneIcon", r -> r.getPhone() != null ? "phone" : "")
                .withProperty("phoneDisplay", r -> r.getPhone() != null ? "flex" : "none");
    }

}
