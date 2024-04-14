package ru.simankovd.videoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.simankovd.videoservice.dto.CommentDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;
    private String text;
    private String authorId;
    private Integer likeCount;
    private Integer dislikeCount;

    public static Comment from(CommentDto dto){
        return Comment.builder()
                .text(dto.getCommentText())
                .authorId(dto.getAuthorId())
                .build();
    }
}
