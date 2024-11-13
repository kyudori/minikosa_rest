package com.kosa.mini.api.dto.member;


import com.kosa.mini.api.entity.ContactUs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContactUsDTO {
    @NotNull
    private Integer memberId;

    @NotNull
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private static String title;

    @NotNull
    @NotBlank(message = "가게이름은 필수 입력 항목입니다.")
    private static String storeName;

    @NotNull
    @NotBlank(message = "내용은 필수 입력 항목입니다.")
    private static String content;


    public static ContactUs toEntity(){
        return ContactUs.builder()
                .title(title)
                .storeName(storeName)
                .content(content)
                .build();
    }

}
