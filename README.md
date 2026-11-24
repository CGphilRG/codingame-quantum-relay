> 🇫🇷 [Version française](README_fr.md)

# Quantum Relay — CodinGame Multiplayer SDK Project

A complete multiplayer algorithmic puzzle based on **dynamic network energy flow** and **computational geometry (laser beam segment intersection)**, built with the **CodinGame Game Engine (`4.7.7`)** across **3 progressive divisions** (**Wood 3 → Wood 2 → Wood 1**).

---

## 1. Quick Commands

### Launch Local Web Viewer (`http://localhost:8888/test.html`)
```bash
cd quantum-relay-sdk
mvn test-compile exec:java -Dexec.mainClass="Main" -Dexec.classpathScope="test"
```

### Run Batch Simulation (Headless)
```bash
cd quantum-relay-sdk
mvn test-compile exec:java -Dexec.mainClass="Main" -Dexec.classpathScope="test" -Dexec.args="--simulate"
```

---

## 2. Project Structure

* [`pom.xml`](pom.xml): Maven configuration with `core`, `module-entities`, `module-tooltip`, `module-endscreen`, `module-toggle`, and `runner` (`4.7.7`).
* [`config/config.ini`](config/config.ini): Game settings (`type=multi`, `min_players=2`, `max_players=2`).
* [`config/stub.txt`](config/stub.txt): Multi-language I/O stub generator.
* [`config/statement_en.html.tpl`](config/statement_en.html.tpl) & [`config/statement_fr.html.tpl`](config/statement_fr.html.tpl): League-gated HTML statements in English and French.
* `config/level1/Boss.java`, `config/level2/Boss.java`, `config/level3/Boss.java`: Progressive league Boss bots *(kept local-only and excluded via `.gitignore`)*.
* [`src/main/java/com/codingame/game/Referee.java`](src/main/java/com/codingame/game/Referee.java): Full referee, relay tree energy flow solver, and geometric beam interference detection.
* [`src/main/java/com/codingame/game/Geometry.java`](src/main/java/com/codingame/game/Geometry.java): Orientation predicates (`crossProduct`) and exact segment intersection solver.

---

## 3. Links

* **CodinGame Contribution**: https://www.codingame.com/contribute/view/15864612ee375b7372711a629aff1930a46f01
* **Play in CodinGame IDE**: https://www.codingame.com/ide/demo/1605690daf156413f9393afba6e22199f0011bc
* **6-Puzzle Showcase Hub (GitHub Pages)**: https://cgphilrg.github.io/codingame-sdk-puzzles/
