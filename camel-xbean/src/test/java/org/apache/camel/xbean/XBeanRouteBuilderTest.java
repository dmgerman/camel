begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.xbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|xbean
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
name|Exchange
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
name|RouteBuilderTest
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
comment|/**  * TODO: re-implement the route building logic using xbean and   * then test it by overriding the buildXXX methods in the RouteBuilderTest  *   * @version $Revision: 520164 $  */
end_comment

begin_class
DECL|class|XBeanRouteBuilderTest
specifier|public
class|class
name|XBeanRouteBuilderTest
extends|extends
name|RouteBuilderTest
block|{
annotation|@
name|Override
DECL|method|buildSimpleRoute ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRoute
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildSimpleRoute
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessor ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildCustomProcessor
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildCustomProcessor
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildCustomProcessorWithFilter ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildCustomProcessorWithFilter
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildCustomProcessorWithFilter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildRouteWithInterceptor ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildRouteWithInterceptor
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildRouteWithInterceptor
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithChoice ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRouteWithChoice
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildSimpleRouteWithChoice
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildSimpleRouteWithHeaderPredicate ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildSimpleRouteWithHeaderPredicate
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildSimpleRouteWithHeaderPredicate
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|buildWireTap ()
specifier|protected
name|RouteBuilder
argument_list|<
name|Exchange
argument_list|>
name|buildWireTap
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
name|super
operator|.
name|buildWireTap
argument_list|()
return|;
block|}
block|}
end_class

end_unit

