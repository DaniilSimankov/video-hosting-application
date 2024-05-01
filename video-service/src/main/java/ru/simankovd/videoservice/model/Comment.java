package ru.simankovd.videoservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import ru.simankovd.videoservice.dto.CommentDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                .date(getCurrentDate())
                .build();
    }

    static String getCurrentDate() {
        return LocalDateTime.now().getDayOfMonth()+ "-" +
                LocalDateTime.now().getMonthValue() + "-" +
                LocalDateTime.now().getYear() + " " +
                LocalDateTime.now().getHour() + ":" +
                LocalDateTime.now().getMinute();
    }


}
