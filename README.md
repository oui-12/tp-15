# 🏦 Gestion de Comptes Bancaires avec Spring Boot et GraphQL 🚀

![Image](https://github.com/user-attachments/assets/d6bd58d1-1d1b-4be9-a367-66c273705c06)
![Image](https://github.com/user-attachments/assets/f44f6f6d-fa6a-4d5e-8f0e-a84d54a3974e)
![Image](https://github.com/user-attachments/assets/145d01d3-f0ff-4e32-bdb8-706d0ffb441f)
![Image](https://github.com/user-attachments/assets/bcb21b53-aff6-422f-8a7b-759a624ae3eb)
![Image](https://github.com/user-attachments/assets/69f3e60c-8128-453e-a3f2-1cc061a03dc9)
![Image](https://github.com/user-attachments/assets/829ac676-4614-4aee-a8f6-1e47257f5aeb)

## 📋 Table des matières
- [📌 Introduction](#-introduction)
- [✨ Fonctionnalités](#-fonctionnalités)
- [🛠️ Prérequis techniques](#️-prérequis-techniques)
- [🚀 Installation](#-installation)
- [⚙️ Configuration](#️-configuration)
- [📚 Modèle de données](#-modèle-de-données)
- [🔍 API GraphQL](#-api-graphql)
- [🧪 Tests](#-tests)
- [📊 Base de données H2](#-base-de-données-h2)
- [🔒 Sécurité](#-sécurité)
- [🚦 Bonnes pratiques](#-bonnes-pratiques)
- [📈 Améliorations possibles](#-améliorations-possibles)
- [🤝 Contribution](#-contribution)
- [📄 Licence](#-licence)

## 📌 Introduction
Ce projet est une application de gestion de comptes bancaires développée avec Spring Boot et GraphQL. Elle permet de gérer des comptes bancaires, d'effectuer des opérations de dépôt et de retrait, et de consulter les transactions.

## ✨ Fonctionnalités

### 🏦 Gestion des Comptes
- ✅ Création de comptes bancaires
- 🔍 Consultation des détails d'un compte
- 📋 Liste de tous les comptes
- 📊 Calcul du solde total

### 💰 Gestion des Transactions
- ➕ Ajout de transactions (dépôts/retraits)
- 📜 Historique des transactions par compte
- 📊 Statistiques globales des transactions
- 🔄 Mise à jour automatique des soldes

### 🌐 Interface GraphQL
- 🎯 Requêtes optimisées
- 📱 Documentation interactive (GraphiQL)
- 🔄 Réponses en temps réel

## 🛠️ Prérequis techniques

### 📋 Logiciels requis
- Java 17 ou supérieur ☕
- Maven 3.6+ 📦
- IDE (IntelliJ IDEA, Eclipse, VS Code) 💻
- Git 🐙

### 📚 Dépendances principales
```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- GraphQL Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-graphql</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

## 🚀 Installation

### 1. Cloner le dépôt
```bash
git clone https://github.com/oui-12/tp-15.git
cd tp-15
```

### 2. Compiler le projet
```bash
mvn clean install
```

### 3. Démarrer l'application
```bash
mvn spring-boot:run
```

## ⚙️ Configuration

### Fichier [application.properties](cci:7://file:///C:/Users/ElKansouli%20Ouiam/Downloads/banque-service/banque-service/src/main/resources/application.properties:0:0-0:0)
```properties
# Application Configuration
spring.application.name=banque-service
server.port=9090

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:banque
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# GraphQL
spring.graphql.graphiql.enabled=true
spring.graphql.schema.locations=classpath:graphql/
spring.graphql.schema.file-extensions=.graphqls
```

## 📚 Modèle de données

### Entité Compte
```java
@Entity
@Data
public class Compte {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double solde;
    private Date dateCreation;
    
    @Enumerated(EnumType.STRING)
    private TypeCompte type;
    
    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
}
```

### Entité Transaction
```java
@Entity
@Data
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double montant;
    private Date date;
    
    @Enumerated(EnumType.STRING)
    private TypeTransaction type;
    
    @ManyToOne
    @JoinColumn(name = "compte_id")
    private Compte compte;
}
```

## 🔍 API GraphQL

### Schéma GraphQL (`compte.graphqls`)
```graphql
type Query {
    allComptes: [Compte]
    compteById(id: ID!): Compte
    totalSolde: Float
    compteTransactions(id: ID!): [Transaction]
    allTransactions: [Transaction]
    transactionStats: TransactionStats
}

type Mutation {
    saveCompte(compte: CompteInput): Compte
    addTransaction(transaction: TransactionInput): Transaction
}

type Compte {
    id: ID!
    solde: Float!
    dateCreation: String!
    type: TypeCompte!
    transactions: [Transaction]
}

type Transaction {
    id: ID!
    montant: Float!
    date: String!
    type: TypeTransaction!
    compte: Compte
}

type TransactionStats {
    count: Int!
    sumDepots: Float!
    sumRetraits: Float!
}

input CompteInput {
    id: ID
    solde: Float!
    dateCreation: String
    type: TypeCompte!
}

input TransactionInput {
    compteId: ID!
    montant: Float!
    date: String
    type: TypeTransaction!
}

enum TypeCompte {
    COURANT,
    EPARGNE
}

enum TypeTransaction {
    DEPOT,
    RETRAIT
}
```

## 🧪 Tests

### Exemples de requêtes

#### Créer un compte
```graphql
mutation {
  saveCompte(compte: {
    solde: 1000.0,
    type: COURANT
  }) {
    id
    solde
    type
  }
}
```

#### Effectuer un dépôt
```graphql
mutation {
  addTransaction(transaction: {
    compteId: 1,
    montant: 500.0,
    type: DEPOT
  }) {
    id
    montant
    type
    compte {
      id
      solde
    }
  }
}
```

#### Consulter les transactions
```graphql
query {
  compteTransactions(id: 1) {
    id
    montant
    date
    type
  }
}
```

#### Voir les statistiques
```graphql
query {
  transactionStats {
    count
    sumDepots
    sumRetraits
  }
}
```

## 📊 Base de données H2

### Accès à la console H2
- URL: http://localhost:9090/h2-console
- JDBC URL: jdbc:h2:mem:banque
- User Name: sa
- Password: (laisser vide)

## 🔒 Sécurité

### Recommandations
- 🔑 Implémenter Spring Security
- 🔐 Activer HTTPS
- 🔒 Valider les entrées utilisateur
- 📝 Journalisation des opérations sensibles

## 🚦 Bonnes pratiques

### Code
- ✅ Validation des entrées
- ✅ Gestion des erreurs
- ✅ Tests unitaires et d'intégration
- ✅ Documentation du code

### Architecture
- 🏗️ Couches séparées (Controller, Service, Repository)
- 🔄 Transactions atomiques
- 📊 Optimisation des requêtes

## 📈 Améliorations possibles

### Fonctionnalités
- 🔄 Virements entre comptes
- 📱 Interface utilisateur
- 📧 Notifications par email
- 📱 API REST complémentaire

### Technique
- 🔍 Pagination des résultats
- 🔄 Mise en cache
- 📊 Monitoring
- 🔄 Migration vers une base de données de production

## 🤝 Contribution

1. Fork le projet
2. Crée une branche (`git checkout -b feature/AmazingFeature`)
3. Commit tes changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvre une Pull Request

## 📄 Licence

Ce projet est sous licence MIT - voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

<div align="center">
  <p>Développé avec ❤️ par [Votre Nom]</p>
  <p>📧 contact@example.com | 🌐 https://votresite.com</p>
</div>
