package com.calebe.concurrency;

import com.calebe.concurrency.domain.VersionedProduct;
import com.calebe.concurrency.domain.VersionlessProduct;
import com.calebe.concurrency.support.TransactionRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.LockModeType;

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
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
			product.setStock(product.getStock() + 10);
		});
	}

	@Test
	void testDistinctVersionedOptimisticLocking() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product = em.find(VersionedProduct.class, 1);
			txRunner.executeInTransaction(em2 -> {
				VersionedProduct insideProduct = em.find(VersionedProduct.class, 1);
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
			product.setName("TV");
		});
	}

	@Test
	void testDistinctVersionlessOptimisticLocking() {
		txRunner.executeInTransaction(em -> {
			VersionlessProduct product = em.find(VersionlessProduct.class, 1);
			txRunner.executeInTransaction(em2 -> {
				VersionlessProduct insideProduct = em.find(VersionlessProduct.class, 1);
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
			product.setName("TV");
		});
	}

	@Test
	void testVersionlessOptimisticLocking() {
		txRunner.executeInTransaction(em -> {
			VersionlessProduct product = em.find(VersionlessProduct.class, 1);
			txRunner.executeInTransaction(em2 -> {
				VersionlessProduct insideProduct = em.find(VersionlessProduct.class, 1);
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
			product.setStock(product.getStock() + 1);
		});
	}

	@Test
	void testExplicitOptmisticLocking() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product = em.find(VersionedProduct.class, 1, LockModeType.OPTIMISTIC);
			//imagine calculating something very important here
			txRunner.executeInTransaction(em2 -> {
				VersionedProduct insideProduct = em.find(VersionedProduct.class, 1);
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
		});
	}

	@Test
	void testExplicitPessimisticLockingForceIncrement() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product = em.find(VersionedProduct.class, 1, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
		});
	}

	@Test
	void testExplicitPessimisticLockingRead() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product = em.find(VersionedProduct.class, 1, LockModeType.PESSIMISTIC_READ);
			txRunner.executeInTransaction(em2 ->{
				VersionedProduct insideProduct = em.find(VersionedProduct.class, 1, LockModeType.PESSIMISTIC_READ);
			});
		});
	}

	@Test
	void testExplicitPessimisticLockingWrite() {
		txRunner.executeInTransaction(em -> {
			VersionedProduct product =
					em.find(VersionedProduct.class, 1, LockModeType.PESSIMISTIC_WRITE);
			txRunner.executeInTransaction(em2 -> {
				VersionedProduct insideProduct = em.find(VersionedProduct.class, 1, LockModeType.PESSIMISTIC_WRITE);
				insideProduct.setStock(insideProduct.getStock() + 1);
			});
		});
	}

}
