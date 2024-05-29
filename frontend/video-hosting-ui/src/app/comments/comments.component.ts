import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../user.service";
import {CommentsService} from "../comments.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CommentDto} from "../comment-dto";

@Component({
    selector: 'app-comments',
    templateUrl: './comments.component.html',
    styleUrls: ['./comments.component.css']
})
export class CommentsComponent implements OnInit {

    @Input()
    videoId: string = '';

    commentsForm: FormGroup;
    commentsDto: CommentDto[] = [];

    constructor(private userService: UserService,
                private commentsService: CommentsService,
                private matSnackBar: MatSnackBar) {
        this.commentsForm = new FormGroup({
            comment: new FormControl('comment'),
        });
    }

    ngOnInit(): void {
        this.getComments();
    }

    postComment() {
        const comment = this.commentsForm.get('comment')?.value;

        const commentDto = {
            'commentText': comment
        }

        console.log(commentDto);

        this.commentsService.postComment(commentDto, this.videoId).subscribe(data => {
            this.matSnackBar.open("Comment Posted Successfully", "OK");

            this.commentsForm.get('comment')?.reset();
            this.getComments();
        });

    }

    getComments(){
        this.commentsService.getAllComments(this.videoId).subscribe(data => {
            this.commentsDto = data;

            console.log(data);
        });
    }

    deleteComment(commentId: string) {
        this.commentsService.deleteComment(this.videoId, commentId).subscribe(data => {
            this.commentsDto = data;

            console.log(data);
        });

    }
}
