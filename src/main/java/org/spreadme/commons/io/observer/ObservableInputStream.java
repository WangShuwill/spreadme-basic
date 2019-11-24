/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.io.observer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ObservableInputStream
 * @author shuwei.wang
 * @since 1.0.0
 */
public abstract class ObservableInputStream extends InputStream {

	private final InputStream in;

	private final List<InputStreamObserver> observers = new ArrayList<>();

	public ObservableInputStream(final InputStream in) {
		this.in = in;
	}

	private long size;

	/**
	 * Adds an Observer.
	 *
	 * @param pObserver the observer to add
	 */
	public void add(final InputStreamObserver pObserver) {
		observers.add(pObserver);
	}

	/**
	 * Removes an Observer.
	 *
	 * @param pObserver the observer to remove
	 */
	public void remove(final InputStreamObserver pObserver) {
		observers.remove(pObserver);
	}

	/**
	 * Removes all Observers.
	 */
	public void removeAllObservers() {
		observers.clear();
	}

	/** Gets all currently registered observers.
	 * @return a list of the currently registered observers
	 */
	protected List<InputStreamObserver> getObservers() {
		return observers;
	}

	@Override
	public int read() throws IOException {
		int result = 0;
		IOException ioe = null;
		try {
			result = in.read();
		}
		catch (final IOException pException) {
			ioe = pException;
		}
		if (ioe != null) {
			noteError(ioe);
		}
		else if (result == -1) {
			noteFinished();
		}
		else {
			noteDataByte(result);
		}
		return result;
	}

	@Override
	public int read(byte[] buffer) throws IOException {
		int result = 0;
		IOException ioe = null;
		try {
			result = in.read(buffer);
		}
		catch (final IOException pException) {
			ioe = pException;
		}
		if (ioe != null) {
			noteError(ioe);
		}
		else if (result == -1) {
			noteFinished();
		}
		else if (result > 0) {
			noteDataBytes(buffer, 0, result);
		}
		return result;
	}

	@Override
	public int read(byte[] buffer, int off, int len) throws IOException {
		int result = 0;
		IOException ioe = null;
		try {
			result = in.read(buffer, off, len);
		}
		catch (final IOException pException) {
			ioe = pException;
		}
		if (ioe != null) {
			noteError(ioe);
		}
		else if (result == -1) {
			noteFinished();
		}
		else if (result > 0) {
			noteDataBytes(buffer, off, result);
		}
		return result;
	}

	/**
	 * Notifies the observers by invoking {@link InputStreamObserver#data(byte[], int, int)} with the given arguments.
	 *
	 * @param pBuffer Passed to the observers.
	 * @param pOffset Passed to the observers.
	 * @param pLength Passed to the observers.
	 * @throws IOException Some observer has thrown an exception, which is being passed down.
	 */
	protected void noteDataBytes(final byte[] pBuffer, final int pOffset, final int pLength) throws IOException {
		this.size += pLength;
		for (final InputStreamObserver observer : getObservers()) {
			observer.data(pBuffer, pOffset, pLength);
		}
	}

	/**
	 * Notifies the observers by invoking {@link InputStreamObserver#finished()}.
	 *
	 * @throws IOException Some observer has thrown an exception, which is being passed down.
	 */
	protected void noteFinished() throws IOException {
		for (final InputStreamObserver observer : getObservers()) {
			observer.finished();
		}
	}

	/**
	 * Notifies the observers by invoking
	 *
	 * @param pDataByte Passed to the observers.
	 * @throws IOException Some observer has thrown an exception, which is being passed down.
	 */
	protected void noteDataByte(final int pDataByte) throws IOException {
		this.size = size + 1;
		for (final InputStreamObserver observer : getObservers()) {
			observer.data(pDataByte);
		}
	}

	/** Notifies the observers by invoking {@link InputStreamObserver#error(IOException)} with the given argument.
	 *
	 * @param pException Passed to the observers.
	 * @throws IOException Some observer has thrown an exception, which is being passed down. This may be the same exception, which has been passed as an argument.
	 */
	protected void noteError(final IOException pException) throws IOException {
		for (final InputStreamObserver observer : getObservers()) {
			observer.error(pException);
		}
	}

	public long getSize() {
		return this.size;
	}
}
