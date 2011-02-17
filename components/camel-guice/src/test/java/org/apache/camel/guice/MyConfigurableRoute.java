begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.guice
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|guice
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * A route which is reused multiple times with different configurations  *  * @version   */
end_comment

begin_class
DECL|class|MyConfigurableRoute
specifier|public
class|class
name|MyConfigurableRoute
extends|extends
name|RouteBuilder
block|{
DECL|field|foo
specifier|protected
name|Endpoint
name|foo
decl_stmt|;
DECL|field|bar
specifier|protected
name|Endpoint
name|bar
decl_stmt|;
annotation|@
name|Inject
DECL|method|MyConfigurableRoute (@amedR) Endpoint bar, @Named(R) Endpoint foo)
specifier|public
name|MyConfigurableRoute
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"bar"
argument_list|)
name|Endpoint
name|bar
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"foo"
argument_list|)
name|Endpoint
name|foo
parameter_list|)
block|{
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
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
name|foo
argument_list|)
operator|.
name|to
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

