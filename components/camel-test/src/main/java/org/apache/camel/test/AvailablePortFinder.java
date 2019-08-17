begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Finds currently available server ports.  */
end_comment

begin_class
DECL|class|AvailablePortFinder
specifier|public
specifier|final
class|class
name|AvailablePortFinder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AvailablePortFinder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Creates a new instance.      */
DECL|method|AvailablePortFinder ()
specifier|private
name|AvailablePortFinder
parameter_list|()
block|{
comment|// Do nothing
block|}
comment|/**      * Gets the next available port.      *      * @throws IllegalStateException if there are no ports available      * @return the available port      */
DECL|method|getNextAvailable ()
specifier|public
specifier|static
name|int
name|getNextAvailable
parameter_list|()
block|{
try|try
init|(
name|ServerSocket
name|ss
init|=
operator|new
name|ServerSocket
argument_list|()
init|)
block|{
name|ss
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ss
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
operator|(
name|InetAddress
operator|)
literal|null
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|int
name|port
init|=
name|ss
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"getNextAvailable() -> {}"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find free port"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

