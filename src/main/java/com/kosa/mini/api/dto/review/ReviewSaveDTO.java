package com.kosa.mini.api.dto.review;

import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Store;
import com.kosa.mini.api.entity.Review;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveDTO {
    private Integer storeId;
    private Integer memberId; // 서버에서 설정하므로 클라이언트에서 전송할 필요 없음

    @NotBlank(message = "리뷰 텍스트는 필수입니다.")
    @Size(max = 1000, message = "리뷰 텍스트는 1000자를 초과할 수 없습니다.")
    private String reviewText;

    @NotNull(message = "평점은 필수입니다.")
    @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
    @Max(value = 5, message = "평점은 5 이하여야 합니다.")
    private Integer rating;

    // Review 엔티티로 변환하는 메서드
    public Review toEntity(Store store, Member member) {
        return Review.builder()
                .store(store)
                .member(member)
                .reviewText(this.reviewText)
                .rating(this.rating)
                .build();
    }
}
