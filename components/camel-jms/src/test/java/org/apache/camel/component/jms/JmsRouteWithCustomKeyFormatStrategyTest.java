begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
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
name|BindToRegistry
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * With the passthrough option  */
end_comment

begin_class
DECL|class|JmsRouteWithCustomKeyFormatStrategyTest
specifier|public
class|class
name|JmsRouteWithCustomKeyFormatStrategyTest
extends|extends
name|JmsRouteWithDefaultKeyFormatStrategyTest
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myJmsKeyStrategy"
argument_list|)
DECL|field|strategy
specifier|private
name|MyCustomKeyFormatStrategy
name|strategy
init|=
operator|new
name|MyCustomKeyFormatStrategy
argument_list|()
decl_stmt|;
DECL|method|getUri ()
specifier|protected
name|String
name|getUri
parameter_list|()
block|{
return|return
literal|"activemq:queue:foo?jmsKeyFormatStrategy=#myJmsKeyStrategy"
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|getUri
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyCustomKeyFormatStrategy
specifier|private
specifier|static
class|class
name|MyCustomKeyFormatStrategy
implements|implements
name|JmsKeyFormatStrategy
block|{
DECL|method|encodeKey (String key)
specifier|public
name|String
name|encodeKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|"FOO"
operator|+
name|key
operator|+
literal|"BAR"
return|;
block|}
DECL|method|decodeKey (String key)
specifier|public
name|String
name|decodeKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|StringHelper
operator|.
name|between
argument_list|(
name|key
argument_list|,
literal|"FOO"
argument_list|,
literal|"BAR"
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

