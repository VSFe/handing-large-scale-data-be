package com.vsfe.largescale.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 마이그레이션 처리를 위한 ThreadPoolExecutor
 * 기본 제공 ThreadPoolExecutor 가 충족하지 못하는 이래 조건들을 위해 커스텀을 수행함.
 * - 모든 작업이 완료될 때 까지 대기할 수 있어야 한다.
 * - 예외 발생 시, 중간에 작업이 중단되지 않고, 로깅으로 처리할 수 있어야 한다. (후처리를 위함)
 * - 모든 작업이 수행된 후, Hold 한 예외를 throw 한다.
 */
@RequiredArgsConstructor
@Slf4j
public class C4ThreadPoolExecutor implements Executor {
	private final int threadCount;
	private final int queueSize;
	private ThreadPoolExecutor threadPoolExecutor;
	private RuntimeException exception = null;

	/**
	 *
	 */
	public void init() {
		// init() 은 1회 호출
		if (threadPoolExecutor != null) {
			return;
		}

		threadPoolExecutor = new ThreadPoolExecutor(threadCount, threadCount, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(queueSize),
			(runnable, executor) -> {
				try {
					executor.getQueue().put(runnable);
				} catch (InterruptedException e) {
					log.error(e.toString(), e);
					Thread.currentThread().interrupt();
				}
			});
	}

	/**
	 *
	 * @param runnable the runnable task
	 */
	@Override
	public void execute(Runnable runnable) {
		if (isInvalidState()) {
			return;
		}

		threadPoolExecutor.execute(() -> {
			try {
				runnable.run();
			} catch (RuntimeException e) {
				log.error(e.toString(), e);
				exception = e;
			}
		});
	}

	/**
	 * 모든 작업을 submit 한 이후, 작업 완료까지 대기한다.
	 * 만약, 예외가 존재할 시 모든 작업 수행 후 예외를 반환하도록 한다.
	 */
	public void waitToEnd() {
		if (isInvalidState()) {
			return;
		}

		threadPoolExecutor.shutdown();
		while (true) {
			try {
				if (threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS)) {
					break;
				}
			} catch (InterruptedException e) {
				log.error(e.toString(), e);
				Thread.currentThread().interrupt();
			}
		}

		threadPoolExecutor = null;
		if (exception != null) {
			throw exception;
		}
	}

	private boolean isInvalidState() {
		return threadPoolExecutor == null || threadPoolExecutor.isTerminated() || threadPoolExecutor.isTerminating();
	}
}
