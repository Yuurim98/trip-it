package com.example.tripit.community.service;

import com.example.tripit.community.dto.CommunityDTO;
import com.example.tripit.community.dto.CommunityUpdateDTO;
import com.example.tripit.community.dto.MetroENUM;
import com.example.tripit.community.entity.PostEntity;
import com.example.tripit.community.repository.PostRepository;
import com.example.tripit.schedule.entity.ScheduleEntity;
import com.example.tripit.schedule.repository.ScheduleRepository;
import com.example.tripit.user.entity.UserEntity;
import com.example.tripit.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {

    private PostRepository postRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public CommunityService(PostRepository postRepository, UserRepository userRepository,ScheduleRepository scheduleRepository ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public void postProcess(PostEntity postEntity) {

        postRepository.save(postEntity);

    }

    //최신순 조회
    public List<CommunityDTO> loadCommunityListOrderByPostDate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> posts = (Page<PostEntity>) postRepository.findAllByOrderByPostDateDesc(pageable);

        return posts.stream()
                .map(post -> {
                    UserEntity user = post.getUserId();
                    ScheduleEntity schedule = post.getScheduleId();

                    return new CommunityDTO(
                            post.getPostId(),
                            post.getPostTitle(),
                            post.getPostContent(),
                            post.getPersonnel(),
                            post.getViewCount(),
                            post.getExposureStatus(),
                            post.getPostPic(),
                            post.getPostDate(),
                            user.getUserId(),
                            user.getNickname(),
                            user.getGender(),
                            user.getBirth(),
                            user.getUserpic(),
                            schedule.getScheduleId(),
                            schedule.getMetroId(),
                            MetroENUM.getNameById(schedule.getMetroId()),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    );
                })
                .collect(Collectors.toList());
    }

    //조회순으로 조회
    public List<CommunityDTO> loadCommunityListOrderByViewCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostEntity> posts = (Page<PostEntity>) postRepository.findAllByOrderByViewCountDesc(pageable);

        return posts.stream()
                .map(post -> {
                    UserEntity user = post.getUserId();
                    ScheduleEntity schedule = post.getScheduleId();

                    return new CommunityDTO(
                            post.getPostId(),
                            post.getPostTitle(),
                            post.getPostContent(),
                            post.getPersonnel(),
                            post.getViewCount(),
                            post.getExposureStatus(),
                            post.getPostPic(),
                            post.getPostDate(),
                            user.getUserId(),
                            user.getNickname(),
                            user.getGender(),
                            user.getBirth(),
                            user.getUserpic(),
                            schedule.getScheduleId(),
                            schedule.getMetroId(),
                            MetroENUM.getNameById(schedule.getMetroId()),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    );
                })
                .collect(Collectors.toList());
    }

    //검색 서비스
    public List<CommunityDTO> searchCommunityByQueryAndMetroId(String query, String metroId) {
        List<PostEntity> posts;

        if (metroId.equals("0")) {
            posts = postRepository.searchByQuerydOrderByPostDateDesc(query);
        } else {
            posts = postRepository.searchByQueryAndMetroIdOrderByPostDateDesc(query, metroId);
        }
        return posts.stream()
                .map(post -> {
                    UserEntity user = post.getUserId();
                    ScheduleEntity schedule = post.getScheduleId();

                    return new CommunityDTO(
                            post.getPostId(),
                            post.getPostTitle(),
                            post.getPostContent(),
                            post.getPersonnel(),
                            post.getViewCount(),
                            post.getExposureStatus(),
                            post.getPostPic(),
                            post.getPostDate(),
                            user.getUserId(),
                            user.getNickname(),
                            user.getGender(),
                            user.getBirth(),
                            user.getUserpic(),
                            schedule.getScheduleId(),
                            schedule.getMetroId(),
                            MetroENUM.getNameById(schedule.getMetroId()),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    );
                })
                .collect(Collectors.toList());
    }


    //상세조회
    public List<CommunityDTO> loadCommunityDetail(Long userId, Long postId) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);

        List<PostEntity> posts = postRepository.findByUserIdAndPostId(user, postId);
        return posts.stream()
                .map(post -> {
                    UserEntity userEntity = post.getUserId();
                    ScheduleEntity schedule = post.getScheduleId();

                    return new CommunityDTO(
                            post.getPostId(),
                            post.getPostTitle(),
                            post.getPostContent(),
                            post.getPersonnel(),
                            post.getViewCount(),
                            post.getExposureStatus(),
                            post.getPostPic(),
                            post.getPostDate(),
                            userEntity.getUserId(),
                            userEntity.getNickname(),
                            userEntity.getGender(),
                            userEntity.getBirth(),
                            userEntity.getUserpic(),
                            schedule.getScheduleId(),
                            schedule.getMetroId(),
                            MetroENUM.getNameById(schedule.getMetroId()),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    );
                })
                .collect(Collectors.toList());
    }

    //조회수 증가
    public void incrementViewCount (long postId){
        postRepository.incrementViewCountByPostId(postId);
    }

    // 상세 글 수정
    public void updatePost(long userId, Long postId, CommunityUpdateDTO updateDTO) {


        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        long postUserId = post.getUserId().getUserId(); //글쓴이userId


        if(postUserId == userId){
            post.setPostTitle(updateDTO.getPostTitle());
            post.setPostContent(updateDTO.getPostContent());
            post.setPostPic(updateDTO.getPostPic());
            postRepository.save(post);

        }else{
            System.out.println("logged" + userId);
            System.out.println("postuserId" + post.getUserId().getUserId());
            throw new RuntimeException("User does not have permission to update this post");
        }


    }

    //상세 글 삭제
    public void deletePost(long userId, Long postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        long postUserId = post.getUserId().getUserId(); //글쓴이userId
        System.out.println("posted" + postUserId);
        System.out.println("logged" + userId);

        if(postUserId == userId){
            postRepository.delete(post);
        }else{
            throw new RuntimeException("User does not have permission to update this post");
        }
    }

    public void updateExposureState(long userId, Long postId) {

        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        long postUserId = post.getUserId().getUserId(); //글쓴이userId
        System.out.println("posted" + postUserId);
        System.out.println("logged" + userId);

        if(postUserId == userId){
            postRepository.updateExposureStatus(postId);
            System.out.println("exposureStatus updated");
        }else{

            throw new RuntimeException("User does not have permission to update this post");
        }
    }
}
