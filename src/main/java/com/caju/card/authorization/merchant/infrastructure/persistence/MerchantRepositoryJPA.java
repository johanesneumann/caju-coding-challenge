package com.caju.card.authorization.merchant.infrastructure.persistence;

import com.caju.card.authorization.merchant.domain.Merchant;
import com.caju.card.authorization.merchant.domain.MerchantRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepositoryJPA extends MerchantRepository, JpaRepository<Merchant, Long> {
}
