import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {Router} from "@angular/router";
import {OidcSecurityService} from "angular-auth-oidc-client";

@Component({
    selector: 'app-callback',
    templateUrl: './callback.component.html',
    styleUrls: ['./callback.component.css']
})
export class CallbackComponent implements OnInit {

    // todo callback

    private router: any;

    constructor(private userService: UserService, router: Router, private oidcSecurityService: OidcSecurityService) {
        this.router = router;

        this.oidcSecurityService
            .checkAuth()
            .subscribe(({isAuthenticated}) => {
                console.log('app is authenticated', isAuthenticated);
                if (isAuthenticated) {
                    this.userService.registerUser();
                    this.router.navigateByUrl('/featured');
                }
            });
    }

    ngOnInit(): void {
    }

}
