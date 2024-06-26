package ru.simankovd.videoservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.simankovd.videoservice.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String id;
    private String commentText;
    private String email;
    private String nickname;
    private String date;
    private Boolean isAuthor;

    public static CommentDto from(Comment comment){
        return  CommentDto.builder()
                // todo добавить айдишник коммента для удаления
                .id(comment.getId())
                .email(comment.getEmail())
                .commentText(comment.getText())
                .nickname(comment.getNickname())
                .date(comment.getDate().toString())
                .build();
    }

    public static List<CommentDto> from(List<Comment> comments){
        return comments.stream().map(CommentDto::from).collect(Collectors.toList());
    }
}
