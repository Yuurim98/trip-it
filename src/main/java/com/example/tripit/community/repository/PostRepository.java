package com.example.tripit.community.repository;

<<<<<<< HEAD
import com.example.tripit.community.dto.CommunityDTO;
import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.user.entity.UserEntity;
=======
import java.util.List;

>>>>>>> chat
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.user.entity.UserEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    //커뮤니티 상세글 조회
    List<PostEntity> findByUserIdAndPostId(UserEntity userId, Long postId);

    List<PostEntity> findByUserId(UserEntity userId);

    //날짜순으로 정렬
    Page<PostEntity> findAllByOrderByPostDateDesc(Pageable pageable);

    //조회순으로 정렬
    Page<PostEntity> findAllByOrderByViewCountDesc(Pageable pageable);

    //조회수 증가
    @Query("UPDATE PostEntity c SET c.viewCount = c.viewCount + 1 WHERE c.postId = :postId")
    @Modifying
    @Transactional
    void incrementViewCountByPostId(@Param("postId") long postId);

    // 커뮤니티 검색
    @Query("SELECT p FROM PostEntity p WHERE " +
            "(LOWER(p.postTitle) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "p.scheduleId.metroId = :metroId " +
            "ORDER BY p.postDate DESC")
    List<PostEntity> searchByQueryAndMetroIdOrderByPostDateDesc(@Param("query") String query, @Param("metroId") String metroId);

    //전체검색
    @Query("SELECT p FROM PostEntity p WHERE " +
            "(LOWER(p.postTitle) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY p.postDate DESC")
    List<PostEntity> searchByQuerydOrderByPostDateDesc(@Param("query") String query);

    //모집완료 설정
    @Modifying
    @Transactional
    @Query("UPDATE PostEntity p SET p.exposureStatus = false WHERE p.postId = :postId")
    void updateExposureStatus(@Param("postId") Long postId);

    PostEntity findByPostId(Long postId);

    @Query("SELECT new  com.example.tripit.community.dto.CommunityDTO(p.postId, p.postTitle, p.postContent, p.personnel, p.viewCount, p.exposureStatus, p.postPic, p.postDate, u.userId, u.nickname, u.gender, u.birth, u.userpic, s.scheduleId, s.metroId, s.startDate, s.endDate) " +
            "FROM PostEntity p " +
            "JOIN p.userId u " +
            "JOIN p.scheduleId s " +
            "WHERE u.userId = :userId " +
            "ORDER BY p.postDate DESC")
    List<CommunityDTO> findPostsByUserId(@Param("userId") Long userId);

}
