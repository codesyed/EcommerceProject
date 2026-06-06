/*
			JAVA → DATABASE INTERACTION EVOLUTION (JDBC → ORM → SPRING DATA)
Example: Student Table (id, name, age)
--------------------------------------------------
1️⃣ JDBC (Core Java Approach ❌)
🔹 Table Creation (Manual SQL)
CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    age INT
);

🔹 Insert Code
Connection con = DriverManager.getConnection(...);
String sql = "INSERT INTO student(name, age) VALUES (?, ?)";
PreparedStatement ps = con.prepareStatement(sql);

ps.setString(1, "Affan");
ps.setInt(2, 22);
ps.executeUpdate();

🔹 Transaction (Manual)
con.setAutoCommit(false);
try {
    ps.executeUpdate();
    con.commit();
} catch(Exception e) {
    con.rollback();
}

❌ Drawbacks:
- Manual SQL writing
- Manual table creation
- Manual mapping (ResultSet → Object)
- Boilerplate code
- Error-prone

--------------------------------------------------
2️⃣ ORM (JPA / Hibernate ✔)

👉 ORM = Object Relational Mapping (Class ↔ Table)
🔹 Entity (Table auto creation)
@Entity
@Table(name = "student")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
}

👉 Config:
spring.jpa.hibernate.ddl-auto=update

🔹 Insert WITHOUT @Transactional (Manual)

@PersistenceContext
EntityManager em;
em.getTransaction().begin();

Student s = new Student();
s.setName("Affan");
s.setAge(22);

em.persist(s);
em.getTransaction().commit();

🔹 Insert WITH @Transactional (Spring way 🔥)
@Transactional
public void saveStudent() {
    Student s = new Student();
    s.setName("Affan");
    s.setAge(22);
    em.persist(s);
}

👉 @Transactional:
- Starts transaction
- Commit if success
- Rollback if failure
👉 Meaning: ALL OR NOTHING

✅ Advantages over JDBC:
- No manual SQL
- No manual mapping
- Table auto creation
- Cleaner code

❌ Drawbacks:
- EntityManager use karna padta hai
- DAO layer likhni padti hai
- Still some boilerplate

👉 Internally:
✔ Hibernate/JPA STILL uses JDBC underneath

--------------------------------------------------
3️⃣ SPRING DATA (CrudRepository ✔)

👉 Problem: Why write same DAO code again?
🔹 Solution: Auto Repository

interface StudentRepo extends CrudRepository<Student, Long> {}
🔹 Insert
studentRepo.save(student);
✅ Solves:
- No DAO implementation
- No EntityManager usage
- Less boilerplate

❌ Limitation:
- No pagination
- No sorting

--------------------------------------------------
4️⃣ SPRING DATA JPA (JpaRepository 🔥)

👉 Advanced version of CrudRepository
interface StudentRepo extends JpaRepository<Student, Long> {}

🔹 Insert
studentRepo.save(new Student("Affan", 22));

🔹 Extra Features:
- Pagination
- Sorting
- Batch operations
- flush()

✅ Advantages:
- Production ready
- Fully automated DB interaction
- Minimal code

--------------------------------------------------
🔥 FINAL UNDERSTANDING

JDBC → Manual SQL & Table creation + Boilerplate and SQL Injection Like issues ❌
ORM (JPA/Hibernate) → Object Mapping ✔ auto Table Creation + auto java Object to DB row conversion no query mapping easy , xx but still more boilder plate code required also trnasaction management is hactic
Spring Data → Auto CRUD ✔  auto table creatin + auto java object to db row + simple method+low code then ORM direct but limited methods features then JPaa
JpaRepository → Advanced + Real World 🔥   --> every thing

--------------------------------------------------
🎯 INTERVIEW LINE

JDBC required manual SQL and transaction handling.
ORM (Hibernate/JPA) introduced object mapping and automatic schema generation but still required boilerplate code.
Spring Data JPA removed boilerplate by auto-generating repository implementations, making development faster and cleaner.

--------------------------------------------------
⚡ FINAL SHORTCUT

JDBC → Write SQL
JPA → Map Objects
Spring Data → Just call methods

 */