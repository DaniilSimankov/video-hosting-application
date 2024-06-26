import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {VideoDto} from "./video-dto";
import {CommentDto} from "./comment-dto";

@Injectable({
    providedIn: 'root'
})
export class CommentsService {

    constructor(private httpClient: HttpClient) {
    }

    postComment(commentDto: any, videoId: string): Observable<any> {
        return this.httpClient.post<any>("http://localhost:8080/api/video/" + videoId + "/comment", commentDto);
    }

    getAllComments(videoId: string): Observable<Array<CommentDto>> {
        return this.httpClient.get<CommentDto[]>("http://localhost:8080/api/video/" + videoId + "/comment");
    }

    deleteComment(videoId: string, commentId: string) {
        return this.httpClient.get<CommentDto[]>("http://localhost:8080/api/video/" + videoId + "/comment/" + commentId + "/delete");
    }
}
