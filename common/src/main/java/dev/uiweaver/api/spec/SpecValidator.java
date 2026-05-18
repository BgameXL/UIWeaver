package dev.uiweaver.api.spec;

import dev.uiweaver.api.binding.UIBinding;
import dev.uiweaver.api.component.ButtonComponent;
import dev.uiweaver.api.component.UIComponent;
import dev.uiweaver.api.slot.SlotBindingDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class SpecValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecValidator.class);

    static void validate(UIScreenSpec spec) {
        Set<String> syncKeys    = spec.getSyncs().stream().map(s -> s.getKey()).collect(Collectors.toSet());
        Set<String> actionIds   = spec.getActions().stream().map(a -> a.getActionId()).collect(Collectors.toSet());
        Set<String> slotNames   = spec.getSlotBindings().stream().map(SlotBindingDeclaration::getName).collect(Collectors.toSet());
        String screen           = spec.getScreenId();

        Set<String> seenIds = new HashSet<>();
        walkTree(spec.getRoot(), screen, syncKeys, actionIds, slotNames, seenIds);

        for (UIBinding b : spec.getBindings()) {
            if (b.componentId() == null) {
                LOGGER.warn("[UIWeaver] [{}] Binding has null componentId (property='{}', syncKey='{}')",
                        screen, b.property(), b.syncKey());
            }
            if (!syncKeys.contains(b.syncKey())) {
                LOGGER.warn("[UIWeaver] [{}] Binding references unknown sync key '{}' on component '{}'",
                        screen, b.syncKey(), b.componentId());
            }
        }
    }

    private static void walkTree(UIComponent component, String screen,
                                  Set<String> syncKeys, Set<String> actionIds,
                                  Set<String> slotNames, Set<String> seenIds) {
        String id = component.getId();

        if (id != null && !seenIds.add(id)) {
            LOGGER.warn("[UIWeaver] [{}] Duplicate component id '{}'", screen, id);
        }

        if (component instanceof ButtonComponent btn && btn.getActionId() != null) {
            if (!actionIds.contains(btn.getActionId())) {
                String label = id != null ? id : "(no id)";
                LOGGER.warn("[UIWeaver] [{}] Button '{}' references unknown action '{}'",
                        screen, label, btn.getActionId());
            }
        }

        for (UIComponent child : component.getChildren()) {
            walkTree(child, screen, syncKeys, actionIds, slotNames, seenIds);
        }
    }
}