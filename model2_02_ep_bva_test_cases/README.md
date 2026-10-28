# Model 2 - Experiment 2: Equivalence Partitioning & Boundary Value Analysis Test Cases

## 📌 Experiment Overview
* **Aim / Objective:** Design test cases for an e-commerce application using equivalence partitioning and boundary value analysis. Include valid and invalid inputs for login, product quantity, search fields, and payment details.
* **Techniques Used:**
  * **Equivalence Partitioning (EP):** Dividing input domains into valid (VP) and invalid (IP) equivalent classes.
  * **Boundary Value Analysis (BVA):** Testing boundary points ($min-1, min, min+1, nominal, max-1, max, max+1$).
* **Main Java File to Write in Lab Exam:** [`EpBvaTestRunner.java`](file:///d:/PROJECTS/stalab/model2_02_ep_bva_test_cases/src/main/java/com/sta/epbva/EpBvaTestRunner.java)

---

## 🚀 How to Run on a New Computer

### 1. Prerequisites
* **Java 17 or 21 LTS** installed (`java -version`).
* **Apache Maven 3.8+** installed (`mvn -version`).

### 2. Run Commands
```bash
cd model2_02_ep_bva_test_cases

# Run Unit Tests
mvn clean test

# Run Main Automated Test Suite
mvn compile exec:java
```

---

## 📑 Lab Manual
Refer to [`EXP.md`](file:///d:/PROJECTS/stalab/model2_02_ep_bva_test_cases/EXP.md) for the complete laboratory writeup (Aim, Algorithm/Procedure, Main Java Program, and Sample Execution Output).
