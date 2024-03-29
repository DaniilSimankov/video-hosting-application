import {Component, OnInit} from '@angular/core';
import {LoginResponse, OidcSecurityService} from "angular-auth-oidc-client";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
    title = 'video-hosting-ui';


    constructor(private oidcSecurityService: OidcSecurityService) {
    }

    ngOnInit(): void {
        this.oidcSecurityService
            .checkAuth()
            .subscribe(({isAuthenticated}) => {
                console.log('app is authenticated', isAuthenticated);
            });
    }
}
