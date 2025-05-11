import sqlite3
import os
import shutil
import time

# Paths
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
SQL_SCRIPT_PATH = os.path.join(SCRIPT_DIR, 'create_bible_db.sql')
OLD_DB_PATH = os.path.join(SCRIPT_DIR, 'bible.db')
NEW_DB_PATH = os.path.join(SCRIPT_DIR, 'bible_new.db')

def create_new_database():
    # Remove the new database file if it exists
    if os.path.exists(NEW_DB_PATH):
        os.remove(NEW_DB_PATH)
    
    # Read the SQL script
    with open(SQL_SCRIPT_PATH, 'r') as f:
        sql_script = f.read()
    
    # Create a new database
    conn = sqlite3.connect(NEW_DB_PATH)
    cursor = conn.cursor()
    
    # Execute the SQL script to create tables with the correct schema
    cursor.executescript(sql_script)
    
    # Commit the changes
    conn.commit()
    
    return conn, cursor

def copy_data(old_conn, new_conn):
    old_cursor = old_conn.cursor()
    new_cursor = new_conn.cursor()
    
    # Get all tables from the old database
    old_cursor.execute("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'")
    tables = old_cursor.fetchall()
    
    # Copy data from each table
    for table in tables:
        table_name = table[0]
        print(f"Copying data from table: {table_name}")
        
        try:
            # Get column names
            old_cursor.execute(f"PRAGMA table_info({table_name})")
            columns = old_cursor.fetchall()
            column_names = [col[1] for col in columns]
            
            # Get data from the old table
            old_cursor.execute(f"SELECT * FROM {table_name}")
            rows = old_cursor.fetchall()
            
            # Insert data into the new table
            if rows:
                placeholders = ','.join(['?' for _ in column_names])
                insert_sql = f"INSERT INTO {table_name} ({','.join(column_names)}) VALUES ({placeholders})"
                
                for row in rows:
                    try:
                        new_cursor.execute(insert_sql, row)
                    except sqlite3.Error as e:
                        print(f"Error inserting row into {table_name}: {e}")
                        print(f"Row data: {row}")
                        continue
        except sqlite3.Error as e:
            print(f"Error processing table {table_name}: {e}")
            continue
    
    # Commit the changes
    new_conn.commit()
    print("Data copy completed successfully!")

def main():
    print("Starting database migration...")
    
    try:
        # Connect to the old database
        old_conn = sqlite3.connect(OLD_DB_PATH)
        
        # Create the new database
        new_conn, _ = create_new_database()
        
        # Copy data from the old database to the new one
        copy_data(old_conn, new_conn)
        
        # Close connections
        old_conn.close()
        new_conn.close()
        
        # Backup the old database and replace it with the new one
        backup_path = os.path.join(SCRIPT_DIR, 'bible.db.bak')
        
        # Try to backup and replace the old database
        try:
            shutil.copy2(OLD_DB_PATH, backup_path)
            os.remove(OLD_DB_PATH)
            os.rename(NEW_DB_PATH, OLD_DB_PATH)
            print(f"Migration completed successfully!")
            print(f"Old database backed up to: {backup_path}")
            print(f"New database created at: {OLD_DB_PATH}")
        except PermissionError:
            print(f"Could not replace the old database file because it's being used by another process.")
            print(f"Please close any applications that might be using the database file.")
            print(f"A new database has been created at: {NEW_DB_PATH}")
            print(f"You can manually replace the old database with the new one when it's not in use.")
    except Exception as e:
        print(f"An error occurred: {e}")
        if os.path.exists(NEW_DB_PATH):
            print(f"A new database was created at: {NEW_DB_PATH}")
            print("You can manually replace the old database with the new one.")

if __name__ == "__main__":
    main() 