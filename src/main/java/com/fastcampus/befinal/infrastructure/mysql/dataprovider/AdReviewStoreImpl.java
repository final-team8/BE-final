package com.fastcampus.befinal.infrastructure.mysql.dataprovider;

import com.fastcampus.befinal.common.annotation.DataProvider;
import com.fastcampus.befinal.common.response.error.exception.BusinessException;
import com.fastcampus.befinal.domain.dataprovider.AdProvisionReader;
import com.fastcampus.befinal.domain.dataprovider.AdReviewStore;
import com.fastcampus.befinal.domain.dataprovider.AdvertisementReader;
import com.fastcampus.befinal.domain.entity.AdProvision;
import com.fastcampus.befinal.domain.entity.AdReview;
import com.fastcampus.befinal.domain.entity.Advertisement;
import com.fastcampus.befinal.domain.info.IssueAdInfo;
import com.fastcampus.befinal.domain.repository.AdReviewRepository;
import com.fastcampus.befinal.infrastructure.mysql.mapper.AdReviewEntityMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import static com.fastcampus.befinal.common.response.error.info.IssueAdErrorCode.NOT_FOUND_ISSUE_REVIEW_ID;

@DataProvider
@RequiredArgsConstructor
public class AdReviewStoreImpl implements AdReviewStore {
    private final AdReviewRepository adReviewRepository;
    private final EntityManager entityManager;
    private final AdReviewEntityMapper adReviewEntityMapper;

    @Override
    public void saveAdReview(IssueAdInfo.IssueAdReviewSaveInfo info){
        AdReview review = adReviewEntityMapper.from(info);
        adReviewRepository.save(review);
    }
    @Override
    public void updateAdReview(IssueAdInfo.IssueAdReviewUpdateInfo info){
        AdReview review = adReviewRepository.findById(info.reviewId())
            .orElseThrow(()-> new BusinessException(NOT_FOUND_ISSUE_REVIEW_ID));
        review.updateSentenceAndOpinion(info.sentence(),info.opinion());
        review.updateAdProvision(info.adProvision());
    }
    @Override
    public void deleteAdReview(IssueAdInfo.IssueAdReviewDeleteInfo info){
        adReviewRepository.findById(info.reviewId())
            .orElseThrow(()-> new BusinessException(NOT_FOUND_ISSUE_REVIEW_ID));
        adReviewRepository.deleteById(info.reviewId());
    }
}
