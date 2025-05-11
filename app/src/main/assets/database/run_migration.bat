@echo off
echo Running database migration script...
python create_new_db.py
echo.
echo If the script ran successfully, your database has been updated.
echo If you encountered any errors, please check the output above.
echo.
pause 