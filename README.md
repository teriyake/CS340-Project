# CMSC340 Project
## Description
This program attempts to create a realistic schedule that is as close to the optimal as possible given a series of constraints (room size, time slots, professor availability) and student preferences.

Since this problem is NP-hard, to solve it in a reasonable amount of time, we have made several additional assumptions :
- all time slots are non-overlapping
- a professor teaches exactly 2 courses 
- if a course does not have any professor available to teach, it is eliminated from consideration even if there are students who have requested it
- labs must be scheduled in Park, and all rooms in Park can accommodate any lab
- English (or other non-STEM) classes can be scheduled in Park, but labs have priority over all other courses for rooms in Park
- all non-lab classes can be scheduled in any room


## Usage
### Data Processing
```
cd .\src
```
Process constraints & student preferences files from the raw ``.csv`` file:
```
.\data\get_bmc_info.py <enrollment.csv> <student_prefs> <constraints>
.\data\mask_data.py <constraints> <student_prefs> <new_constraints> <new_prefs>
```
Process the newly generated constraint & preferences files from above to include labs & room types: 
```
javac Utils.java
java Utils <enrollment.csv> <constraints> <new_constraints>
```
If ```java Utils``` receives no argument, ```.\data\Spring2015.csv``` is processed by default.

### Schedule Creation
```
javac Scheduling.java
java Scheduling <constraints_and_preferences>
```
```<constraints_and_preferences>``` is an optional argument that specifies which constraints & preferences files the program will use. For example, when ```java Scheduling 5``` is executed, the program will use constraints file ```.\data\c_5``` and preferences file ```.\data\s_5```.
If ```java Scheduling``` receives no argument, it will use the constraints & preferences files in ```.\tests```.

## Credits
Collaborators: Silvia Alemany, Teri Ke, Paige Schaefer

```.\src\data\mask_data.py```: Trang Dang
## Notes
```.\src\is_valid.pl``` does not work
