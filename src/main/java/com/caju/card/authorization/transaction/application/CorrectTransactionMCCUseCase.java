package com.caju.card.authorization.transaction.application;

import com.caju.card.authorization.UseCase;
import com.caju.card.authorization.merchant.domain.MerchantRepository;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class CorrectTransactionMCCUseCase {

    private final MerchantRepository merchantRepository;

    public String execute(CorrectTransactionMccPayload payload) {
        var find = merchantRepository.findByTransactionName(payload.merchantName().replace(" ", ""));
        if (find.isPresent()) {
            return find.get().getMcc();
        } else {
            return payload.transactionMcc();
        }
    }
}
