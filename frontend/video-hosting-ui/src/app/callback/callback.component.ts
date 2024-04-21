import { Component, OnInit } from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-callback',
  templateUrl: './callback.component.html',
  styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

  // todo callback
  constructor(private userService: UserService, router: Router) {
    this.userService.registerUser();
    router.navigateByUrl('/featured');
  }

  ngOnInit(): void {
  }

}
