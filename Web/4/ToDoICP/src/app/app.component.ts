import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  items = [];

  // Write code to push new item
  submitNewItem(val) {
    if (val !== '') {
      this.items.push({val: val, complete: false})
    }
  }

  // Write code to complete item
  completeItem(todo) {
    for (let i = 0; i < this.items.length; i++) {
      if (this.items[i] === todo) {
        this.items[i].complete = true;
      }
    }
  }

  // Write code to delete item
  deleteItem(todo) {
    for (let i = 0; i < this.items.length; i++) {
      if (this.items[i] === todo) {
        this.items.splice(i, 1);
      }
    }
  }

}
