begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.placeholder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|placeholder
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|SimpleLanguageWithSpringPropertyPlaceholderRoute
specifier|public
class|class
name|SimpleLanguageWithSpringPropertyPlaceholderRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|fromEndpoint
specifier|private
name|String
name|fromEndpoint
decl_stmt|;
DECL|field|toEndpoint
specifier|private
name|String
name|toEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|fromEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|toEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|setFromEndpoint (String fromEndpoint)
specifier|public
name|void
name|setFromEndpoint
parameter_list|(
name|String
name|fromEndpoint
parameter_list|)
block|{
name|this
operator|.
name|fromEndpoint
operator|=
name|fromEndpoint
expr_stmt|;
block|}
DECL|method|setToEndpoint (String toEndpoint)
specifier|public
name|void
name|setToEndpoint
parameter_list|(
name|String
name|toEndpoint
parameter_list|)
block|{
name|this
operator|.
name|toEndpoint
operator|=
name|toEndpoint
expr_stmt|;
block|}
block|}
end_class

end_unit

