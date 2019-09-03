begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Represents the core lifecycle API for services which can be initialized, started and stopped  */
end_comment

begin_interface
DECL|interface|Service
specifier|public
interface|interface
name|Service
extends|extends
name|AutoCloseable
block|{
comment|/**      * Initialize the service      *      * @throws RuntimeCamelException is thrown if initialization failed      */
DECL|method|init ()
specifier|default
name|void
name|init
parameter_list|()
block|{     }
comment|/**      * Starts the service      *      * @throws RuntimeCamelException is thrown if starting failed      */
DECL|method|start ()
name|void
name|start
parameter_list|()
function_decl|;
comment|/**      * Stops the service      *      * @throws RuntimeCamelException is thrown if stopping failed      */
DECL|method|stop ()
name|void
name|stop
parameter_list|()
function_decl|;
comment|/**      * Delegates to {@link Service#stop()} so it can be used in      * try-with-resources expression.      *       * @throws IOException per contract of {@link AutoCloseable} if      *             {@link Service#stop()} fails      */
DECL|method|close ()
specifier|default
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_interface

end_unit

