import {Component, inject, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatChipInputEvent} from "@angular/material/chips";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {ActivatedRoute} from "@angular/router";
import {VideoService} from "../video.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
    selector: 'app-save-video-details',
    templateUrl: './save-video-details.component.html',
    styleUrls: ['./save-video-details.component.css']
})
export class SaveVideoDetailsComponent implements OnInit {

    saveVideoDetailsForm: FormGroup;
    title: FormControl = new FormControl('');
    description: FormControl = new FormControl('');
    videoStatus: FormControl = new FormControl('');

    readonly separatorKeysCodes = [ENTER, COMMA] as const;
    selectable = true;
    removable = true;
    addOnBlur = true;
    selectedFile!: File;
    videoId = '';
    fileSelected = false;

    selectedFileName = '';

    tags: string[] = [];
    announcer = inject(LiveAnnouncer);

    constructor(private activatedRoute: ActivatedRoute, private videoService: VideoService,
                private matSnackBar: MatSnackBar) {
        this.videoId = this.activatedRoute.snapshot.params['videoId'];

        this.saveVideoDetailsForm = new FormGroup({
            title: this.title,
            description: this.description,
            videoStatus: this.videoStatus,
        });
    }

    ngOnInit(): void {
    }

    add(event: MatChipInputEvent): void {
        const value = (event.value || '').trim();

        // Add our fruit
        if (value) {
            this.tags.push(value);
        }

        // Clear the input value
        event.chipInput!.clear();
    }

    remove(value: string): void {
        const index = this.tags.indexOf(value);

        if (index >= 0) {
            this.tags.splice(index, 1);

            this.announcer.announce(`Removed ${value}`);
        }
    }

    onFileSelected($event: Event) {
        // @ts-ignore
        this.selectedFile = event.target.files[0];
        this.selectedFileName = this.selectedFile.name;
        this.fileSelected = true;
    }

    onUpload() {
        this.videoService.uploadThumbnail(this.selectedFile, this.videoId)
            .subscribe(data => {
                console.log(data);
                // show an upload success notification
                this.matSnackBar.open("Thumbnail upload Successful", "OK");
            });

    }
}

