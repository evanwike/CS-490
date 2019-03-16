import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';

import {ApiService} from '../api.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css']
})
export class CustomerEditComponent implements OnInit {

  customerForm: FormGroup;
  id = '';
  submitted: false;

  constructor(private router: Router, private route: ActivatedRoute, private api: ApiService, private formBuilder: FormBuilder) {
    this.customerForm = this.formBuilder.group({
      name: this.formBuilder.group(
        {
          first: ['', Validators.required],
          last: ['', Validators.required]
        }
      ),
      gender: ['', Validators.required],
      birthday: ['', Validators.required],
      lastContact: ['', Validators.required],
      customerLifetimeValue: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.api.getCustomer(this.route.snapshot.params['id'])
      .subscribe(data => {
        data.birthday = new Date(data.birthday);
        this.customerForm.patchValue(data);
      });
  }

  get f(): any {
    return this.customerForm.controls;
  }

  onFormSubmit() {
    this.api.updateCustomer(this.route.snapshot.params['id'], this.customerForm.value)
      .subscribe(res => {
        const id = res['_id'];
        this.router.navigate(['/customer-details', id]);
      }, (err) => {
        console.log(err);
      });
  }

  onReset() {
    this.submitted = false;
    this.customerForm.reset();
  }
}
