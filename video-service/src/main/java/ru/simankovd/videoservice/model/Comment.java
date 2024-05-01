package ru.simankovd.videoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.simankovd.videoservice.dto.CommentDto;

import static ru.simankovd.videoservice.utils.DateUtil.getCurrentDateCommentFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    private String id;
    private String text;
    private String email;
    private String nickname;
    private Integer likeCount;
    private Integer dislikeCount;
    private String date;

    public static Comment from(CommentDto dto) {

        return Comment.builder()
                .text(dto.getCommentText())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .date(getCurrentDateCommentFormat())
                .build();
    }


}
