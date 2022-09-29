package com.calebe.concurrency;

import com.calebe.concurrency.domain.VersionedProduct;
import com.calebe.concurrency.support.TransactionRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConcurrencyApplicationTests {

	@Autowired
	private TransactionRunner txRunner;

	@Test
	void testVersionedOptimisticLocking() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product = em.find(VersionedProduct.class, 1);
			txRunner.executeInTransaction(em2 -> {
				VersionedProduct insideProduct = em.find(VersionedProduct.class, 1);
				insideProduct.setStock(insideProduct.getStock() + 1);//Here sets to 4
			});
			product.setStock(product.getStock() + 10);//But here updates the database with 13. Which means it doesn't take into account the previous update
		});
	}

}
