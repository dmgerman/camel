begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|mail
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Endpoint
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
name|component
operator|.
name|gae
operator|.
name|bind
operator|.
name|OutboundBinding
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/gmail.html">Google App Engine Mail  * Component</a> supports outbound mail communication. It makes use of the mail  * service provided by Google App Engine.  */
end_comment

begin_class
DECL|class|GMailComponent
specifier|public
class|class
name|GMailComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|OutboundBinding
name|binding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"outboundBindingRef"
argument_list|,
name|OutboundBinding
operator|.
name|class
argument_list|,
operator|new
name|GMailBinding
argument_list|()
argument_list|)
decl_stmt|;
name|GMailEndpoint
name|endpoint
init|=
operator|new
name|GMailEndpoint
argument_list|(
name|uri
argument_list|,
name|getCamelContext
argument_list|()
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setOutboundBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

