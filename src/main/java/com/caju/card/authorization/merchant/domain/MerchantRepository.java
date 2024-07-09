package com.caju.card.authorization.merchant.domain;

import java.util.Optional;

public interface MerchantRepository {

    Optional<Merchant> findByTransactionName(String transactionName);

}
