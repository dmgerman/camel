begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.adapter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|integration
operator|.
name|adapter
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * The Abstract class for the Spring Integration Camel Adapter  */
end_comment

begin_class
DECL|class|AbstractCamelAdapter
specifier|public
specifier|abstract
class|class
name|AbstractCamelAdapter
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|camelEndpointUri
specifier|private
name|String
name|camelEndpointUri
decl_stmt|;
DECL|field|expectReply
specifier|private
specifier|volatile
name|boolean
name|expectReply
init|=
literal|true
decl_stmt|;
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|camelContext
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getCamelEndpointUri ()
specifier|public
name|String
name|getCamelEndpointUri
parameter_list|()
block|{
return|return
name|camelEndpointUri
return|;
block|}
DECL|method|setCamelEndpointUri (String uri)
specifier|public
name|void
name|setCamelEndpointUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|camelEndpointUri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|setExpectReply (boolean expectReply)
specifier|public
name|void
name|setExpectReply
parameter_list|(
name|boolean
name|expectReply
parameter_list|)
block|{
name|this
operator|.
name|expectReply
operator|=
name|expectReply
expr_stmt|;
block|}
DECL|method|isExpectReply ()
specifier|public
name|boolean
name|isExpectReply
parameter_list|()
block|{
return|return
name|expectReply
return|;
block|}
block|}
end_class

end_unit

