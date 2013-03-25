/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mcxiaoke.commons.http;

import org.apache.http.HttpStatus;

public class StatusCodes {

	public static boolean isSuccess(int errorCode) {
		return errorCode >= HttpStatus.SC_OK
				&& errorCode < HttpStatus.SC_MULTIPLE_CHOICES;
	}

	public static boolean isRedirect(int errorCode) {
		return errorCode >= HttpStatus.SC_MULTIPLE_CHOICES
				&& errorCode < HttpStatus.SC_BAD_REQUEST;
	}

	public static boolean isRequestError(int errorCode) {
		return errorCode >= HttpStatus.SC_BAD_REQUEST
				&& errorCode < HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}

	public static boolean isServerError(int errorCode) {
		return errorCode >= HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}
}
