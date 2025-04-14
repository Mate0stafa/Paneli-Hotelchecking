package com.example.paneli.Services;

import com.example.paneli.Models.Property;
import com.example.paneli.Models.ReviewsTab;
import com.example.paneli.Repositories.ReviewsTabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    
    public double getReviewSum(Property property){
        Integer revCount = 0;
        if (property.getReviewsTabList() != null){
            revCount = Math.toIntExact(property.getReviewsTabList().stream().count());
        } else {
            return 5.0; // Return default value with one decimal place
        }

        Double sum = 0.0;
        for (int i = 0; i < revCount; i++){
            sum += property.getReviewsTabList().get(i).getMesatare();
        }

        if (sum == 0){
            return 5.0; // Return default value with one decimal place
        } else {
            double average = sum / revCount;
            DecimalFormat df = new DecimalFormat("#.0"); // Format to one decimal place
            return Double.parseDouble(df.format(average));
        }
    }

//    Kodi i vjeter per mesataren e review
//    public double getReviewSum(Property property){
//        Integer revCount =0;
//        if (property.getReviewsTabList()!=null){
//            revCount = Math.toIntExact(property.getReviewsTabList().stream().count());
//
//        }else {
//            return 5;
//        }
//
//        Double sum = Double.valueOf(0);
//        for (int i=0;i<revCount;i++){
//            sum+=property.getReviewsTabList().get(i).getMesatare();
//        }
//
//
//        if (sum==0){
//            return 5;
//        }else return sum/revCount;
//    }

    public Map<Integer, Double> getRatingPercentages(Property property) {
        Map<Integer, Double> ratingPercentages = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order
        List<ReviewsTab> reviews = property.getReviewsTabList();
        if (reviews == null || reviews.isEmpty()) {
            return ratingPercentages;
        }

        int totalCount = reviews.size();
        int[] countByRatingLevel = new int[6]; // index 0 is unused, ratings are from 1 to 5

        // Count reviews for each rating level
        for (ReviewsTab review : reviews) {
            int rating = (int) Math.ceil(review.getMesatare()); // Round up to the nearest integer
            countByRatingLevel[rating]++;
        }

        // Calculate percentage for each rating level in reverse order (from 5 to 1)
        for (int i = 5; i >= 1; i--) {
            double percentage = (double) countByRatingLevel[i] / totalCount * 100;
            ratingPercentages.put(i, percentage);
        }

        return ratingPercentages;
    }

    @Autowired
    ReviewsTabRepository reviewsTabRepository;

    public void deleteReviewsTab(Long reviewsTabId){
        System.out.println(reviewsTabId + "rrrrrrrrrr");
        ReviewsTab reviewsTab = reviewsTabRepository.findReviewsTabById(reviewsTabId);
        System.out.println((reviewsTab.getId() + "iddd") );
        reviewsTabRepository.deleteReviewsTabById(reviewsTab.getId());
    }

}