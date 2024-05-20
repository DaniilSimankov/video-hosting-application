import {Component, OnInit} from '@angular/core';
import {LoginResponse, OidcSecurityService} from "angular-auth-oidc-client";
import {UserService} from "./user.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'video-hosting-ui';

    private router: any;


    constructor(private oidcSecurityService: OidcSecurityService, private userService: UserService, router: Router) {
        this.router = router;
    }

    ngOnInit(): void {
        this.oidcSecurityService
            .checkAuth()
            .subscribe(({isAuthenticated}) => {
                console.log('app is authenticated', isAuthenticated);
                // this.userService.registerUser();
                // this.router.navigateByUrl('/featured');
            });
    }
}
