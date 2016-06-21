# codepath

# Pre-work - Papyrus

Papyrus is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: Bhaskar Jaiswal

Time spent: 12-14 hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can successfully add and remove items from the todo list.
*  [X] User can tap a todo item in the list and bring up a screen which will list
 task details. It will also provide the option to edit/delete or cancel the popup screen. The edited changes will reflect in the task description.
*  [X] User can persist todo items using sqlite and retrieve them properly on app restart

The following **optional** features are implemented:

* [X] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [X] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [X] Add support for completion due dates for todo items (and display within listview item)
* [ ] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [X] Add support for selecting the priority of each todo item (and display in listview item)
* [X] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [X] List anything else that you can get done to improve the app functionality!

Papyrus allows users to retrieve a deleted task within the first 3 seconds from the time the task was deleted. After 3 seconds, the task cannot be retrieved. A notification will appear at the bottom of the screen following a deletion. The notification contains a UNDO link which will retrieve the deleted task.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://imgur.com/sU2KJl8' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

1) Using CustomAdapter to list task name and priorities
2) Changing colors on the spinners.
3) Changing the background color on the icons
4) Returning result from Dialog fragment to Activity

## License

    Copyright [2016] [Lice]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
