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
comment|/**  * Represents a strategy for closing an object down such as using the @PreDestroy  * lifecycle from JSR 250, invoking {@link java.io.Closeable#close()} or using  * the DisposableBean interface from Spring.  *   * @version  */
end_comment

begin_interface
DECL|interface|Closer
specifier|public
interface|interface
name|Closer
block|{
comment|/**      * Closes the given object      *       * @param object      *            the object to be closed      * @throws Exception      *             if the close operation caused some exception to occur      */
DECL|method|close (Object object)
name|void
name|close
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Throwable
function_decl|;
block|}
end_interface

end_unit

