begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
operator|.
name|support
package|;
end_package

begin_comment
comment|/**  * A handler of exceptions when closing down resources. Implementations will  * typically create a collection of error messages so that all of the different  * close exceptions are collected together.  *   * @version  */
end_comment

begin_interface
DECL|interface|CloseErrors
specifier|public
interface|interface
name|CloseErrors
block|{
comment|/**      * Notification of a close exception      *       * @param key      *            the key of the object being closed which is usually a      *            {@link com.google.inject.Key} or {@link String}      * @param object      *            the object being closed      * @param cause      *            the exception thrown when the close was attempted      */
DECL|method|closeError (Object key, Object object, Exception cause)
name|void
name|closeError
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|object
parameter_list|,
name|Exception
name|cause
parameter_list|)
function_decl|;
DECL|method|throwIfNecessary ()
name|void
name|throwIfNecessary
parameter_list|()
throws|throws
name|CloseFailedException
function_decl|;
block|}
end_interface

end_unit

