import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'CalculatorICP';
  currVal = 0;
  view = [];
  expression = [];
  last;

  clear() {
    this.currVal = 0;
    this.view = [];
    this.expression = [];
    this.last = null;
  }

  // Responsible for handling and routing input
  press(e) {
    if (Number.isInteger(this.last) && Number.isInteger(e)) {
      const n = Number(String(this.last) + String(e));
      this.currVal = n;
      this.view.pop();
      this.view.push(n);
      this.expression[this.expression.length - 1] = n;
      this.last = n;
    } else {
      this.currVal = e;
      this.view.push(e);
      this.expression.push(e);
      this.last = e;
    }
  }

  // Sums the expression and resets variables
  evaluate() {
    this.currVal = this.crunch(this.expression);
    this.view = [this.currVal];
    this.expression = [this.currVal];
    this.last = null;
  }

  // Recursively collapse and sum the expression array
  crunch(expr) {
    if (expr.length === 1) {
      return expr[0];
    }

    const n = expr[0];
    expr = expr.slice(1);

    const op = expr[0];
    expr = expr.slice(1);

    if (op === '+') {
      return n + this.crunch(expr);
    } else if (op === '-') {
      return n - this.crunch(expr);
    } else if (op === '*') {
      return n * this.crunch(expr);
    } else if (op === '/') {
      return n / this.crunch(expr);
    } else {
      return n % this.crunch(expr);
    }
  }
}
