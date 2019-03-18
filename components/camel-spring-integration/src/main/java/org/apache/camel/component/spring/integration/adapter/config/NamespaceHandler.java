begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.integration.adapter.config
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
operator|.
name|config
package|;
end_package

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|NamespaceHandlerSupport
import|;
end_import

begin_comment
comment|/**  * The name space handler Spring Integration Camel Adapter  */
end_comment

begin_class
DECL|class|NamespaceHandler
specifier|public
class|class
name|NamespaceHandler
extends|extends
name|NamespaceHandlerSupport
block|{
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|registerBeanDefinitionParser
argument_list|(
literal|"camelSource"
argument_list|,
operator|new
name|CamelSourceAdapterParser
argument_list|()
argument_list|)
expr_stmt|;
name|registerBeanDefinitionParser
argument_list|(
literal|"camelTarget"
argument_list|,
operator|new
name|CamelTargetAdapterParser
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

