package com.example.paneli.Services;

import com.example.paneli.Models.Promotion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class CalculatePriceService {
    /**
     * Kthen true nese promocioni eshte valid per nje dit te dhen
     * */
    public boolean isPromotionValid(Promotion promotion, Date date) {
        return promotion != null &&
                promotion.getStatus() != null &&
                promotion.getStatus().equals("Yes") &&
                promotion.getStartDate() != null &&
                promotion.getEndDate() != null &&
                !date.before(promotion.getStartDate()) &&
                !date.after(promotion.getEndDate());
    }
    /**
     * Kthen cmimin pjesetuar me taksat si nje BigDecimal,
     * merr taksat nga qyteti si float, cmimin e dhomes
     * me ose pa discount si dhe nese taksat jane te perfshira.
     * */
    public BigDecimal getTaxes(Float price, Float cityTax, Boolean calculated) {
        if (price != null) {
            cityTax = cityTax == null ? 3f : cityTax;
            BigDecimal hundred = BigDecimal.valueOf(100.0);
            BigDecimal prices = BigDecimal.valueOf(price);
            if (calculated) {
                return prices.multiply(BigDecimal.valueOf(cityTax)).divide(hundred, 2, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.valueOf(0L);
    }
}
