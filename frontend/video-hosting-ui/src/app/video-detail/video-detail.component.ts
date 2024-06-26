import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {VideoService} from "../video.service";
import {UserService} from "../user.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
    selector: 'app-video-detail',
    templateUrl: './video-detail.component.html',
    styleUrls: ['./video-detail.component.css']
})
export class VideoDetailComponent implements OnInit {


    videoId!: string;
    videoUrl!: string;
    authorId!: string;
    authorNickname!: string;
    videoAvailable: boolean = false;
    videoTitle!: string;
    date!: string;
    subscribersCount!: string;
    videoDescription!: string;
    videoTags: Array<string> = [];
    likeCount: number = 0;
    dislikeCount: number = 0;
    viewCount: number = 0;
    showSubscribeButton: boolean = true;
    showUnsubscribeButton: boolean = false;
    isAuthor: boolean = true;

    private router: Router;


    constructor(private activatedRoute: ActivatedRoute,
                private videoService: VideoService,
                private userService: UserService, private matSnackBar: MatSnackBar,
                router: Router) {

        this.router = router;

        this.videoId = this.activatedRoute.snapshot.params['videoId'];

        this.videoService.getVideo(this.videoId).subscribe(data => {
            this.videoUrl = data.videoUrl;
            this.videoTitle = data.title;
            this.videoDescription = data.description;
            this.videoTags = data.tags;
            this.videoAvailable = true;
            this.likeCount = data.likeCount;
            this.dislikeCount = data.dislikeCount;
            this.viewCount = data.viewCount;
            this.authorId = data.authorId;
            this.isAuthor = data.isAuthor;
            this.authorNickname = data.authorNickname;
            this.date = data.date;
            this.subscribersCount = data.subscribersCount;
            if (data.isSubscribed) {
                this.showSubscribeButton = false;
                this.showUnsubscribeButton = true;
            } else {
                this.showSubscribeButton = true;
                this.showUnsubscribeButton = false;
            }
        });
    }

    ngOnInit(): void {
    }

    likeVideo() {
        this.videoService.likeVideo(this.videoId).subscribe(data => {
            this.likeCount = data.likeCount;
            this.dislikeCount = data.dislikeCount;
        });
    }

    dislikeVideo() {
        this.videoService.dislikeVideo(this.videoId).subscribe(data => {
            this.likeCount = data.likeCount;
            this.dislikeCount = data.dislikeCount;
        });

    }

    subscribeToUser() {
        let userId = this.authorId;
        this.userService.subscribeToUser(userId).subscribe(data => {
            this.showSubscribeButton = false;
            this.showUnsubscribeButton = true;
        });
    }

    unsubscribeToUser() {
        let userId = this.authorId;
        this.userService.unsubscribeToUser(userId).subscribe(data => {
            this.showSubscribeButton = true;
            this.showUnsubscribeButton = false;
        });

    }

    deleteVideo() {
        this.videoService.deleteVideo(this.videoId).subscribe(data => {

            console.log("Deleting status: ", data);

            this.matSnackBar.open("Video deleted successfully", "OK ");

            this.router.navigateByUrl('');

        });

    }
}
