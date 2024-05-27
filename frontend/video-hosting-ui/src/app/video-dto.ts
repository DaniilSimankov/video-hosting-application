export interface VideoDto {
    id: string;
    title: string;
    description: string;
    tags: Array<string>;
    videoUrl: string;
    // videoStatus: string;
    thumbnailUrl: string;
    likeCount: number;
    dislikeCount: number;
    viewCount: number;
    authorId: string;
    authorNickname: string;
    date: string;
    subscribersCount: string;
    isSubscribed: boolean;
    isAuthor: boolean;
}