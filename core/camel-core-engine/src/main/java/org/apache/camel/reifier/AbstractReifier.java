begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|spi
operator|.
name|RouteContext
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
name|support
operator|.
name|CamelContextHelper
import|;
end_import

begin_class
DECL|class|AbstractReifier
specifier|public
specifier|abstract
class|class
name|AbstractReifier
block|{
DECL|method|parseString (RouteContext routeContext, String text)
specifier|protected
specifier|static
name|String
name|parseString
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|parseBoolean (RouteContext routeContext, String text)
specifier|protected
specifier|static
name|boolean
name|parseBoolean
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|Boolean
name|b
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|text
argument_list|)
decl_stmt|;
return|return
name|b
operator|!=
literal|null
operator|&&
name|b
return|;
block|}
DECL|method|parseLong (RouteContext routeContext, String text)
specifier|protected
specifier|static
name|Long
name|parseLong
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|parseInt (RouteContext routeContext, String text)
specifier|protected
specifier|static
name|Integer
name|parseInt
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|parse (RouteContext routeContext, Class<T> clazz, String text)
specifier|protected
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|parse
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|parse
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|clazz
argument_list|,
name|text
argument_list|)
return|;
block|}
block|}
end_class

end_unit

