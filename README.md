# Essentia

Essentia is a lightweight Fabric mod for Minecraft that introduces a clean and intuitive system for storing and managing player XP.

The mod focuses on simplicity, clarity, and expandability. Version 1 adds the Essentia Vial, with future updates bringing the Essentia Bank block and a full XP management interface.

---

## What the Mod Does

Essentia allows players to:
- Store their XP safely in containers
- Carry XP between bases or worlds
- Save XP before dangerous encounters
- Share XP with teammates
- Use XP more strategically and efficiently

The goal is to replace the limitations of vanilla XP with a consistent, elegant, player-friendly XP ecosystem.

---

## Current Features

### **Essentia Vial**
A custom item that stores XP in precise units (XP points, not levels).

**Core behavior:**
- **Capacity:** 350 XP
- **Deposit:** Shift + Right-Click
- **Withdraw:** Right-Click (returns all stored XP)
- **Partial Fill:** Supported
- **Tooltip:** Shows exact XP stored
- **Dynamic Visual States:**
    - Empty
    - Partially filled
    - Full

The vial uses Minecraft’s modern data components and damage-based model dispatching to automatically update its appearance based on the fill level.

---

## Planned Features

### **Essentia Bank (Block)**
A stationary XP storage block with large capacity.
- Acts as a central XP reservoir
- Supports depositing/withdrawing XP
- Integrates naturally with Essentia Vials
- Ideal for mid/late-game storage setups

### **Custom GUI for the Essentia Bank**
A clean interface that will show:
- Current XP stored
- Transfer controls
- Vial slots
- Capacity indicators

### **User Experience Improvements**
- More refined tooltip indicators
- Optional HUD element for vial status

### **Audio & Animation**
- Sound effects when filling or withdrawing XP
- Light animation to make using the vial feel similar to drinking or channeling a potion

### **Stackable Empty Vials**
Empty Essentia Vials will be stackable (like vanilla glass bottles).  
When a player uses a stack:
- Only **one vial** separates from the stack
- That vial becomes the filled one
- The rest of the stack remains in the player’s hand

This keeps inventory management clean without breaking XP storage logic.