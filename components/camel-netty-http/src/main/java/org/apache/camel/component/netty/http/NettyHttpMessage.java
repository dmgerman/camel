begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_comment
comment|/**  * Netty HTTP based {@link org.apache.camel.Message}.  *<p/>  * This implementation allows direct access to the Netty {@link HttpRequest} using  * the {@link #getHttpRequest()} method.  */
end_comment

begin_class
DECL|class|NettyHttpMessage
specifier|public
class|class
name|NettyHttpMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|httpRequest
specifier|private
specifier|final
specifier|transient
name|HttpRequest
name|httpRequest
decl_stmt|;
DECL|method|NettyHttpMessage (HttpRequest httpRequest)
specifier|public
name|NettyHttpMessage
parameter_list|(
name|HttpRequest
name|httpRequest
parameter_list|)
block|{
name|this
operator|.
name|httpRequest
operator|=
name|httpRequest
expr_stmt|;
block|}
DECL|method|getHttpRequest ()
specifier|public
name|HttpRequest
name|getHttpRequest
parameter_list|()
block|{
return|return
name|httpRequest
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|DefaultMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|NettyHttpMessage
argument_list|(
name|httpRequest
argument_list|)
return|;
block|}
block|}
end_class

end_unit

