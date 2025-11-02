# DIT301_ZFRiomalos_Act05

## Activity 5: NoteKeeperApp SQLite CRUD Reflection

### 1. How did I implement CRUD using SQLite?

I used the **`NotesDatabaseHelper`** class, which extends **`SQLiteOpenHelper`** to manage the database creation and table schema. 

* **Create (Add):** I used the **`insertNote()`** method along with **`ContentValues`** to add new records to the database.
* **Read (List):** I used the **`getAllNotes()`** method, executing a raw SQL query (`SELECT * FROM notes`) and utilizing a **`Cursor`** to retrieve and process all saved notes.
* **Update (Edit):** I used the **`updateNote()`** method, targeting a specific note ID with a **WHERE clause**.
* **Delete (Remove):** I used the **`deleteNote()`** method, also applying a **WHERE clause** to remove the selected record by its ID.

### 2. What challenges did I face in maintaining data persistence?

The main challenge I faced was with the **testing environment**, not the application code itself.

* **Emulator Issues:** I had serious problems running the intended Nougat (**API 25**) AVD due to frequent termination and system conflicts (likely with the computer's virtualization). I was forced to switch to a **newer, more stable emulator (like Pixel 4/API 30)** to finish the testing.
* **Code-Side Persistence:** Data persistence was successfully maintained in the code by calling the **`loadNotes()`** function (which performs the Read operation) within the **`MainActivity.onResume()`** lifecycle method. This strategy guarantees that fresh data is loaded from the SQLite database every time the user opens the app. 

### 3. How could I improve performance or UI design in future versions?

* **Performance:** For better long-term performance and cleaner code, I should replace raw SQLite with the **Android Room Persistence Library**. Room handles all the manual Cursor and query management automatically.
* **Efficiency:** I should implement **`DiffUtil`** in the `NotesAdapter` to update only the specific notes that have changed, which is much faster and more efficient than the current full refresh (**`notifyDataSetChanged()`**).
* **UI Design:** I could improve the user experience by adding features like a **search bar** and using modern navigation patterns.
