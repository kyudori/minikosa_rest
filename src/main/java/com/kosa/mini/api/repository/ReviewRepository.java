package com.kosa.mini.api.repository;

import com.kosa.mini.api.dto.review.ReviewReplyDTO;
import com.kosa.mini.api.dto.review.ReviewsUpdateDTO;
import com.kosa.mini.api.dto.search.SearchReviewInterfaceDTO;
import com.kosa.mini.api.dto.search.SearchStoreInterfaceDTO;
import com.kosa.mini.api.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT new com.kosa.mini.api.dto.review.ReviewReplyDTO(" +
            "r.store.storeId, " +
            "r.member.memberId, " +
            "r.reviewText, " +
            "r.rating, " +
            "r.createdAt, " +
            "rr.replyId, " +
            "r.reviewId, " +
            "rr.owner.memberId, " +
            "rr.replyText, " +
            "r.member.nickname) " +
            "FROM Review r " +
            "LEFT JOIN r.reviewReplies rr " +
            "WHERE r.store.storeId = :storeId " +
            "ORDER BY r.createdAt DESC")
    List<ReviewReplyDTO> findByStoreStoreId(@Param("storeId") Integer storeId);
    ReviewsUpdateDTO findByReviewId(Integer reviewId);

    @Query(value = "SELECT " +
            "rv.store_id AS storeId, " +
            "rv.member_id AS memberId, " +
            "rv.review_id AS reviewId, " +
            "s.store_name AS storeName, " +
            "m.nickname AS memberNickname, " +
            "rv.review_text AS reviewText, " +
            "rv.rating, " +
            "rv.created_at AS createdAt, " +
            "rv.updated_at AS updatedAt " +
            "FROM reviews rv " +
            "JOIN members m ON rv.member_id = m.member_id " +
            "JOIN stores s ON rv.store_id = s.store_id " +
            "WHERE rv.review_text LIKE CONCAT('%', :query, '%') " +
            "OR m.nickname LIKE CONCAT('%', :query, '%') " +
            "GROUP BY rv.store_id, rv.member_id, rv.review_id, s.store_name, m.nickname, rv.review_text, rv.rating, rv.created_at, rv.updated_at " +
            "ORDER BY :reviewSort ",
            nativeQuery = true)
    List<SearchReviewInterfaceDTO> findByReviewSearch(String query, String reviewSort);

    @Query(value = "SELECT " +
            "rv.store_id AS storeId, " +
            "rv.member_id AS memberId, " +
            "rv.review_id AS reviewId, " +
            "s.store_name AS storeName, " +
            "m.nickname AS memberNickname, " +
            "rv.review_text AS reviewText, " +
            "rv.rating, " +
            "rv.created_at AS createdAt, " +
            "rv.updated_at AS updatedAt " +
            "FROM reviews rv " +
            "JOIN members m ON rv.member_id = m.member_id " +
            "JOIN stores s ON rv.store_id = s.store_id " +
            "WHERE rv.review_text LIKE CONCAT('%', :query, '%') " +
            "OR m.nickname LIKE CONCAT('%', :query, '%') " +
            "GROUP BY rv.store_id, rv.member_id, rv.review_id, s.store_name, m.nickname, rv.review_text, rv.rating, rv.created_at, rv.updated_at " +
            "ORDER BY rv.updated_at DESC ",
            nativeQuery = true)
    List<SearchReviewInterfaceDTO> findByReviewSearchOrderByUpdatedAt(@Param("query") String query);

    @Query(value = "SELECT " +
            "rv.store_id AS storeId, " +
            "rv.member_id AS memberId, " +
            "rv.review_id AS reviewId, " +
            "s.store_name AS storeName, " +
            "m.nickname AS memberNickname, " +
            "rv.review_text AS reviewText, " +
            "rv.rating, " +
            "rv.created_at AS createdAt, " +
            "rv.updated_at AS updatedAt " +
            "FROM reviews rv " +
            "JOIN members m ON rv.member_id = m.member_id " +
            "JOIN stores s ON rv.store_id = s.store_id " +
            "WHERE rv.review_text LIKE CONCAT('%', :query, '%') " +
            "OR m.nickname LIKE CONCAT('%', :query, '%') " +
            "GROUP BY rv.store_id, rv.member_id, rv.review_id, s.store_name, m.nickname, rv.review_text, rv.rating, rv.created_at, rv.updated_at " +
            "ORDER BY rv.rating DESC ",
            nativeQuery = true)
    List<SearchReviewInterfaceDTO> findByReviewSearchOrderByRating(@Param("query") String query);
}
