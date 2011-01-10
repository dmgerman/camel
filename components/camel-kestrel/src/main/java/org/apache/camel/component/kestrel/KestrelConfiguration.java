begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
package|;
end_package

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Represents the configuration of the Kestrel component and/or endpoint.  */
end_comment

begin_class
DECL|class|KestrelConfiguration
specifier|public
class|class
name|KestrelConfiguration
implements|implements
name|Cloneable
block|{
comment|/**      * The default port on which kestrel runs      */
DECL|field|DEFAULT_KESTREL_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_KESTREL_PORT
init|=
literal|22133
decl_stmt|;
comment|/**      * The address(es) on which kestrel is running      */
DECL|field|addresses
specifier|private
name|String
index|[]
name|addresses
init|=
operator|new
name|String
index|[]
block|{
literal|"localhost:"
operator|+
name|DEFAULT_KESTREL_PORT
block|}
decl_stmt|;
comment|/**      * How long a given wait should block (server side), in milliseconds      */
DECL|field|waitTimeMs
specifier|private
name|int
name|waitTimeMs
init|=
literal|100
decl_stmt|;
comment|/**      * How many concurrent listeners to schedule for the thread pool      */
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|method|getAddresses ()
specifier|public
name|String
index|[]
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
DECL|method|setAddresses (String[] addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|String
index|[]
name|addresses
parameter_list|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
block|}
DECL|method|getWaitTimeMs ()
specifier|public
name|int
name|getWaitTimeMs
parameter_list|()
block|{
return|return
name|waitTimeMs
return|;
block|}
DECL|method|setWaitTimeMs (int waitTimeMs)
specifier|public
name|void
name|setWaitTimeMs
parameter_list|(
name|int
name|waitTimeMs
parameter_list|)
block|{
name|this
operator|.
name|waitTimeMs
operator|=
name|waitTimeMs
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
if|if
condition|(
name|concurrentConsumers
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid value for concurrentConsumers: "
operator|+
name|concurrentConsumers
argument_list|)
throw|;
block|}
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getAddressesAsString ()
specifier|public
name|String
name|getAddressesAsString
parameter_list|()
block|{
name|StringBuilder
name|bld
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|address
range|:
name|addresses
control|)
block|{
if|if
condition|(
name|bld
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bld
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|bld
operator|.
name|append
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
return|return
name|bld
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getInetSocketAddresses ()
specifier|public
name|List
argument_list|<
name|InetSocketAddress
argument_list|>
name|getInetSocketAddresses
parameter_list|()
block|{
name|List
argument_list|<
name|InetSocketAddress
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|InetSocketAddress
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|address
range|:
name|addresses
control|)
block|{
name|String
index|[]
name|tok
init|=
name|address
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|host
decl_stmt|;
name|int
name|port
decl_stmt|;
if|if
condition|(
name|tok
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|host
operator|=
name|tok
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|tok
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tok
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|host
operator|=
name|tok
index|[
literal|0
index|]
expr_stmt|;
name|port
operator|=
name|DEFAULT_KESTREL_PORT
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid address: "
operator|+
name|address
argument_list|)
throw|;
block|}
name|list
operator|.
name|add
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|copy ()
specifier|public
name|KestrelConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|KestrelConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

