# UIWeaver

**UIWeaver** is an experimental UI framework, focused on building server-driven, declarative, and debuggable interfaces without writing every screen by hand.

The goal is to make Minecraft mod UI's feel closer to modern app layouts while still respecting the realities of Minecraft screens, server validation, inventories, synced state, and mod-loader integration.

Status: experimental framework

---

## Why UIWeaver?

Minecraft GUI development often becomes repetitive and fragile:

- every screen manually positions widgets with hardcoded coordinates
- layouts break when the screen size or GUI scale changes
- client-side buttons can accidentally trust state that should be validated server-side
- sync logic, rendering logic, input handling, and inventory logic easily get mixed together
- debugging layout issues is painful because component bounds are usually invisible

UIWeaver tries to solve this by separating UI declaration, layout, rendering, synced state, and server actions.

Instead of manually drawing everything at fixed positions, a UI can be described as a tree of components:

```java
ui.column()
    .padding(8)
    .gap(6)
    .add(ui.label(Component.literal("Machine Dashboard")))
    .add(ui.row()
        .gap(4)
        .add(ui.button(Component.literal("Start")).action("start"))
        .add(ui.button(Component.literal("Stop")).action("stop"))
    )
    .add(ui.progressBar().bind("progress"));
```
---
## Declarative UI
Screens are built from components such as:

- `column`
- `row`
- `grid`
- `tabs`
- `scrollPanel`
- `label`
- `button`
- `toggle`
- `checkbox`
- `slider`
- `textInput`
- `progressBar`
- `energyBar`
- `fluidBar`
- `slotGrid`

The UI describes **what** should exist, while the framework decides **where** it should be placed.

---
### Layout Containers
Layout containers manage child positioning automatically.

Examples:
```java
ui.column().padding(8).gap(4)
```
```java
ui.row().gap(8).fillWidth()
```
```java
ui.grid(3, 3)
```
This avoids manually calculating `x`, `y`, `width`, and `height` for every widget.

---
### Server-Driven Actions

Buttons and interactive widgets can send named actions back to the server.

Example:
```java
ui.button(Component.literal("Start"))
    .id("btnStart")
    .action("start");
```
The server can then decide whether the action is valid.

This is important because the client should never be trusted to directly mutate machine state.

---
### ViewModel Sync
UIWeaver uses a `UIViewModel` concept to expose synced values to the UI.
```text
energy = 100
maxEnergy = 10000
working = false
progress = 42
```
Widgets such as bars, labels, and toggles can read from this state and update visually.

---

### Debug Overlay and Inspector
UIWeaver includes debugging tools to inspect component bounds and layout behavior.

Useful for finding issues such as:

- components overflowing their parent;
- inactive tabs still being inspected;
- stale child bounds after layout changes;
- slot grids not matching their visual position;
- panels being clipped or placed too high.
- 
The debug overlay can draw colored bounds for visible components, and the inspector can show component type, id, bounds, visibility, and ViewModel values.

---
## Example

A basic machine UI can be structured as:
```java
.root(ui.column().padding(10).gap(8).size(530, 340)

    .add(ui.row().gap(8).fillWidth().height(28)
        .add(ui.column().gap(2).grow().height(28)
            .add(ui.label(Component.literal("UIWeaver Reactor"))
                .id("title"))
            .add(ui.label(Component.literal("Server driven test machine"))
                .id("subtitle"))
        )
        .add(ui.button(Component.literal("Start"))
            .id("btnStart")
            .action("start")
            .size(54, 20))
        .add(ui.button(Component.literal("Stop"))
            .id("btnStop")
            .action("stop")
            .size(54, 20))
    )

    .add(ui.row().gap(8).fillWidth().height(58)
        .add(ui.energyBar()
            .id("energyBar")
            .bind("energy", "maxEnergy")
            .size(12, 54))

        .add(ui.column().gap(5).grow().height(58)
            .add(ui.label(Component.literal("Main cycle")))
            .add(ui.progressBar()
                .id("mainProgress")
                .fillWidth()
                .height(12))
        )

        .add(ui.fluidBar()
            .id("fluidReserve")
            .size(12, 54))
    )

    .add(ui.tabs().id("mainTabs").fillWidth().grow()
        .tab("Main", ui.column().padding(8).gap(6)
            .add(ui.label(Component.literal("Machine State")))
            .add(ui.toggle().id("statusToggle"))
        )

        .tab("Inv", ui.column().padding(8).gap(6)
            .add(ui.slotGrid(3, 3)
                .id("slots")
                .bind("machineInv"))
        )

        .tab("Tune", ui.column().padding(8).gap(7)
            .add(ui.slider(0, 100)
                .id("speedSlider")
                .value(40))
            .add(ui.button(Component.literal("Apply tuning"))
                .id("applyTuning"))
        )
    )
)
```
---
## Current Features

- Component tree based UI declarations
- Rows, columns, grids, tabs, and scroll panels
- Labels, buttons, toggles, checkboxes, sliders, and text inputs
- Progress, energy, and fluid bars
- Basic inventory slot grids
- ViewModel-driven state
- Named server actions
- Forge client renderer integration
- Debug overlay for component bounds
- UI inspector for tree and ViewModel debugging

---
## Known Limitations
UIWeaver is still early and some systems need more work.
### Slot Layout
Slot visuals and actual Minecraft `Slot` positions need to stay perfectly synchronized. This is currently one of the most important areas to improve before building complex UIs.

### Tabs Layout
Tabs need special handling because only the active tab should be rendered, inspected, and allowed to receive input. Inactive tab contents should not participate in the visible layout pass unless explicitly debugging inactive tabs.

### Styling System
The framework would benefit from a stronger style system:

- panel backgrounds
- borders
- hover styles
- active states
- spacing tokens
- theme variants
- reusable style presets

### Better Responsive Layout

The framework should continue improving behavior across different GUI scales and resolutions.

---

## Design Goals
UIWeaver aims to become:

1. **Declarative** — UI code should describe structure, not pixel math.
2. **Server-safe** — client interactions should become validated server actions.
3. **Composable** — complex screens should be made from reusable components.
4. **Debuggable** — layout and sync issues should be visible and inspectable.
5. **Modder-friendly** — creating a usable UI should require less boilerplate than vanilla screens.
6. **Minecraft-aware** — inventories, slots, tooltips, GUI scale, and server sync must be first-class concerns.

---

## Future Roadmap
Potential future improvements:

- style/theme API
- reusable panel/card components
- better slot binding and absolute slot synchronization
- tab-aware layout engine
- animation support
- declarative tooltips
- server-validated form inputs
- screen editor
- drag-and-drop layout debugging
- reusable UI templates
- better support for Forge, Fabric and NeoForge

---
## Project Status
UIWeaver is currently a prototype framework. It already proves the core idea, building Minecraft UI's from a declarative component tree with layout, sync, actions, and debugging tools.

The next major step is stabilizing layout behavior, especially for tabs and inventory slots, then improving the styling system so UI's can look polished instead of purely functional.

---
## License

UIWeaver is licensed under the **GNU Lesser General Public License v3.0**.

This means you may use UIWeaver as a library in your own Mods, including closed-source or commercial projects, as long as modifications to UIWeaver itself remain available under the LGPL-3.0 license.

See the `LICENSE` file for the full license text.
